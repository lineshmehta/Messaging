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
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Subscription;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangeTypeEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangeUserEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionExpiredEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionLogicalDeleteEvent;
import com.telenor.cos.test.category.ServiceTest;

/**
 *  Test class SubscriptionRouterCamel with embedded Camel and mocked topic end points.
 */
@Category(ServiceTest.class)
@DirtiesContext
public class SubscriptionRouterTest extends RouterBaseTest {
       
    private static final Long SUBSCRIPTION_ID = Long.valueOf("666");
    
    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_NEW_TOPIC)
    private MockEndpoint newSubscriptionEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_EXPIRED_TOPIC)
    private MockEndpoint expiredSubscriptionEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_LOGICAL_DELETE_TOPIC)
    private MockEndpoint logicalDeletedSubscriptionEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;
    
    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_CHANGE_USER_TOPIC)
    private MockEndpoint changeUserSubscription;
    
    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_CHANGE_TYPE_TOPIC)
    private MockEndpoint changeTypeSubscription;
    
    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/subscriptionRouter.xml");
    }
    
    @Test(timeout = 20000)
    public void testRouteNewSubscriptionEvent() throws Exception {
        Event subscriptionEvent = new NewSubscriptionEvent(SUBSCRIPTION_ID, new Subscription());
        
        template.sendBody(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE, subscriptionEvent);
        assertReceivedMessage(newSubscriptionEndpoint, subscriptionEvent, ACTION.CREATED);
    }
   
    @Test(timeout = 20000)
    public void testRouteExpiredSubscriptionEvent() throws Exception {
        Event subscriptionEvent = new SubscriptionExpiredEvent(SUBSCRIPTION_ID, null);
        
        template.sendBody(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE, subscriptionEvent);
        assertReceivedMessage(expiredSubscriptionEndpoint, subscriptionEvent, ACTION.EXPIRED);
    }
    
    @Test(timeout = 20000)
    public void testRouteLogicalDeletedSubscriptionEvent() throws Exception{
        Event subscriptionEvent = new SubscriptionLogicalDeleteEvent(SUBSCRIPTION_ID);
        
        template.sendBody(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE, subscriptionEvent);
        assertReceivedMessage(logicalDeletedSubscriptionEndpoint, subscriptionEvent, ACTION.LOGICAL_DELETE);
    }
    
    @Test(timeout = 20000)
    public void testRouteChangeUserSubscriptionEvent() throws Exception{
        CustomerName customer= new CustomerName(2L);
        Event subscriptionEvent = new SubscriptionChangeUserEvent(SUBSCRIPTION_ID, customer);
        
        template.sendBody(EndPointUri.SUBSCRIPTION_CHANGE_USER_TOPIC, subscriptionEvent);
        assertReceivedMessage(changeUserSubscription, subscriptionEvent, ACTION.USER_CHANGE);
    }
    
    @Test(timeout = 20000)
    public void testRouteChangeTypeSubscriptionEvent() throws Exception{
        Event subscriptionEvent = new SubscriptionChangeTypeEvent(SUBSCRIPTION_ID, "trololol");
        
        template.sendBody(EndPointUri.SUBSCRIPTION_CHANGE_TYPE_TOPIC, subscriptionEvent);
        assertReceivedMessage(changeTypeSubscription, subscriptionEvent, ACTION.TYPE_CHANGE);
    }
    
    @Test
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE, "This is a test message");
        invalidMessageQueueMockEndPoint.setAssertPeriod(1000); //wait to make sure no more than the expected messages are received
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterMock.expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }
    
    private void assertReceivedMessage(MockEndpoint mockEndpoint, Event subscriptionEvent, ACTION expectedAction) throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
        assertMessage(mockEndpoint, subscriptionEvent, expectedAction);
    }

    private void assertMessage(MockEndpoint mockEndpoint,  Event subscriptionEvent, ACTION expectedAction) {
        List <Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        Event actualSubscriptionEvent = actualMessages.get(0).getIn().getBody(Event.class);
        assertEquals("Unexpected subscription id", subscriptionEvent.getDomainId(), actualSubscriptionEvent.getDomainId());
        assertEquals("Unexpected action", expectedAction, actualSubscriptionEvent.getAction());
    }
}
