package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.AgreementMember;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.agreementmember.AgreementMemberLogicalDeleteEvent;
import com.telenor.cos.messaging.event.agreementmember.AgreementMemberNewEvent;
import com.telenor.cos.test.category.IntegrationTest;

/**
 * IntegrationTest case for AgreementMemberEvents.
 *
 */
@Category(IntegrationTest.class)
public class AgreementMemberRouterIntegrationTest extends CommonIntegrationTest {

    private static final Long AGREEMENT_MEMBER_ID = Long.valueOf(666);
    private static final Long AGREEMENT_ID = Long.valueOf(1);
    private static final Long MASTER_ID = Long.valueOf(555);

    private static final String NEW_AGR_MEMBER_XML = "dataset/agreementMember_new.xml";
    private static final String AGR_MEMBER_LOGICAL_DELETE_XML = "dataset/agreementMember_logical_delete.xml";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.AGREEMENT_MEMBER_NEW_TOPIC);
        getJms().receive(ExternalEndpoints.AGREEMENT_MEMBER_LOGICAL_DELETE_TOPIC);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test(timeout = 10000)
    public void newAgreementMemberIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(NEW_AGR_MEMBER_XML);
        AgreementMemberNewEvent consumedAgreementMemberNewEvent = (AgreementMemberNewEvent)getJms().receive(ExternalEndpoints.AGREEMENT_MEMBER_NEW_TOPIC, correlationId);
        assertAgreementMemberIdAndAction(consumedAgreementMemberNewEvent,ACTION.CREATED);
        assertAgreementMember(consumedAgreementMemberNewEvent.getAgreementMember());
    }

    @Test(timeout = 10000)
    public void logicalDeleteAgreementMemberIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(AGR_MEMBER_LOGICAL_DELETE_XML);
        AgreementMemberLogicalDeleteEvent consumedAgreementMemberLogicalDeleteEvent = (AgreementMemberLogicalDeleteEvent)getJms().receive(ExternalEndpoints.AGREEMENT_MEMBER_LOGICAL_DELETE_TOPIC, correlationId);
        assertAgreementMemberIdAndAction(consumedAgreementMemberLogicalDeleteEvent,ACTION.LOGICAL_DELETE);
        assertAgreementMember(consumedAgreementMemberLogicalDeleteEvent.getAgreementMember());
    }

    private void assertAgreementMemberIdAndAction(Event event,ACTION action) {
        assertAction(event,action);
        assertEquals("Unexpected agreement member id",AGREEMENT_MEMBER_ID,event.getDomainId());
    }
    private void assertAgreementMember(AgreementMember agreementMember) {
        assertEquals("Unexpected agreement id", AGREEMENT_ID, agreementMember.getAgreementId());
        assertEquals("Unexpected master id", MASTER_ID, agreementMember.getMasterId());
    }
}
