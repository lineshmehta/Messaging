package com.telenor.cos.messaging.routers;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.userresource.UserResourceCsUserIdUpdateEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceDeleteEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceNewEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceResourceIdUpdateEvent;
import com.telenor.cos.messaging.util.UserResourceTestHelper;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class UserResourceRouterTest extends RouterBaseTest {
    
    @EndpointInject(uri = "mock:" + EndPointUri.USERRESOURCE_NEW_TOPIC)
    private MockEndpoint userResourceNewEndPoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.USERRESOURCE_DELETE_TOPIC)
    private MockEndpoint userResourceDeleteEndPoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.USERRESOURCE_RESOURCE_ID_UPDATE_TOPIC)
    private MockEndpoint userResourceIdUpdateEndPoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.USERRESOURCE_CSUSER_ID_UPDATE_TOPIC)
    private MockEndpoint userResourceCsUserIdEndPoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;
    
    private static final Long RESOURCE_ID_NEW = Long.valueOf(123);
    private static final Long RESOURCE_ID_OLD = Long.valueOf(124);
    private static final String CS_USER_ID = "cosmaster";
    private UserResourceTestHelper userResourceTestHelper = new UserResourceTestHelper();
    
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/userResourceRouter.xml");
    }
    
    /**
     * Tests UserResource New Event Router. 
     * @throws Exception
     */
    @Test(timeout = 10000)
    public void testRouteUserResourceNewEvent() throws Exception {
        Event event = new UserResourceNewEvent(userResourceTestHelper.createUserResource(RESOURCE_ID_NEW,CS_USER_ID));
        template.sendBody(EndPointUri.USERRESOURCE_INCOMING_QUEUE, event);
        assertReceivedMessage(userResourceNewEndPoint);
        assertNewUserResourceMessage(userResourceNewEndPoint, event, ACTION.CREATED);
    }
    
    /**
     * Tests UserResource Delete Event Router.
     * @throws Exception
     */
    @Test(timeout = 10000)
    public void testRouteUserResourceDeleteEvent() throws Exception {
        Event event = new UserResourceDeleteEvent(userResourceTestHelper.createUserResource(RESOURCE_ID_NEW,CS_USER_ID));
        template.sendBody(EndPointUri.USERRESOURCE_INCOMING_QUEUE, event);
        assertReceivedMessage(userResourceDeleteEndPoint);
        assertDeleteUserResourceMessage(userResourceDeleteEndPoint, event, ACTION.DELETE);
    }
    
    @Test
    public void testRouteUserResourceIdUpdateEvent() throws Exception {
        Event event = new UserResourceResourceIdUpdateEvent(userResourceTestHelper.createUserResource(RESOURCE_ID_NEW,CS_USER_ID), userResourceTestHelper.createUserResource(RESOURCE_ID_OLD,CS_USER_ID));
        template.sendBody(EndPointUri.USERRESOURCE_INCOMING_QUEUE, event);
        assertReceivedMessage(userResourceIdUpdateEndPoint);
        List <Exchange> actualMessages = userResourceIdUpdateEndPoint.getReceivedExchanges();
        UserResourceResourceIdUpdateEvent actualUserResourceEvent = actualMessages.get(0).getIn().getBody(UserResourceResourceIdUpdateEvent.class);
        assertEquals("Unexpected action", ACTION.RESOURCE_ID_CHANGE, actualUserResourceEvent.getAction());
        assertEquals("Unexpected Resource Id", RESOURCE_ID_NEW, actualUserResourceEvent.getNewUserResource().getResource().getResourceId());
    }
    
    @Test
    public void testRouteUserResourceCsUserIdUpdateEvent() throws Exception {
        Event event = new UserResourceCsUserIdUpdateEvent(userResourceTestHelper.createUserResource(RESOURCE_ID_NEW,CS_USER_ID),CS_USER_ID);
        template.sendBody(EndPointUri.USERRESOURCE_INCOMING_QUEUE, event);
        assertReceivedMessage(userResourceCsUserIdEndPoint);
        List <Exchange> actualMessages = userResourceCsUserIdEndPoint.getReceivedExchanges();
        UserResourceCsUserIdUpdateEvent actualUserResourceEvent = actualMessages.get(0).getIn().getBody(UserResourceCsUserIdUpdateEvent.class);
        assertEquals("Unexpected action", ACTION.CS_USERID_CHANGE, actualUserResourceEvent.getAction());
        assertEquals("Unexpected Resource Id", RESOURCE_ID_NEW, actualUserResourceEvent.getDomainId());
    }
    
    @Test
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.USERRESOURCE_INCOMING_QUEUE, "This is a test message");
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterMock.expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }
    
    private void assertReceivedMessage(MockEndpoint mockEndpoint) throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
    }

    private void assertNewUserResourceMessage(MockEndpoint mockEndpoint,  Event event, ACTION expectedAction) {
        List <Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        UserResourceNewEvent actualUserResourceEvent = actualMessages.get(0).getIn().getBody(UserResourceNewEvent.class);
        assertEquals("Unexpected action", expectedAction, actualUserResourceEvent.getAction());
    }
    
    private void assertDeleteUserResourceMessage(MockEndpoint mockEndpoint,  Event event, ACTION expectedAction) {
        List <Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        UserResourceDeleteEvent actualUserResourceEvent = actualMessages.get(0).getIn().getBody(UserResourceDeleteEvent.class);
        assertEquals("Unexpected action", expectedAction, actualUserResourceEvent.getAction());
    }
}
