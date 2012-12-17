package com.telenor.cos.messaging.event;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.agreementmember.AgreementMemberNewEvent;

public class AgreementMemberEventTest extends AbstractEventTest{

    private final static Long AGREEMENT_MEMBER_ID = Long.valueOf(1);
    
    @Test
    public void testAgreementMemberNew() {
        AgreementMember agreementMember = new AgreementMember();
        agreementMember.setAgreementMemberId(AGREEMENT_MEMBER_ID);
        AgreementMemberNewEvent agreementMemberNewEvent = new AgreementMemberNewEvent(agreementMember);
        
        assertActionAndType(agreementMemberNewEvent, ACTION.CREATED, TYPE.AGREEMENT_MEMBER);
        assertEquals("Unexpected domain id", AGREEMENT_MEMBER_ID, agreementMemberNewEvent.getDomainId());
    }

}
