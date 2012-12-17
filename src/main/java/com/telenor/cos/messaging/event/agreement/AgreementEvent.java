package com.telenor.cos.messaging.event.agreement;

import com.telenor.cos.messaging.event.Agreement;
import com.telenor.cos.messaging.event.Event;

public abstract class AgreementEvent extends Event {

    /**
     *
     */
    private static final long serialVersionUID = 7528144107377476624L;
    private Agreement agreement;

    /**
     * Constructor
     * 
     * @param agreement the agreement
     * @param action the action
     */
    public AgreementEvent(Agreement agreement, ACTION action) {
        super(agreement.getAgreementId(), action, TYPE.AGREEMENT);
        this.agreement = agreement;
    }

    public Agreement getAgreement() {
        return agreement;
    }

}