package com.telenor.cos.messaging.event.customer;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;

public class CustomerNewEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    private CustomerAddress customerAddress;

    private CustomerName customerName;

    /**
     * @param customerAddress customerAddress.
     * @param customerName customerName.
     */
    public CustomerNewEvent(CustomerAddress customerAddress,CustomerName customerName) {
        super(customerAddress.getCustomerId(), ACTION.CREATED, TYPE.CUSTOMER);
        this.customerAddress = customerAddress;
        this.customerName = customerName;
    }

    public CustomerAddress getCustomerAddress() {
        return customerAddress;
    }

    public CustomerName getCustomerName() {
        return customerName;
    }
}
