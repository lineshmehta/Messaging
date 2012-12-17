package com.telenor.cos.messaging.event.subscription;

import com.telenor.cos.messaging.event.Event;

public class SubscriptionChangedStatusEvent extends Event {

    private static final long serialVersionUID = -1L;

    private final String status;
    /**
     * Constructor
     *
     * @param subscriptionId the subscription id
     * @param status the subscription status
     */
    public SubscriptionChangedStatusEvent(Long subscriptionId, String status) {
        super(subscriptionId, ACTION.STATUS_CHANGE, TYPE.SUBSCRIPTION);
        this.status = status;
    }

    /**
     * @return the subscription status
     */
    public String getStatus() {
        return status;
    }
}
