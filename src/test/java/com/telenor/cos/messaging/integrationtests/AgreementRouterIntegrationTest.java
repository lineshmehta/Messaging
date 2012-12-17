package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.agreement.AgreementEvent;
import com.telenor.cos.messaging.event.agreement.AgreementLogicalDeletedEvent;
import com.telenor.cos.messaging.event.agreement.AgreementNewEvent;
import com.telenor.cos.test.category.IntegrationTest;

@Category(IntegrationTest.class)
public class AgreementRouterIntegrationTest extends CommonIntegrationTest {

    private static final Long AGREEMENT_ID = Long.valueOf(4274399);
    private static final Long MASTER_ID = Long.valueOf(369);

    private static final String NEW_MASTER_CUSTOMER = "dataset/mastercustomer_new.xml";
    private static final String NEW_AGREEMENT = "dataset/agreement_new.xml";
    private static final String AGREEMENT_LOGICAL_DELETE = "dataset/agreement_logicaldelete.xml";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.AGREEMENT_NEW);
        getJms().receive(ExternalEndpoints.AGREEMENT_LOGICAL_DELETE);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test(timeout = 10000)
    public void newAgreementIntegrationTest() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(NEW_MASTER_CUSTOMER);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(NEW_AGREEMENT);
        AgreementNewEvent consumedAgreementNewEvent = (AgreementNewEvent) getJms().receive(ExternalEndpoints.AGREEMENT_NEW, correlationId);
        assertEvent(consumedAgreementNewEvent, ACTION.CREATED);
        assertEquals("Unexpected Master id", MASTER_ID, consumedAgreementNewEvent.getAgreement().getMasterCustomerId());
    }

    @Test(timeout = 10000)
    public void agreementLogicalDeleteIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(AGREEMENT_LOGICAL_DELETE);
        AgreementLogicalDeletedEvent consumedAgreementLogicalDeletedEvent = (AgreementLogicalDeletedEvent) getJms().receive(ExternalEndpoints.AGREEMENT_LOGICAL_DELETE, correlationId);
        assertEvent(consumedAgreementLogicalDeletedEvent, ACTION.LOGICAL_DELETE);
    }

    private void assertEvent(AgreementEvent event, ACTION action) {
        assertAction(event,action);
        assertEquals("Unexpected Agreement id", AGREEMENT_ID, event.getAgreement().getAgreementId());
    }
}
