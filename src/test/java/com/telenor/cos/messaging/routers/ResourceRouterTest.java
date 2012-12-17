package com.telenor.cos.messaging.routers;

import java.util.ArrayList;
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
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceContentInheritUpdateEvent;
import com.telenor.cos.messaging.event.resource.ResourceLogicalDeleteEvent;
import com.telenor.cos.messaging.event.resource.ResourceNewEvent;
import com.telenor.cos.messaging.event.resource.ResourceStructureInheritUpdateEvent;
import com.telenor.cos.messaging.event.resource.ResourceTypeIdKeyUpdateEvent;
import com.telenor.cos.messaging.event.resource.ResourceTypeIdUpdateEvent;
import com.telenor.cos.test.category.ServiceTest;

/**
 * Test case for {@link ResourceRouter}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
@DirtiesContext
public class ResourceRouterTest extends RouterBaseTest {

    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;

    @EndpointInject(uri = "mock:" + EndPointUri.RESOURCE_NEW_TOPIC)
    private MockEndpoint resourceNewEndpoint;

    @EndpointInject(uri = "mock:" + EndPointUri.RESOURCE_LOGICAL_DELETE_TOPIC)
    private MockEndpoint resourceLogicalDeleteEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.RESOURCE_TYPE_ID_UPDATE_TOPIC)
    private MockEndpoint resourceTypeIdUpdateEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.RESOURCE_TYPE_ID_KEY_UPDATE_TOPIC)
    private MockEndpoint resourceTypeIdKeyUpdateEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC)
    private MockEndpoint resourceContentInheritUpdateEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.RESOURCE_STRUCTURE_INHERIT_UPDATE_TOPIC)
    private MockEndpoint resourceStructureInheritUpdateEndpoint;

    public static final Long RESOURCE_ID = Long.valueOf(1234);
    private Resource resource  = new Resource(RESOURCE_ID);
    private static final List<String> csUserIdsList = new ArrayList<String>();

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/resourceRouter.xml");
    }

    @Test(timeout = 10000)
    public void testRouteResourceNewEvent() throws Exception {
        Resource resource = new Resource(RESOURCE_ID);
        ResourceNewEvent resourceNewEvent = new ResourceNewEvent(resource);
        template.sendBody(EndPointUri.RESOURCE_INCOMING_QUEUE, resourceNewEvent);
        assertReceivedMessage(resourceNewEndpoint, resourceNewEvent, ACTION.CREATED);
    }

    @Test(timeout = 10000)
    public void testRouteResourceLogicalDeleteEvent() throws Exception {
        ResourceLogicalDeleteEvent resourceLogicalDeleteEvent = new ResourceLogicalDeleteEvent(RESOURCE_ID);
        template.sendBody(EndPointUri.RESOURCE_INCOMING_QUEUE, resourceLogicalDeleteEvent);
        assertReceivedMessage(resourceLogicalDeleteEndpoint, resourceLogicalDeleteEvent, ACTION.LOGICAL_DELETE);
    }
    
    @Test(timeout = 10000)
    public void testRouteResourceTypeIdUpdateEvent() throws Exception {
        ResourceTypeIdUpdateEvent resourceTypeIdUpdateEvent = new ResourceTypeIdUpdateEvent(resource,csUserIdsList,1);
        template.sendBody(EndPointUri.RESOURCE_INCOMING_QUEUE, resourceTypeIdUpdateEvent);
        assertReceivedMessage(resourceTypeIdUpdateEndpoint, resourceTypeIdUpdateEvent, ACTION.TYPE_ID_UPDATE);
    }
    
    @Test(timeout = 10000)
    public void testRouteResourceTypeIdKeyUpdateEvent() throws Exception {
        ResourceTypeIdKeyUpdateEvent resourceTypeIdKeyUpdateEvent = new ResourceTypeIdKeyUpdateEvent(resource,csUserIdsList,"1");
        template.sendBody(EndPointUri.RESOURCE_INCOMING_QUEUE, resourceTypeIdKeyUpdateEvent);
        assertReceivedMessage(resourceTypeIdKeyUpdateEndpoint, resourceTypeIdKeyUpdateEvent, ACTION.TYPE_ID_KEY_UPDATE);
    }
    
    @Test(timeout = 10000)
    public void testRouteResourceContentInheritUpdateEvent() throws Exception {
        ResourceContentInheritUpdateEvent resourceContentInheritUpdateEvent = new ResourceContentInheritUpdateEvent(resource,csUserIdsList,true);
        template.sendBody(EndPointUri.RESOURCE_INCOMING_QUEUE, resourceContentInheritUpdateEvent);
        assertReceivedMessage(resourceContentInheritUpdateEndpoint, resourceContentInheritUpdateEvent, ACTION.CONTENT_INHERIT_UPDATE);
    }
    
    @Test(timeout = 10000)
    public void testRouteResourceStructureInheritUpdateEvent() throws Exception {
        ResourceStructureInheritUpdateEvent resourceStructureInheritUpdateEvent = new ResourceStructureInheritUpdateEvent(resource,csUserIdsList,false);
        template.sendBody(EndPointUri.RESOURCE_INCOMING_QUEUE, resourceStructureInheritUpdateEvent);
        assertReceivedMessage(resourceStructureInheritUpdateEndpoint, resourceStructureInheritUpdateEvent, ACTION.STRUCTURE_INHERIT_UPDATE);
    }

    @Test(timeout = 10000)
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.RESOURCE_INCOMING_QUEUE, "This is a test message");
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterMock.expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }

    private void assertReceivedMessage(MockEndpoint mockEndpoint, Event event, ACTION expectedAction) throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
        assertMessage(mockEndpoint, event, expectedAction);
    }

    private void assertMessage(MockEndpoint mockEndpoint, Event event, ACTION expectedAction) {
        List<Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        Event actualResourceEvent = actualMessages.get(0).getIn().getBody(Event.class);
        assertEquals("Unexpected resource Id", event.getDomainId(), actualResourceEvent.getDomainId());
        assertEquals("Unexpected action", expectedAction, actualResourceEvent.getAction());
    }
}
