package com.telenor.cos.messaging.event.agreementmember;

import com.telenor.cos.messaging.event.AgreementMember;

public class AgreementMemberNewEvent extends AgreementMemberEvent {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param agreementMember
     *            the agreement member
     */
    public AgreementMemberNewEvent(AgreementMember agreementMember) {
        super(agreementMember, ACTION.CREATED);
    }

}
