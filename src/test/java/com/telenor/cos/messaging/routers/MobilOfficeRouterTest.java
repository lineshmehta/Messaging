package com.telenor.cos.messaging.routers;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeDeleteEvent;
import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeNewEvent;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class MobilOfficeRouterTest extends RouterBaseTest{

    @EndpointInject(uri = "mock:" + EndPointUri.MOBILE_OFFICE_NEW_TOPIC)
    private MockEndpoint mobileOfficeNewEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.MOBILE_OFFICE_DELETE_TOPIC)
    private MockEndpoint mobileOfficeDeleteEndPoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;
    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;
    
    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/mobileOfficeRouter.xml");
    }
    
    @Test(timeout = 10000)
    public void testRouteMobileOfficeNewEvent() throws Exception {
        MobileOfficeNewEvent event = new MobileOfficeNewEvent("55668844","4578");
        template.sendBody(EndPointUri.MOBILE_OFFICE_INCOMING_QUEUE, event);
        assertReceivedMessage(mobileOfficeNewEndpoint, event, ACTION.CREATED);
    }
 
    @Test(timeout = 10000)
    public void testRouteMobileOfficeDeleteEvent() throws Exception {
        MobileOfficeDeleteEvent event = new MobileOfficeDeleteEvent("55668844","4578");
        template().sendBody(EndPointUri.MOBILE_OFFICE_INCOMING_QUEUE, event);
        assertReceivedMessage(mobileOfficeDeleteEndPoint, event, ACTION.DELETE);
    }
    
    @Test
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.MOBILE_OFFICE_INCOMING_QUEUE, "This is a test message");
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterMock.expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }
    
    private void assertReceivedMessage(MockEndpoint mockEndpoint, Event event, ACTION expectedAction) throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
        assertMessage(mockEndpoint, event, expectedAction);
    }

    private void assertMessage(MockEndpoint mockEndpoint,  Event event, ACTION expectedAction) {
        List <Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        Event actualMobileOfficeEvent = actualMessages.get(0).getIn().getBody(Event.class);
        assertEquals("Unexpected domain id", event.getDomainId(), actualMobileOfficeEvent.getDomainId());
        assertEquals("Unexpected action", expectedAction, actualMobileOfficeEvent.getAction());
    }
}
