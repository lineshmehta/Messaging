package com.telenor.cos.messaging.event.customer;

import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;

public class CustomerNameChangeEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = 1163458004190750293L;

    private CustomerName customerName;

    /**
     * @param customerId custId.
     * @param customerName custName.
     */
    public CustomerNameChangeEvent(Long customerId, CustomerName customerName) {
        super(customerId, ACTION.NAME_CHANGE, TYPE.CUSTOMER);
        this.customerName = customerName;
    }

    public CustomerName getCustomerName() {
        return customerName;
    }
}
