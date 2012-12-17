package com.telenor.cos.messaging.event.subscription;

import com.telenor.cos.messaging.event.Event;

public class SubscriptionChangeTypeEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -6204271742690500214L;

    private String subscriptionType;

    /**
     * Constructor
     * 
     * @param subscriptionId
     *            the subscriptionId
     * @param subscriptionType
     *            the subscriptionType
     */
    public SubscriptionChangeTypeEvent(Long subscriptionId, String subscriptionType) {
        super(subscriptionId, ACTION.TYPE_CHANGE, TYPE.SUBSCRIPTION);
        this.subscriptionType = subscriptionType;
    }

    public String getSubcriptionType() {
        return subscriptionType;
    }

}
