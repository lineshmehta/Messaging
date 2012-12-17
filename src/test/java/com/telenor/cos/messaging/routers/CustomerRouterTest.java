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
import com.telenor.cos.messaging.event.Customer;
import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.customer.CustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.event.customer.CustomerNameChangeEvent;
import com.telenor.cos.messaging.event.customer.CustomerNewEvent;
import com.telenor.cos.test.category.ServiceTest;

/**
 *  Test class CustomerRouteTest with embedded Camel and mocked topic end points.
 */
@Category(ServiceTest.class)
@DirtiesContext
public class CustomerRouterTest extends RouterBaseTest{

    @EndpointInject(uri = "mock:" + EndPointUri.CUSTOMER_NEW_TOPIC)
    private MockEndpoint customerNewEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.CUSTOMER_NAME_CHANGE_TOPIC)
    private MockEndpoint customerNameChangeEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.CUSTOMER_LOGICAL_DELETE_TOPIC)
    private MockEndpoint customerLogicalDeleteEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;
    
    private static final Long ID = 1L;
    
    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/customerRouter.xml");
    }
    
    @Test(timeout = 10000)
    public void testRouteCustomerNewEvent() throws Exception {
        CustomerName customerName = new CustomerName(ID);
        CustomerAddress customerAddress = new CustomerAddress(ID);
        customerName.setFirstName("Ole");
        customerName.setMiddleName("Dole");
        customerName.setLastName("Doffen");
        Event event = new CustomerNewEvent(customerAddress,customerName);
        
        template.sendBody(EndPointUri.CUSTOMER_INCOMING_QUEUE, event);
        assertReceivedMessage(customerNewEndpoint, event, ACTION.CREATED);
    }
    
    @Test(timeout = 10000)
    public void testRouteCustomerLogicalDeleteEvent() throws Exception {
        Customer customer = new Customer(ID);
        Event event = new CustomerLogicalDeleteEvent(customer.getCustomerId());
        
        template.sendBody(EndPointUri.CUSTOMER_LOGICAL_DELETE_TOPIC, event);
        assertReceivedMessage(customerLogicalDeleteEndpoint, event, ACTION.LOGICAL_DELETE);
    }
    
    @Test(timeout = 10000)
    public void testRouteCustomerNameChangeNewEvent() throws Exception {
        CustomerName customer = new CustomerName(ID);
        customer.setFirstName("Ole");
        customer.setMiddleName("Dole");
        customer.setLastName("Doffen");
        Event event = new CustomerNameChangeEvent(customer.getCustomerId(),customer);
        
        template.sendBody(EndPointUri.CUSTOMER_INCOMING_QUEUE, event);
        assertReceivedMessage(customerNameChangeEndpoint, event, ACTION.NAME_CHANGE);
    }
    
    @Test
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.CUSTOMER_INCOMING_QUEUE, "This is a test message");
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
        Event actualSubscriptionEvent = actualMessages.get(0).getIn().getBody(Event.class);
        assertEquals("Unexpected subscription id", event.getDomainId(), actualSubscriptionEvent.getDomainId());
        assertEquals("Unexpected action", expectedAction, actualSubscriptionEvent.getAction());
    }

}
