package com.telenor.cos.messaging.routers;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;

/**
 * Base test case for classes that implement {@link StandardRouter}
 */
public abstract class StandardRouterBaseTest extends RouterBaseTest {
    
    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE) 
    private MockEndpoint invalidMessageQueueMockEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE) 
    private MockEndpoint deadLetterMock;
    
    @Test(timeout = 10000)
    public void testInvalidMessageQueue() throws Exception {
        template.sendBody(getIncomingQueue(), "This is a test message");
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterMock.expectedMessageCount(0);
        assertMockEndpointsSatisfied();
    }

    protected void assertReceivedMessage(MockEndpoint mockEndpoint, Event event, ACTION expectedAction) throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
        assertMessage(mockEndpoint, event, expectedAction);
    }

    protected void assertMessage(MockEndpoint mockEndpoint, Event event, ACTION expectedAction) {
        List<Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        Event actualAccountEvent = actualMessages.get(0).getIn().getBody(Event.class);
        assertEquals("Unexpected subscription id", event.getDomainId(), actualAccountEvent.getDomainId());
        assertEquals("Unexpected action", expectedAction, actualAccountEvent.getAction());
    }

    /**
     * @return the name of the incoming queue
     */
    protected abstract String getIncomingQueue();
}