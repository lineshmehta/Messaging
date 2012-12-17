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
import com.telenor.cos.messaging.event.TnuIdUserMapping;
import com.telenor.cos.messaging.event.usermapping.TnuIdUserMappingNewEvent;
import com.telenor.cos.messaging.event.usermapping.TnuidUserMappingDeleteEvent;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class TnuIdUserMappingNewRouterTest extends RouterBaseTest {
    
    private static final String TELENOR_USER_ID = "808074";

    private static final Integer APPLICATION_ID = 62;

    private static final String COS_SECURITY_USER_ID = "cos_test_large_1";

    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;

    @EndpointInject(uri = "mock:" + EndPointUri.TNUIDUSERMAPPING_NEW_TOPIC)
    private MockEndpoint tnuIdUserMappingNewEndPoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.TNUIDUSERMAPPING_DELETE_TOPIC)
    private MockEndpoint tnuIdUserMappingDeleteEndPoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/tnuidUserMappingRouter.xml");
    }

    /**
     * Tests UserResource New Event Router. 
     * @throws Exception
     */
    @Test(timeout = 10000)
    public void testRouteTnuIdUserMappingNewEvent() throws Exception {
        Event event = new TnuIdUserMappingNewEvent(createUserMapping());
        template.sendBody(EndPointUri.TNUIDUSERMAPPING_INCOMING_QUEUE, event);
        assertReceivedMessage(tnuIdUserMappingNewEndPoint);
        assertNewUserResourceMessage(tnuIdUserMappingNewEndPoint, event, ACTION.CREATED);
    }
    
    /**
     * Tests UserResource New Event Router. 
     * @throws Exception
     */
    @Test(timeout = 10000)
    public void testRouteTnuIdUserMappingDeleteEvent() throws Exception {
        Event event = new TnuidUserMappingDeleteEvent(TELENOR_USER_ID, APPLICATION_ID);
        template.sendBody(EndPointUri.TNUIDUSERMAPPING_INCOMING_QUEUE, event);
        assertReceivedMessage(tnuIdUserMappingDeleteEndPoint);
        List <Exchange> actualMessages = tnuIdUserMappingDeleteEndPoint.getReceivedExchanges();
        TnuidUserMappingDeleteEvent actualUserMappingEvent = actualMessages.get(0).getIn().getBody(TnuidUserMappingDeleteEvent.class);    
        assertEquals(TELENOR_USER_ID, actualUserMappingEvent.getTelenorUserId());
        assertEquals(APPLICATION_ID, actualUserMappingEvent.getApplicationId());
        assertEquals(ACTION.DELETE, actualUserMappingEvent.getAction());
    }
        

    @Test
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.TNUIDUSERMAPPING_INCOMING_QUEUE, "This is a test message");
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
        TnuIdUserMappingNewEvent actualUserMappingEvent = actualMessages.get(0).getIn().getBody(TnuIdUserMappingNewEvent.class);
        assertEquals("Unexpected telenor user id", TELENOR_USER_ID, actualUserMappingEvent.getUserMapping().getTelenorUserId());
        assertEquals("Unexpected application id", APPLICATION_ID, actualUserMappingEvent.getUserMapping().getApplicationId());
        assertEquals("Unexpected cos security user id", COS_SECURITY_USER_ID, actualUserMappingEvent.getUserMapping().getCosSecurityUserId());
        assertEquals("Unexpected action", expectedAction, actualUserMappingEvent.getAction());
    }

    private TnuIdUserMapping createUserMapping() {
        TnuIdUserMapping userMapping = new TnuIdUserMapping();
        userMapping.setTelenorUserId(TELENOR_USER_ID);
        userMapping.setApplicationId(APPLICATION_ID);
        userMapping.setCosSecurityUserId(COS_SECURITY_USER_ID);
        return userMapping;
    }
}
