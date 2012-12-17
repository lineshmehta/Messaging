package com.telenor.cos.messaging.event.subscription;

import com.telenor.cos.messaging.event.Event;

/**
 * Special case of update subscription where the parent account of subscription is updated
 * 
 * @author Eirik Bergande, (Capgemini)
 * 
 */
public class SubscriptionChangedAccountEvent extends Event {

    private Long accountId;

    private static final long serialVersionUID = -1L;

    /**
     * Constructor
     *
     * @param subscriptionId
     *            the subscriptionId
     * @param accoundId
     *            the accountId
     */
    public SubscriptionChangedAccountEvent(Long subscriptionId, Long accoundId) {
        super(subscriptionId, ACTION.CHANGE_ACCOUNT, TYPE.SUBSCRIPTION);
        this.accountId = accoundId;
    }

    public Long getAccountId() {
        return accountId;
    }
}
