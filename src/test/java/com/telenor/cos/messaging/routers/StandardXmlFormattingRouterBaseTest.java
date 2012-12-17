package com.telenor.cos.messaging.routers;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.thoughtworks.xstream.XStream;

/**
 * Base test case for classes that implement {@link StandardXmlFormattingRouter}
 */
public abstract class StandardXmlFormattingRouterBaseTest extends StandardRouterBaseTest {

    @Override
    protected void assertMessage(MockEndpoint mockEndpoint, Event event, ACTION expectedAction) {
        List<Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        String xmlEvent = actualMessages.get(0).getIn().getBody(String.class);

        Event actualEvent = (Event) new XStream().fromXML(xmlEvent);
        assertEquals("Unexpected subscription id", event.getDomainId(), actualEvent.getDomainId());
        assertEquals("Unexpected action", expectedAction, actualEvent.getAction());
    }

    /**
     * @return the name of the incoming queue
     */
    protected abstract String getIncomingQueue();

}