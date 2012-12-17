package com.telenor.cos.messaging.event.subscription;

import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;

public class SubscriptionChangeUserEvent extends Event {

    private static final long serialVersionUID = -1700272807659235764L;

    private CustomerName customerName;

    /**
     * Constructor
     * 
     * @param subscriptionId the subscriptionId
     * @param newUserCustomer new userCustomer
     */
    public SubscriptionChangeUserEvent(Long subscriptionId, CustomerName newUserCustomer) {
        super(subscriptionId, ACTION.USER_CHANGE, TYPE.SUBSCRIPTION);
        this.customerName = newUserCustomer;
    }

    public CustomerName getCustomerName() {
        return customerName;
    }
}
