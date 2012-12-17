package com.telenor.cos.messaging.event.agreementowner;

import com.telenor.cos.messaging.event.AgreementOwner;
import com.telenor.cos.messaging.event.Event;

public abstract class AgreementOwnerEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = 3471776422565240616L;

    private AgreementOwner agreementOwner;

    /**
     * Constructor
     * 
     * @param agreementOwner
     *            the agreementOwner
     * @param action
     *            the action
     */
    public AgreementOwnerEvent(AgreementOwner agreementOwner, ACTION action) {
        super(null, action, TYPE.AGREEMENT_OWNER);
        this.agreementOwner = agreementOwner;
    }

    public AgreementOwner getAgreementOwner() {
        return agreementOwner;
    }

}
