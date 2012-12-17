package com.telenor.cos.messaging.event.agreementmember;

import com.telenor.cos.messaging.event.AgreementMember;
import com.telenor.cos.messaging.event.Event;

public abstract class AgreementMemberEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = 3912244631154410015L;

    private AgreementMember agreementMember;

    /**
     * Constructor
     * 
     * @param agreementMember
     *            the agreementMember
     * @param action
     *            The action
     */
    public AgreementMemberEvent(AgreementMember agreementMember, ACTION action) {
        super(agreementMember.getAgreementMemberId(), action, TYPE.AGREEMENT_MEMBER);
        this.agreementMember = agreementMember;
    }

    public AgreementMember getAgreementMember() {
        return agreementMember;
    }

}
