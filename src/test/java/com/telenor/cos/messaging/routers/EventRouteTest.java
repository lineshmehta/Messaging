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
import com.telenor.cos.messaging.event.Agreement;
import com.telenor.cos.messaging.event.AgreementMember;
import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.MasterCustomer;
import com.telenor.cos.messaging.event.Subscription;
import com.telenor.cos.messaging.event.agreement.AgreementNewEvent;
import com.telenor.cos.messaging.event.agreementmember.AgreementMemberNewEvent;
import com.telenor.cos.messaging.event.customer.CustomerNewEvent;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNewEvent;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentEvent;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class EventRouteTest extends RouterBaseTest{

    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;

    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_INCOMING_QUEUE)
    private MockEndpoint subscriptionIncomingQueueMock;

    @EndpointInject(uri = "mock:" + EndPointUri.CUSTOMER_INCOMING_QUEUE)
    private MockEndpoint customerIncomingQueueMock;

    @EndpointInject(uri = "mock:" + EndPointUri.MASTERCUSTOMER_INCOMING_QUEUE)
    private MockEndpoint masterCustomerIncomingQueueMock;

    @EndpointInject(uri = "mock:" + EndPointUri.AGREEMENT_INCOMING_QUEUE)
    private MockEndpoint agreementIncomingQueueMock;

    @EndpointInject(uri = "mock:" + EndPointUri.AGREEMENT_MEMBER_INCOMING_QUEUE)
    private MockEndpoint agreementMemberIncomingQueueMock;

    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_EQUPMENT_INCOMING_QUEUE)
    private MockEndpoint subscribedOfferEquipmentIncomingQueueMock;

    private static final Long ID = 1L;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/eventTestContext.xml");
    }

    @Test(timeout = 10000)
    public void testRouteSusbcriptionEvent() throws Exception {
        Event event = new NewSubscriptionEvent(ID, new Subscription());

        template.sendBody(EndPointUri.INCOMING_EVENT_QUEUE, event);
        assertReceivedMessage(subscriptionIncomingQueueMock, event, TYPE.SUBSCRIPTION);
    }

    @Test(timeout = 10000)
    public void testRouteCustomerEvent() throws Exception {
        CustomerName customerName = new CustomerName(ID);
        customerName.setFirstName("Ole");
        customerName.setMiddleName("Dole");
        customerName.setLastName("Duck");
        CustomerAddress customerAddress = new CustomerAddress(ID);
        Event event = new CustomerNewEvent(customerAddress,customerName);

        template.sendBody(EndPointUri.INCOMING_EVENT_QUEUE, event);
        assertReceivedMessage(customerIncomingQueueMock, event, TYPE.CUSTOMER);
    }

    @Test(timeout = 10000)
    public void testNewMasterCustEvent() throws Exception {
        MasterCustomer masterCustomer = new MasterCustomer();
        masterCustomer.setMasterId(123L);
        masterCustomer.setFirstName("Anna");
        masterCustomer.setLastName("Duck");
        Event event = new MasterCustomerNewEvent(masterCustomer);
        template.sendBody(EndPointUri.INCOMING_EVENT_QUEUE, event);
        assertReceivedMessage(masterCustomerIncomingQueueMock, event, TYPE.MASTERCUSTOMER);
    }

    @Test(timeout = 10000)
    public void testNewAgreementEvent() throws Exception {
        Event event = new AgreementNewEvent(new Agreement());
        template.sendBody(EndPointUri.INCOMING_EVENT_QUEUE, event);
        assertReceivedMessage(agreementIncomingQueueMock, event, TYPE.AGREEMENT);
    }

    @Test(timeout = 10000)
    public void testAgreementMemberNewEvent() throws Exception {
        Event event = new AgreementMemberNewEvent(new AgreementMember());
        template.sendBody(EndPointUri.INCOMING_EVENT_QUEUE, event);
        assertReceivedMessage(agreementMemberIncomingQueueMock, event, TYPE.AGREEMENT_MEMBER);
    }

    @Test
    public void testSubscribedOfferEquipmentEvent() throws Exception {
        Event event = new SubscriptionEquipmentEvent(ACTION.CREATED, 1L, null);
        template.sendBody(EndPointUri.INCOMING_EVENT_QUEUE, event);
        assertReceivedMessage(subscribedOfferEquipmentIncomingQueueMock, event, TYPE.SUBSCRIPTION_EQUIPMENT);
    }

    @Test
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.INCOMING_EVENT_QUEUE, "This is a test message");
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterMock.expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }

    private void assertReceivedMessage(MockEndpoint mockEndpoint, Event event, TYPE expectedType) throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
        assertMessage(mockEndpoint, event, expectedType);
    }

    private void assertMessage(MockEndpoint mockEndpoint,  Event event, TYPE expectedType) {
        List <Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        Event actualSubscriptionEvent = actualMessages.get(0).getIn().getBody(Event.class);
        assertEquals("Unexpected event id", event.getDomainId(), actualSubscriptionEvent.getDomainId());
        assertEquals("Unexpected action", expectedType, actualSubscriptionEvent.getType());
    }

}
