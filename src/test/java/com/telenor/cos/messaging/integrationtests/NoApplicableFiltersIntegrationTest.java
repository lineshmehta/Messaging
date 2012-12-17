package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.CosCorrelationIdFactory;
import com.telenor.cos.test.category.IntegrationTest;

@Category(IntegrationTest.class)
public class NoApplicableFiltersIntegrationTest extends CommonIntegrationTest {

    private final static String INVALID_XML = "dataset/invalid.xml";

    @Before
    public void beforeTest() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.INCOMING);
        getJms().receive(ExternalEndpoints.DEAD_LETTER);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test(timeout = 10000)
    public void testNoApplicableFilters() throws Exception {
        String invalidXml = getTestHelper().fileToString(INVALID_XML);
        String eventIdFromXml = getEventIdFromXml(invalidXml);
        String correlationId = getJms().send(ExternalEndpoints.INCOMING_REP_SERVER, invalidXml);
        String messageSelectorExpression = CosCorrelationIdFactory.createMessageSelectorExpression(correlationId);
        String deadLetterMsg = (String) getJms().receiveSelectedAndConvert(ExternalEndpoints.DEAD_LETTER, messageSelectorExpression);
        assertNotNull("Expected a correaltionId", correlationId);
        assertNotNull("Expected a deadLetter message", deadLetterMsg);
        assertTrue("Wrong message received on deadLetterQueue", deadLetterMsg.contains(eventIdFromXml));
    }

    private String getEventIdFromXml(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        ByteArrayInputStream bis = new ByteArrayInputStream(xml.getBytes(Charset.defaultCharset()));
        Document doc = db.parse(bis);
        return doc.getDocumentElement().getAttribute("eventId");
    }
}