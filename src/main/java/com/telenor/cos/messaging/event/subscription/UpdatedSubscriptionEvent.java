package com.telenor.cos.messaging.event.subscription;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Subscription;

/**
 * We probably sold something
 */

public class UpdatedSubscriptionEvent extends Event {

    private Subscription data;

    private static final long serialVersionUID = -1L;

    /**
     * Constructor
     *
     * @param subscriptionId the id
     * @param data           the new data
     */
    public UpdatedSubscriptionEvent(Long subscriptionId, Subscription data) {
        super(subscriptionId, ACTION.UPDATED, TYPE.SUBSCRIPTION);
        this.data = data;
    }

    public Subscription getData() {
        return data;
    }

}

