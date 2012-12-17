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
import com.telenor.cos.messaging.event.UserReference;
import com.telenor.cos.messaging.event.userref.InvoiceReferenceUpdateEvent;
import com.telenor.cos.messaging.event.userref.UserReferenceDescriptionUpdateEvent;
import com.telenor.cos.messaging.event.userref.UserReferenceLogicalDeleteEvent;
import com.telenor.cos.messaging.event.userref.UserReferenceNewEvent;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class UserReferenceRouterTest extends RouterBaseTest {

    private final static Long SUBSCRIPTION_ID = Long.valueOf("32870370");
    private final static String NUMBER_TYPE = "ES";
    private final static String INVOICE_REF = "Test";
    private final static String USER_REF_DESCR = "TEST REF 2";

    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;

    @EndpointInject(uri = "mock:" + EndPointUri.USER_REFERENCE_NEW_TOPIC)
    private MockEndpoint userReferenceNewEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.USER_REFERENCE_DESCR_CHANGE_TOPIC)
    private MockEndpoint userReferenceDescrUpdateEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.USER_REFERENCE_INVOICE_CHANGE_TOPIC)
    private MockEndpoint invoiceReferenceUpdateEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.USER_REFERENCE_LOGICAL_DELETE_TOPIC)
    private MockEndpoint userReferenceLogicalDeleteEndPoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/userReferenceRouter.xml");
    }

    @Test(timeout = 10000)
    public void testRouteUserReferenceNewEvent() throws Exception {
        UserReferenceNewEvent userReferenceNewEvent = new UserReferenceNewEvent(SUBSCRIPTION_ID, new UserReference());
        template.sendBody(EndPointUri.USER_REFERENCE_INCOMING_QUEUE, userReferenceNewEvent);
        assertReceivedMessage(userReferenceNewEndPoint);
        List <Exchange> actualMessages = userReferenceNewEndPoint.getReceivedExchanges();
        UserReferenceNewEvent actualUserReferenceNewEvent = actualMessages.get(0).getIn().getBody(UserReferenceNewEvent.class);
        assertActionAndDomainId(actualUserReferenceNewEvent,ACTION.CREATED);
    }

    /**
     * Tests UserReferenceLogicalDeleteEvent Router.
     * @throws Exception
     */
    @Test(timeout = 10000)
    public void testRouteUserReferenceLogicalDeleteEvent() throws Exception {
        Event event = new UserReferenceLogicalDeleteEvent(SUBSCRIPTION_ID,NUMBER_TYPE);
        template.sendBody(EndPointUri.USER_REFERENCE_INCOMING_QUEUE, event);
        assertReceivedMessage(userReferenceLogicalDeleteEndPoint);
        List <Exchange> actualMessages = userReferenceLogicalDeleteEndPoint.getReceivedExchanges();
        UserReferenceLogicalDeleteEvent actualUserReferenceLogicalDeleteEvent = actualMessages.get(0).getIn().getBody(UserReferenceLogicalDeleteEvent.class);
        assertActionAndDomainId(actualUserReferenceLogicalDeleteEvent,ACTION.LOGICAL_DELETE);
    }

    /**
     * Tests UserReferenceDescrUpdateEvent Router.
     * @throws Exception
     */
    @Test(timeout = 10000)
    public void testRouteUserReferenceDescrUpdateEvent() throws Exception {
        Event event = new UserReferenceDescriptionUpdateEvent(SUBSCRIPTION_ID, USER_REF_DESCR,NUMBER_TYPE);
        template.sendBody(EndPointUri.USER_REFERENCE_INCOMING_QUEUE, event);
        assertReceivedMessage(userReferenceDescrUpdateEndPoint);
        List <Exchange> actualMessages = userReferenceDescrUpdateEndPoint.getReceivedExchanges();
        UserReferenceDescriptionUpdateEvent actualUserReferenceDescriptionUpdateEvent = actualMessages.get(0).getIn().getBody(UserReferenceDescriptionUpdateEvent.class);  
        assertEquals(USER_REF_DESCR, actualUserReferenceDescriptionUpdateEvent.getUserRefDescr());
        assertActionAndDomainId(actualUserReferenceDescriptionUpdateEvent,ACTION.USERREF_DESC_CHG);  
    }

    /**
     * Tests InvoiceReferenceUpdate Event Router. 
     * @throws Exception
     */
    @Test(timeout = 10000)
    public void testRouteInvoiceReferenceUpdateEvent() throws Exception {
        Event event = new InvoiceReferenceUpdateEvent(SUBSCRIPTION_ID, INVOICE_REF);
        template.sendBody(EndPointUri.USER_REFERENCE_INCOMING_QUEUE, event);
        assertReceivedMessage(invoiceReferenceUpdateEndPoint);
        List <Exchange> actualMessages = invoiceReferenceUpdateEndPoint.getReceivedExchanges();
        InvoiceReferenceUpdateEvent actualInvoiceReferenceUpdateEvent = actualMessages.get(0).getIn().getBody(InvoiceReferenceUpdateEvent.class);    
        assertEquals(INVOICE_REF, actualInvoiceReferenceUpdateEvent.getInvoiceRef());
        assertActionAndDomainId(actualInvoiceReferenceUpdateEvent,ACTION.INVOICE_CHANGE);
    }

    @Test
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.USER_REFERENCE_INCOMING_QUEUE, "This is a test message");
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterMock.expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }

    private void assertReceivedMessage(MockEndpoint mockEndpoint) throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
    }
    private void assertActionAndDomainId(Event event,ACTION action) {
        assertEquals(SUBSCRIPTION_ID, event.getDomainId());
        assertEquals(action, event.getAction()); 
    }
}
