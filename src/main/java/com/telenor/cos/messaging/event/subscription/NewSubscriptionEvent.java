package com.telenor.cos.messaging.event.subscription;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Subscription;

public class NewSubscriptionEvent extends Event {

    private Subscription data;

    private static final long serialVersionUID = -1L;

    /**
     * Constructor
     *
     * @param subscriptionId the id
     * @param data           the data
     */
    public NewSubscriptionEvent(Long subscriptionId, Subscription data) {
        super(subscriptionId, ACTION.CREATED, TYPE.SUBSCRIPTION);
        this.data = data;
    }

    public Subscription getData() {
        return data;
    }
}

