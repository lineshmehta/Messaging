package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.AgreementOwner;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.agreementowner.AgreementOwnerNewEvent;
import com.telenor.cos.test.category.IntegrationTest;

@Category(IntegrationTest.class)
public class AgreementOwnerRouterIntegrationTest extends CommonIntegrationTest {

    private static final Long AGREEMENT_ID = Long.valueOf(531314);
    private static final Long MASTER_ID = Long.valueOf(100318909);

    private static final String NEW_AGR_OWNER = "dataset/agreement_owner_new.xml";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.AGREEMENT_OWNER_NEW_TOPIC);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test(timeout = 10000)
    public void newAgreementOwnerIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(NEW_AGR_OWNER);
        AgreementOwnerNewEvent consumedAgreementMemberNewEvent = (AgreementOwnerNewEvent) getJms().receive(ExternalEndpoints.AGREEMENT_OWNER_NEW_TOPIC, correlationId);
        assertAction(consumedAgreementMemberNewEvent,ACTION.CREATED);
        AgreementOwner agreementMember = consumedAgreementMemberNewEvent.getAgreementOwner();
        assertEquals("Unexpected agreement id", AGREEMENT_ID, agreementMember.getAgreementId());
        assertEquals("Unexpected master id", MASTER_ID, agreementMember.getMasterId());
    }
}