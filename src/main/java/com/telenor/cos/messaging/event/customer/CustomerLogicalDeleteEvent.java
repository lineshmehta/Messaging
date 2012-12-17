package com.telenor.cos.messaging.event.customer;

import com.telenor.cos.messaging.event.Event;

public class CustomerLogicalDeleteEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = 5540334448035381619L;

    /**
     * Constructor
     *
     * @param customerId the id
     */
    public CustomerLogicalDeleteEvent(Long customerId) {
        super(customerId, ACTION.LOGICAL_DELETE, TYPE.CUSTOMER);
    }
}
