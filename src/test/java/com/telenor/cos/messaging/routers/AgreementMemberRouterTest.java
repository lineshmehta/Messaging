package com.telenor.cos.messaging.routers;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.AgreementMember;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.agreementmember.AgreementMemberNewEvent;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class AgreementMemberRouterTest extends StandardRouterBaseTest {
    
    private static final Long AGREEMENT_MEMBER_ID = Long.valueOf(1);
    
    @EndpointInject(uri = "mock:" + EndPointUri.AGREEMENT_MEMBER_NEW_TOPIC)
    private MockEndpoint mockAgreementMemberNewEndpoint;
    

    @Test(timeout = 10000)
    public void testRouteAgreementMemberNewEvent() throws Exception {
        AgreementMember agreementMember = new AgreementMember();
        agreementMember.setAgreementId(AGREEMENT_MEMBER_ID);
        Event event = new AgreementMemberNewEvent(agreementMember);
        
        template.sendBody(getIncomingQueue(), event);
        assertReceivedMessage(mockAgreementMemberNewEndpoint, event, ACTION.CREATED);
    }

    @Override
    protected String getIncomingQueue() {
        return EndPointUri.AGREEMENT_MEMBER_INCOMING_QUEUE;
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/agreementMemberRouter.xml");
    }

}
