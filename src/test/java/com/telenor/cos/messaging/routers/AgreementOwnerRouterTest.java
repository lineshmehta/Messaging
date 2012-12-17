package com.telenor.cos.messaging.routers;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractApplicationContext;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.AgreementOwner;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.agreementowner.AgreementOwnerNewEvent;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
public class AgreementOwnerRouterTest extends StandardRouterBaseTest {

    private static final Long AGREEMENT_ID = Long.valueOf(1);
    private static final Long MASTER_ID = Long.valueOf(1);
    
    @EndpointInject(uri = "mock:" + EndPointUri.AGREEMENT_OWNER_NEW_TOPIC)
    private MockEndpoint mockAgreementOwnerNewEndpoint;
    
    @Test
    public void testRouterAgreementOwnerNewEvent() throws Exception{
        AgreementOwner agreementOwner = new AgreementOwner();
        agreementOwner.setAgreementId(AGREEMENT_ID);
        agreementOwner.setMasterId(MASTER_ID);
        Event event = new AgreementOwnerNewEvent(agreementOwner);
        
        template.sendBody(getIncomingQueue(), event);
        assertReceivedMessage(mockAgreementOwnerNewEndpoint, event, ACTION.CREATED);
    }

    @Override
    protected String getIncomingQueue() {
        return EndPointUri.AGREEMENT_OWNER_INCOMING_QUEUE;
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/agreementOwnerRouter.xml");
    }
}