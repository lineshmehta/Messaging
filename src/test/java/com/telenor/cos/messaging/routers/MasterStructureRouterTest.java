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
import com.telenor.cos.messaging.event.MasterStructure;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureDeleteEvent;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureNewEvent;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureUpdateEvent;
import com.telenor.cos.test.category.ServiceTest;

/**
 * @author t798435
 */
@Category(ServiceTest.class)
@DirtiesContext
public class MasterStructureRouterTest extends RouterBaseTest {

    private static final Long MASTER_ID = 666L;
    private static final Long OWNER_ID = 777L;

    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;

    @EndpointInject(uri = "mock:" + EndPointUri.MASTERSTRUCTURE_NEW_TOPIC)
    private MockEndpoint masterStructureNewEndPoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.MASTERSTRUCTURE_UPDATED_TOPIC)
    private MockEndpoint masterStructureUpdateEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.MASTERSTRUCTURE_DELETE_TOPIC)
    private MockEndpoint masterStructureDeleteEndPoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/masterStructureRouter.xml");
    }

    @Test(timeout = 10000)
    public void routeMasterStructureNewEvent() throws Exception {
        MasterStructure masterStructure = createMasterStructure();
        Event event = new MasterStructureNewEvent(masterStructure);

        template.sendBody(EndPointUri.MASTERSTRUCTURE_INCOMING_QUEUE, event);
        assertReceivedMessage(masterStructureNewEndPoint, event, ACTION.CREATED);
    }

    @Test(timeout = 10000)
    public void routeMasterStructureUpdateEvent() throws Exception {
        MasterStructure masterStructure = createMasterStructure();
        Event masterStructureEvent = new MasterStructureUpdateEvent(MASTER_ID, masterStructure);
        template().sendBody(EndPointUri.MASTERSTRUCTURE_INCOMING_QUEUE, masterStructureEvent);
        assertReceivedMessage(masterStructureUpdateEndPoint, masterStructureEvent, ACTION.UPDATED);
    }
    
    @Test(timeout = 10000)
    public void routeMasterStructureDeleteEvent() throws Exception {
        Event masterStructureEvent = new MasterStructureDeleteEvent(MASTER_ID);
        template().sendBody(EndPointUri.MASTERSTRUCTURE_INCOMING_QUEUE, masterStructureEvent);
        assertReceivedMessage(masterStructureDeleteEndPoint, masterStructureEvent, ACTION.DELETE);
    }

    @Test
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.MASTERSTRUCTURE_INCOMING_QUEUE, "This is a test message");
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterMock.expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }

    private void assertReceivedMessage(MockEndpoint mockEndpoint, Event masterstructureEvent, ACTION expectedAction) throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
        assertMessage(mockEndpoint, masterstructureEvent, expectedAction);
    }

    private void assertMessage(MockEndpoint mockEndpoint,  Event masterstructureEvent, ACTION expectedAction) {
        List <Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        Event actualMasterStructureEvent = actualMessages.get(0).getIn().getBody(Event.class);
        assertEquals("Unexpected master id", masterstructureEvent.getDomainId(), actualMasterStructureEvent.getDomainId());
        assertEquals("Unexpected action", expectedAction, actualMasterStructureEvent.getAction());
    }
    
    private MasterStructure createMasterStructure(){
        MasterStructure masterStructure = new MasterStructure();
        masterStructure.setMasterId(MASTER_ID);
        masterStructure.setMasterIdOwner(OWNER_ID);
        return masterStructure;
    }
}
