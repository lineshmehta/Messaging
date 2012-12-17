package com.telenor.cos.messaging.routers;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Agreement;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.agreement.AgreementLogicalDeletedEvent;
import com.telenor.cos.messaging.event.agreement.AgreementNewEvent;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class AgreementRouterTest extends StandardRouterBaseTest {

    @EndpointInject(uri = "mock:" + EndPointUri.AGREEMENT_NEW_TOPIC)
    private MockEndpoint agreementNewEndpoint;

    @EndpointInject(uri = "mock:" + EndPointUri.AGREEMENT_LOGICAL_DELETE_TOPIC)
    private MockEndpoint agreementLogicalDeleteEndpoint;
    
    private static final Long AGREEMENT_ID = Long.valueOf(22);

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/agreementRouter.xml");
    }
    
    @Test(timeout = 10000)
    public void testRouteAgreementNewEvent() throws Exception {
        Event event = new AgreementNewEvent(createAgreement());
        template.sendBody(getIncomingQueue(), event);
        assertReceivedMessage(agreementNewEndpoint, event, ACTION.CREATED);
    }
    
    @Test(timeout = 10000)
    public void testRouteLogicalDeleteEvent() throws Exception {
        Event event = new AgreementLogicalDeletedEvent(createAgreement());
        context.getShutdownStrategy().setTimeout(300);
        template.sendBody(getIncomingQueue(), event);
        assertReceivedMessage(agreementLogicalDeleteEndpoint, event, ACTION.LOGICAL_DELETE);
    }

    private Agreement createAgreement() {
        Agreement agreement = new Agreement();
        agreement.setAgreementId(AGREEMENT_ID);
        return agreement;
    }
    
    @Override
    protected String getIncomingQueue() {
        return EndPointUri.AGREEMENT_INCOMING_QUEUE;
    }

}
