package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.customer.CustomerNewEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;

/**
 * Handler of {@link CustomerNewEvent}
 *
 */
@Component
public class CustomerNewHandler {

    @Autowired
    private CustomerCache customerCache;

    private static final Logger LOG = LoggerFactory.getLogger(CustomerNewHandler.class);

    /**
     * Inserts a new customer into the Jdbm3.
     * @param customerNewEvent a CustomerNewEvent.
     */
    public void handle(CustomerNewEvent customerNewEvent) {
        Long customerId = customerNewEvent.getDomainId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [CustomerNewEvent] with [customerId] as ["+ customerId +"]");
        }  
        CachableCustomer customer = toCachableCustomer(customerNewEvent.getCustomerAddress(), customerNewEvent.getCustomerName());
        customerCache.insert(customer.getCustomerId(), customer);
    }

    private CachableCustomer toCachableCustomer(CustomerAddress customerAddress, CustomerName customerName) {
        CachableCustomer cachableCustomer = new CachableCustomer(customerAddress.getCustomerId());
        cachableCustomer.setCustomerId(customerAddress.getCustomerId());
        
        //Setting the Customer Address.
        cachableCustomer.setAddressCOName(customerAddress.getAddressCoName());
        cachableCustomer.setAddressLineMain(customerAddress.getAddressLineMain());
        cachableCustomer.setAddressStreetName(customerAddress.getAddressStreetName());
        cachableCustomer.setAddressStreetNumber(customerAddress.getAddressStreetNumber());
        cachableCustomer.setPostcodeIdMain(customerAddress.getPostcodeIdMain());
        cachableCustomer.setPostcodeNameMain(customerAddress.getPostcodeNameMain());
        
        //Setting Name custUnitNumber and MasterId.
        cachableCustomer.setCustUnitNumber(customerName.getCustUnitNumber());
        cachableCustomer.setFirstName(customerName.getFirstName());
        cachableCustomer.setMiddleName(customerName.getMiddleName());
        cachableCustomer.setLastName(customerName.getLastName());
        cachableCustomer.setMasterCustomerId(customerName.getMasterCustomerId());
        
        return cachableCustomer;
    }
}
