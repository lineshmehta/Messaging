package com.telenor.cos.messaging.event.subscription;

import com.telenor.cos.messaging.event.Event;

public class SubscriptionSecretNumberEvent extends Event {

    private static final long serialVersionUID = -1L;
    private boolean isSecretNumber;

    /**
     * Constructor
     *
     * @param subscriptionId the subscription id
     * @param isSecretNumber the short number
     */
    public SubscriptionSecretNumberEvent(Long subscriptionId, boolean isSecretNumber) {
        super(subscriptionId, ACTION.SECRET_NUMBER, TYPE.SUBSCRIPTION);
        this.isSecretNumber = isSecretNumber;
    }

    /**
     * @return isSecretNumber
     */
    public boolean isSecretNumber() {
        return isSecretNumber;
    }
}