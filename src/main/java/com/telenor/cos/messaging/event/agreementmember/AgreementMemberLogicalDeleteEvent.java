package com.telenor.cos.messaging.event.agreementmember;

import com.telenor.cos.messaging.event.AgreementMember;

public class AgreementMemberLogicalDeleteEvent extends AgreementMemberEvent {

    /**
     * 
     */
    private static final long serialVersionUID = -8076808847483312473L;

    /**
     * Constructor
     * 
     * @param agreementMember
     *            the agreementMember
     */
    public AgreementMemberLogicalDeleteEvent(AgreementMember agreementMember) {
        super(agreementMember, ACTION.LOGICAL_DELETE);
    }
}
