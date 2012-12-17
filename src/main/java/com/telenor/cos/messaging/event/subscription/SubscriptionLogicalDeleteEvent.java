package com.telenor.cos.messaging.event.subscription;

import com.telenor.cos.messaging.event.Event;

public class SubscriptionLogicalDeleteEvent extends Event {

    private static final long serialVersionUID = -1L;

    /**
     * Constructor
     * 
     * @param subscriptionId
     *            the id
     */
    public SubscriptionLogicalDeleteEvent(Long subscriptionId) {
        super(subscriptionId, ACTION.LOGICAL_DELETE, TYPE.SUBSCRIPTION);
    }

}
