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
import com.telenor.cos.messaging.event.Account;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountNameChangeEvent;
import com.telenor.cos.messaging.event.account.AccountNewEvent;
import com.telenor.cos.test.category.ServiceTest;

/**
 * Test class AccountRouter. Test with embedded Camel and mocked topic end points.
 */
@Category(ServiceTest.class)
@DirtiesContext
public class AccountRouterTest extends RouterBaseTest {

    @EndpointInject(uri = "mock:" + EndPointUri.ACCOUNT_NEW_TOPIC)
    private MockEndpoint accountNewEndpoint;
    
    @EndpointInject(uri = "mock:" + EndPointUri.ACCOUNT_NAME_CHANGE_TOPIC)
    private MockEndpoint accountNameChangeEndpoint;

    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;
    
    private static final Long ACCOUNT_ID = Long.valueOf(22);
    private static final String NAME_CHANGE = "DDDD";

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/accountRouter.xml");
    }

    @Test(timeout = 10000)
    public void testRouteAccountNewEvent() throws Exception {
        Account account = new Account();
        account.setAccountId(ACCOUNT_ID);
        Event event = new AccountNewEvent(account);

        template.sendBody(EndPointUri.ACCOUNT_INCOMING_QUEUE, event);
        assertReceivedMessage(accountNewEndpoint, event, ACTION.CREATED);
    }
    
    @Test(timeout = 10000)
    public void testRouteAccountUpdateEvents() throws Exception {
        Event event = new AccountNameChangeEvent(ACCOUNT_ID,NAME_CHANGE);
        template.sendBody(EndPointUri.ACCOUNT_INCOMING_QUEUE, event);
        assertReceivedMessage(accountNameChangeEndpoint, event, ACTION.NAME_CHANGE);
    }
    
    @Test(timeout = 10000)
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(EndPointUri.ACCOUNT_INCOMING_QUEUE, "This is a test message");
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
        Event actualAccountEvent = actualMessages.get(0).getIn().getBody(Event.class);
        assertEquals("Unexpected subscription id", event.getDomainId(), actualAccountEvent.getDomainId());
        assertEquals("Unexpected action", expectedAction, actualAccountEvent.getAction());
    }
}
