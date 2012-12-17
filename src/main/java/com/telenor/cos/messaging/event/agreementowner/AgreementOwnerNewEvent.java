package com.telenor.cos.messaging.event.agreementowner;

import com.telenor.cos.messaging.event.AgreementOwner;

public class AgreementOwnerNewEvent extends AgreementOwnerEvent {

    /**
     * 
     */
    private static final long serialVersionUID = -7026584133234837868L;

    /**
     * Constructor
     * 
     * @param agreementOwner
     *            the agreementOwner
     */
    public AgreementOwnerNewEvent(AgreementOwner agreementOwner) {
        super(agreementOwner, ACTION.CREATED);
    }

}
