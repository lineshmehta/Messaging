package com.telenor.cos.messaging.event.customer;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.Event;

public class CustomerAddressChangeEvent extends Event {

    private static final long serialVersionUID = -4556543881848824731L;

    private CustomerAddress customerAdress;

    /**
     * constructor
     *
     * @param customerId     the customerId
     * @param customerAdress the customerAdress
     */
    public CustomerAddressChangeEvent(Long customerId, CustomerAddress customerAdress) {
        super(customerId, ACTION.ADRESS_CHANGE, TYPE.CUSTOMER);
        this.customerAdress = customerAdress;
    }

    public CustomerAddress getCustomerAdress() {
        return customerAdress;
    }
}
