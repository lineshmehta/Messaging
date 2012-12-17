package com.telenor.cos.messaging.integrationtests;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.CosCorrelationIdFactory;
import com.telenor.cos.test.category.IntegrationTest;

@Category(IntegrationTest.class)
public class ReplicationServerRouterIntegrationTest extends CommonIntegrationTest {

    private final static String UNINTERESTING_XML = "dataset/uninterestingMessage.xml";

    @Before
    public void beforeTest() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(EndPointUri.REPSERVER_QUEUE);
        getJms().receive(EndPointUri.INCOMING_QUEUE);
        getJms().setReceiveTimeout(20000);
    }

    @Test(timeout = 40000)
    public void testUnuinterestingMessage() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(UNINTERESTING_XML);
        String messageSelectorExpression = CosCorrelationIdFactory.createMessageSelectorExpression(correlationId);
        String incomingQueueMsg = (String) getJms().receiveSelectedAndConvert(EndPointUri.INCOMING_QUEUE, messageSelectorExpression);
        assertNull("Should not receive message on INCOMING_QUEUE", incomingQueueMsg);
        assertNotNull("Expected a correaltionId", correlationId);
    }
}
