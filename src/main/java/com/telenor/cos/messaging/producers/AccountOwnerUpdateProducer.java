package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.account.AccountOwnerUpdateEvent;
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
 * Producer for {@link com.telenor.cos.messaging.event.account.AccountOwnerUpdateEvent}
 * @author Sanjin Cevro (Capgemini)
 *
 */
@Component
public class AccountOwnerUpdateProducer extends AbstractProducer {

    public static final Logger LOG = LoggerFactory.getLogger(AccountOwnerUpdateProducer.class);

    @Autowired
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    @Autowired
    private CustomerCache customerCache;

    @Override
    public Boolean isApplicable(Node message) {
        return accountUpdateXpathExtractor.getNewCustIdResp(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        CustomerName customerName = null;
        CustomerAddress customerAddress = null;
        Long accountId = XPathLong.getValue(accountUpdateXpathExtractor.getAccountId(message));
        String newCustIdOwner = XPathString.getValue(accountUpdateXpathExtractor.getNewCustIdResp(message));

        if(StringUtils.isNotBlank(newCustIdOwner)) {
            Long custIdOwner = Long.valueOf(newCustIdOwner);
            CachableCustomer cachableCustomerOwner = customerCache.get(custIdOwner);
            customerName = new CustomerName(custIdOwner);
            customerAddress = new CustomerAddress(custIdOwner);
            if(cachableCustomerOwner != null) {
                createNewOwnerCustomerName(customerName, cachableCustomerOwner);
                createNewOwnerCustomerAddress(customerAddress,cachableCustomerOwner);
            } else {
                LOG.error("Customer with Customer Owner Id [" + newCustIdOwner + "] was not found in CustomerCache! Hence Customer Owner Details will not be updated for Account with id [" + accountId + "]");
            }
        }
        AccountOwnerUpdateEvent accountOwnerUpdateEvent = new AccountOwnerUpdateEvent(accountId,customerName,customerAddress);
        return Collections.<Event>singletonList(accountOwnerUpdateEvent);
    }

    private void createNewOwnerCustomerName(CustomerName customer, CachableCustomer cachableCustomerOwner) {
        customer.setFirstName(cachableCustomerOwner.getFirstName());
        customer.setMiddleName(cachableCustomerOwner.getMiddleName());
        customer.setLastName(cachableCustomerOwner.getLastName());
        customer.setCustUnitNumber(cachableCustomerOwner.getCustUnitNumber());
        customer.setMasterCustomerId(cachableCustomerOwner.getMasterCustomerId());
    }

    private void createNewOwnerCustomerAddress(CustomerAddress customerAddress, CachableCustomer cachableCustomerOwner) {
        customerAddress.setPostcodeIdMain(cachableCustomerOwner.getPostcodeIdMain());
        customerAddress.setPostcodeNameMain(cachableCustomerOwner.getPostcodeNameMain());
        customerAddress.setAddressCoName(cachableCustomerOwner.getAddressCOName());
        customerAddress.setAddressLineMain(cachableCustomerOwner.getAddressLineMain());
        customerAddress.setAddressStreetName(cachableCustomerOwner.getAddressStreetName());
        customerAddress.setAddressStreetNumber(cachableCustomerOwner.getAddressStreetNumber());
    }
}
