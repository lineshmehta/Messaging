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
import com.telenor.cos.messaging.event.MasterCustomer;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNewEvent;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class MasterCustomerRouterTest extends RouterBaseTest {

    private static final String FIRST_NAME = "Anna";

    private static final String LAST_NAME = "Duck";

    private static final Long MASTER_ID = 123L;

    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;

    @EndpointInject(uri = "mock:" + EndPointUri.MASTERCUSTOMER_NEW_TOPIC)
    private MockEndpoint masterCustomerNewEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.MASTERCUSTOMER_LOGICAL_DELETE_TOPIC)
    private MockEndpoint masterCustomerLogicalDeleteEndPoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/masterCustomerRouter.xml");
    }

    @Test(timeout = 10000)
    public void testRouteCustomerNewEvent() throws Exception {
        MasterCustomer masterCustomer = new MasterCustomer();
        masterCustomer.setMasterId(MASTER_ID);
        masterCustomer.setFirstName(FIRST_NAME);
        masterCustomer.setLastName(LAST_NAME);
        Event event = new MasterCustomerNewEvent(masterCustomer);

        template.sendBody(EndPointUri.MASTERCUSTOMER_INCOMING_QUEUE, event);
        assertReceivedMessage(masterCustomerNewEndPoint, event, ACTION.CREATED);
    }

    @Test(timeout = 10000)
    public void testRouteMasterCustomerLogicalDeleteEvent() throws Exception {
        Event masterCustomerEvent = new MasterCustomerLogicalDeleteEvent(MASTER_ID);

        template().sendBody(EndPointUri.MASTERCUSTOMER_INCOMING_QUEUE, masterCustomerEvent);
        assertReceivedMessage(masterCustomerLogicalDeleteEndPoint, masterCustomerEvent, ACTION.LOGICAL_DELETE );
    }

    @Test
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.MASTERCUSTOMER_INCOMING_QUEUE, "This is a test message");
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterMock.expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }

    private void assertReceivedMessage(MockEndpoint mockEndpoint, Event mastercustomerEvent, ACTION expectedAction) throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
        assertMessage(mockEndpoint, mastercustomerEvent, expectedAction);
    }

    private void assertMessage(MockEndpoint mockEndpoint,  Event mastercustomerEvent, ACTION expectedAction) {
        List <Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        Event actualMasterCustomerEvent = actualMessages.get(0).getIn().getBody(Event.class);
        assertEquals("Unexpected master id", mastercustomerEvent.getDomainId(), actualMasterCustomerEvent.getDomainId());
        assertEquals("Unexpected action", expectedAction, actualMasterCustomerEvent.getAction());
    }
}
