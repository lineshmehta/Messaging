package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.customer.CustomerAddressChangeEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;

/**
 * Handler of {@link CustomerAddressChangeEvent}
 *
 */
@Component
public class CustomerAddressChangeHandler {

    @Autowired
    private CustomerCache customerCache;

    private static final Logger LOG = LoggerFactory.getLogger(CustomerAddressChangeHandler.class);

    /**
     * Updates the address of a customer in jbdm3
     * @param customerAddressChangeEvent a CustomerAdressChangeEvent.
     */
    public void handle(CustomerAddressChangeEvent customerAddressChangeEvent) {
        Long customerId = customerAddressChangeEvent.getDomainId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [CustomerAddressChangeEvent] with [customerId] as ["+ customerId +"]");
        }
        CustomerAddress customerAdress = customerAddressChangeEvent.getCustomerAdress();
        if (customerAdress != null) {
            CachableCustomer oldCustomer = customerCache.get(customerAdress.getCustomerId());
            if (oldCustomer != null) {
                updateCustomerAdress(customerAdress, oldCustomer);
            } else {
                LOG.error("Customer with [customerId] as [" + customerId + "] was not found in the Customer Cache. Address will not be updated!");
            }
        } else {
            LOG.error("The received [CustomerAddressChangeEvent] with [customerId] as ["+ customerId +"] has [customerAdress] as null! Address will not be updated!");
        }
    }

    private void updateCustomerAdress(CustomerAddress customerAdress, CachableCustomer oldCustomer) {
        String postcodeIdMain = customerAdress.getPostcodeIdMain();
        if (postcodeIdMain != null) {
            oldCustomer.setPostcodeIdMain(postcodeIdMain);
        }
        String postcodeNameMain = customerAdress.getPostcodeNameMain();
        if (postcodeNameMain != null) {
            oldCustomer.setPostcodeNameMain(postcodeNameMain);
        }
        String addressLineMain = customerAdress.getAddressLineMain();
        if (addressLineMain != null) {
            oldCustomer.setAddressLineMain(addressLineMain);
        }
        String addressCOName = customerAdress.getAddressCoName();
        if (addressCOName != null) {
            oldCustomer.setAddressCOName(addressCOName);
        }
        String addressStreetName = customerAdress.getAddressStreetName();
        if (addressStreetName != null) {
            oldCustomer.setAddressStreetName(addressStreetName);
        }
        String addressStreetNumber = customerAdress.getAddressStreetNumber();
        if (addressStreetNumber != null) {
            oldCustomer.setAddressStreetNumber(addressStreetNumber);
        }
        customerCache.insert(oldCustomer.getCustomerId(), oldCustomer);
    }
}
