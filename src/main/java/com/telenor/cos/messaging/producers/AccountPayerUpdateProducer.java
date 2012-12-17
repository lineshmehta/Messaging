package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.account.AccountPayerUpdateEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.producers.xpath.AccountUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for {@link com.telenor.cos.messaging.event.account.AccountPayerUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class AccountPayerUpdateProducer extends AbstractProducer {

    public static final Logger LOG = LoggerFactory.getLogger(AccountPayerUpdateProducer.class);

    @Autowired
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    @Autowired
    private CustomerCache customerCache;

    @Override
    public Boolean isApplicable(Node message) {
        return accountUpdateXpathExtractor.getNewCustIdPayer(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        CustomerName customerName = null;
        CustomerAddress customerAddress = null;
        Long accountId = XPathLong.getValue(accountUpdateXpathExtractor.getAccountId(message));
        String newCustIdPayer = XPathString.getValue(accountUpdateXpathExtractor.getNewCustIdPayer(message));

        if(StringUtils.isNotBlank(newCustIdPayer)) {
            Long custIdPayer = Long.valueOf(newCustIdPayer);
            CachableCustomer cachableCustomerPayer = customerCache.get(custIdPayer);
            customerName = new CustomerName(custIdPayer);
            customerAddress = new CustomerAddress(custIdPayer);
            if(cachableCustomerPayer != null) {
                createNewPayerCustomerName(customerName, cachableCustomerPayer);
                createNewPayerCustomerAddress(customerAddress,cachableCustomerPayer);
            } else {
                LOG.error("Customer with Customer Payer Id [" + newCustIdPayer + "] was not found in CustomerCache! Hence Customer Payer Details will not be updated for Account with id [" + accountId + "]");
            }
        }
        AccountPayerUpdateEvent accountPayerUpdateEvent = new AccountPayerUpdateEvent(accountId,customerName,customerAddress);
        return Collections.<Event>singletonList(accountPayerUpdateEvent);
    }

    private void createNewPayerCustomerName(CustomerName customer, CachableCustomer cachableCustomerPayer) {
        customer.setFirstName(cachableCustomerPayer.getFirstName());
        customer.setMiddleName(cachableCustomerPayer.getMiddleName());
        customer.setLastName(cachableCustomerPayer.getLastName());
        customer.setCustUnitNumber(cachableCustomerPayer.getCustUnitNumber());
        customer.setMasterCustomerId(cachableCustomerPayer.getMasterCustomerId());
    }

    private void createNewPayerCustomerAddress(CustomerAddress customerAddress, CachableCustomer cachableCustomerPayer) {
        customerAddress.setPostcodeIdMain(cachableCustomerPayer.getPostcodeIdMain());
        customerAddress.setPostcodeNameMain(cachableCustomerPayer.getPostcodeNameMain());
        customerAddress.setAddressCoName(cachableCustomerPayer.getAddressCOName());
        customerAddress.setAddressLineMain(cachableCustomerPayer.getAddressLineMain());
        customerAddress.setAddressStreetName(cachableCustomerPayer.getAddressStreetName());
        customerAddress.setAddressStreetNumber(cachableCustomerPayer.getAddressStreetNumber());
    }
}
