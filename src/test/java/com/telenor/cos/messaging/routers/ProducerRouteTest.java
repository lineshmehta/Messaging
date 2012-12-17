package com.telenor.cos.messaging.routers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.util.TestHelper;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
public class ProducerRouteTest extends RouterBaseTest {
    private final static String EXPIRED_SUBSCRIPTION_XML = "dataset/expired_subscription.xml";
    private final static String INVALID_XML = "dataset/invalid.xml";

    private TestHelper testHelperUtil = new TestHelper();

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterQueueMock;

    @EndpointInject(uri = "mock:" + EndPointUri.INCOMING_EVENT_QUEUE)
    private MockEndpoint incomingEventQueue;

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[] {"/producerTestContext.xml", "/camelTestContext.xml"});
    }

    /**
     * Tests that xmls where no filters are applicable end up in the {@link EndPointUri#DEAD_LETTER_QUEUE}.
     * @throws Exception
     */
    @Test
    public void testNotApplicableFilters() throws Exception {
        String body = testHelperUtil.fileToString(INVALID_XML);
        String eventIdFromXml = getEventIdFromXml(body);
        template.sendBody(EndPointUri.INCOMING_QUEUE, body);
        deadLetterQueueMock.expectedMessageCount(1);
        deadLetterQueueMock.assertIsSatisfied();
        List<Exchange> actualMessages = deadLetterQueueMock.getReceivedExchanges();
        String actualMessageBody = actualMessages.get(0).getIn().getBody(String.class);
        assertTrue("Wrong message received on deadLetterQueue. Expected eventId " + eventIdFromXml, actualMessageBody.contains(eventIdFromXml));
    }

    /**
     * Tests that xml where {@link com.telenor.cos.messaging.producers.SubscriptionExpiredProducer#isApplicable(org.w3c.dom.Node)}
     * is applicable does not end up in the {@link EndPointUri#DEAD_LETTER_QUEUE}.
     * @throws Exception
     */
    @Test(timeout = 10000)
    public void testApplicableFilter() throws Exception {
        String body = testHelperUtil.fileToString(EXPIRED_SUBSCRIPTION_XML);
        String eventIdFromXml = getEventIdFromXml(body);
        template.sendBody(EndPointUri.INCOMING_QUEUE, body);
        deadLetterQueueMock.expectedMessageCount(0);
        deadLetterQueueMock.assertIsSatisfied();
        incomingEventQueue.expectedMessageCount(1);
        incomingEventQueue.assertIsSatisfied();
        List<Exchange> actualMessages = incomingEventQueue.getReceivedExchanges();
        String actualMessageBody = actualMessages.get(0).getIn().getBody(String.class);
        assertTrue("eventId not present in  message: " + eventIdFromXml, actualMessageBody.contains(eventIdFromXml));
    }

    private String getEventIdFromXml(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes(Charset.defaultCharset()));
        Document doc = db.parse(bis);
        return doc.getElementsByTagName("tran").item(0).getAttributes().getNamedItem("eventId").getNodeValue();
    }
}
