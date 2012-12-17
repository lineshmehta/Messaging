package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.customer.CustomerNameChangeEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;

/**
 * Handler of {@link CustomerNameChangeEvent}
 *
 */
@Component
public class CustomerNameChangeHandler {

    @Autowired
    private CustomerCache customerCache;

    private static final Logger LOG = LoggerFactory.getLogger(CustomerNameChangeHandler.class);

    /**
     * updates the Name,MasterCustomerId and OrgNumber of a customer in jdbm3
     * @param customerNameChangeEvent the event.
     */
    public void handle(CustomerNameChangeEvent customerNameChangeEvent) {
        Long customerId = customerNameChangeEvent.getDomainId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [CustomerNameChangeEvent] with [customerId] as ["+ customerId +"]");
        }
        CustomerName customerName = customerNameChangeEvent.getCustomerName();
        if (customerName != null) {
            CachableCustomer oldCustomer = customerCache.get(customerName.getCustomerId());
            if (oldCustomer != null) {
                updateNameChange(customerName, oldCustomer);
            } else {
                LOG.error("Customer with [customerId] as [" + customerId + "] was not found in the Customer Cache. Customer Name will not be updated!");
            }
        } else {
            LOG.error("The received [CustomerNameChangeEvent] with [customerId] as ["+ customerId +"] has [customerName] as null! Customer Name will not be updated!");
        }
    }

    private void updateNameChange(CustomerName customer, CachableCustomer oldCustomer) {
        String firstName = customer.getFirstName();
        if (firstName != null) {
            oldCustomer.setFirstName(firstName);
        }

        String middleName = customer.getMiddleName();
        if (middleName != null) {
            oldCustomer.setMiddleName(middleName);
        }

        String lastName = customer.getLastName();
        if (lastName != null) {
            oldCustomer.setLastName(lastName);
        }

        Long masterCustomerId = customer.getMasterCustomerId();
        if(masterCustomerId != null) {
            oldCustomer.setMasterCustomerId(masterCustomerId);
        }

        Long custUnitNumber = customer.getCustUnitNumber();
        if(custUnitNumber != null) {
            oldCustomer.setCustUnitNumber(custUnitNumber);
        }
        customerCache.insert(oldCustomer.getCustomerId(), oldCustomer);
    }
}
