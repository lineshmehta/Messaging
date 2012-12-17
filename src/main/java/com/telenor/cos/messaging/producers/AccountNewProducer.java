package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Account;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.account.AccountNewEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.producers.xpath.AccountInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountNewProducer extends AbstractProducer{

    private static final Logger LOG = LoggerFactory.getLogger(AccountNewProducer.class);

    @Autowired
    private CustomerCache customerCache;

    @Autowired
    private AccountInsertXpathExtractor accountInsertXpathExtractor;
    
    @Override
    public List<Event> produceMessage(Node message) {
        Account account = createAccount(message);
        List<Event> eventsList = new ArrayList<Event>();
        AccountNewEvent accountNewEvent = new AccountNewEvent(account);
        eventsList.add(accountNewEvent);
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return accountInsertXpathExtractor.getAccountId(message) != null;
    }

    private Account createAccount(Node message) {
        Account account = new Account();
        account.setAccountId(XPathLong.getValue(accountInsertXpathExtractor.getAccountId(message)));
        account.setName(XPathString.getValue(accountInsertXpathExtractor.getAccountName(message)));
        account.setType(XPathString.getValue(accountInsertXpathExtractor.getAccountTypeId(message)));
        account.setStatus(XPathString.getValue(accountInsertXpathExtractor.getAccountStatusId(message)));
        account.setPaymentStatus(XPathString.getValue(accountInsertXpathExtractor.getAccountPaymentStatus(message)));
        account.setInvoiceFormat(XPathString.getValue(accountInsertXpathExtractor.getAccountInvoiceFormat(message)));
        Long custIdResp = XPathLong.getValue(accountInsertXpathExtractor.getAccountCustIdResp(message));
        account.setCustIdResp(custIdResp == null ? null : custIdResp);
        Long custIdPayer = XPathLong.getValue(accountInsertXpathExtractor.getAccountCustIdPayer(message));
        account.setCustIdPayer(custIdPayer == null ? null : custIdPayer);

        CachableCustomer customerPayerDetailsFromCache = (custIdPayer == null ? null : customerCache.get(custIdPayer));

        if(customerPayerDetailsFromCache != null) {
            account.setCustFirstNamePayer(customerPayerDetailsFromCache.getFirstName());
            account.setCustMiddleNamePayer(customerPayerDetailsFromCache.getMiddleName());
            account.setCustLastNamePayer(customerPayerDetailsFromCache.getLastName());
            account.setOrganizationNumberPayer(customerPayerDetailsFromCache.getCustUnitNumber());
            account.setMasterIdPayer(customerPayerDetailsFromCache.getMasterCustomerId());
            account.setInvoiceAddress(customerPayerDetailsFromCache.getPostcodeIdMain() + ' ' + customerPayerDetailsFromCache.getPostcodeNameMain());
        } else {
            LOG.warn("Customer with Customer Id [" + custIdPayer + "] was not found in CustomerCache");
        }

        CachableCustomer customerOwnerDetailsFromCache = (custIdResp == null ? null : customerCache.get(custIdResp));

        if(customerOwnerDetailsFromCache != null) {
            account.setMasterIdOwner(customerOwnerDetailsFromCache.getMasterCustomerId());
        } else {
            LOG.warn("Customer with Customer Id [" + custIdResp + "] was not found in CustomerCache");
        }
        return account;
    }
}
