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
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class ReplicationServerRouterTest extends RouterBaseTest {

    @EndpointInject(uri = "mock:" + EndPointUri.INCOMING_QUEUE)
    private MockEndpoint incomingQueueMock;

    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/replicationRouterTestContext.xml");
    }

    @Test(timeout = 13000)
    public void testUninterestingMessage() throws Exception {
        template.sendBody(EndPointUri.REPSERVER_QUEUE, UNINTERESTING_XML);
        assertNoMessageReceived(incomingQueueMock);
        assertNoMessageReceived(deadLetterMock);
    }

    @Test(timeout = 13000)
    public void testInterestingMessage() throws Exception {
        template.sendBody(EndPointUri.REPSERVER_QUEUE, INTERESTING_XML);
        assertMessageReceived(incomingQueueMock);
        assertMessage(incomingQueueMock, INTERESTING_XML);
        assertNoMessageReceived(deadLetterMock);
    }

    @Test
    public void testInvalidMessageQueueWithPlainTextMessage() throws Exception {
        executeInvalidMessageQueueTestScenarioWithInputXml("This is a test message");
    }

    @Test
    public void testInValidMessageQueueWithBadXmlFile() throws Exception {
        executeInvalidMessageQueueTestScenarioWithInputXml(BAD_XML);
    }

    private void executeInvalidMessageQueueTestScenarioWithInputXml(String xmlToTest) throws Exception {
        template.sendBody(EndPointUri.REPSERVER_QUEUE, xmlToTest);
        invalidMessageQueueMockEndPoint.setAssertPeriod(1000); //wait to make sure no more than the expected messages are received
        assertMessageReceived(invalidMessageQueueMockEndPoint);
        assertNoMessageReceived(deadLetterMock);
        assertMockEndpointsSatisfied();
    }

    private void assertNoMessageReceived(MockEndpoint mockEndpoint) throws Exception {
        mockEndpoint.expectedMessageCount(0);
        mockEndpoint.assertIsSatisfied();
    }

    private void assertMessageReceived(MockEndpoint mockEndpoint) throws Exception {
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
    }

    private void assertMessage(MockEndpoint mockEndpoint, String xmlMessage) {
        List<Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        String actualMessageBody = actualMessages.get(0).getIn().getBody(String.class);
        assertEquals("Unexpected body received", xmlMessage, actualMessageBody);
    }

    private static final String UNINTERESTING_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<dbStream xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://dhttm12:8000/RepraWebApp/dtds/dbeventstream.xsd\" environment=\"REP_connector.RepCondb\">\n" +
                    "  <tran eventId=\"190:000000086294a6c70216449c00060216449a00050000a05100a4fe190000000000010003\">\n" +
                    "    <update schema=\"SUBSCRIPTION\">\n" +
                    "      <values>\n" +
                    "        <cell name=\"INFO_CHG_DATE\" type=\"DATETIME\">20120514 10:00:43:553</cell>\n" +
                    "        <cell name=\"INFO_CHG_APPL_NAME\" type=\"VARCHAR\">S212RT</cell>\n" +
                    "      </values>\n" +
                    "      <oldValues>\n" +
                    "        <cell name=\"SUBSCR_ID\" type=\"NUMERIC\">30873219</cell>\n" +
                    "        <cell name=\"CONTRACT_ID\" type=\"INT\">11384752</cell>\n" +
                    "      </oldValues>\n" +
                    "    </update>\n" +
                    "  </tran>\n" +
                    "</dbStream>";

    private static final String INTERESTING_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<dbStream xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://dhttm12:8000/RepraWebApp/dtds/dbeventstream.xsd\" environment=\"REP_connector.RepCondb\">\n" +
                    "  <tran eventId=\"190:000000086294a6c70216449c00060216449a00050000a05100a4fe190000000000010003\">\n" +
                    "    <update schema=\"SUBSCRIPTION\">\n" +
                    "      <values>\n" +
                    "        <cell name=\"INFO_CHG_DATE\" type=\"DATETIME\">20120514 10:00:43:553</cell>\n" +
                    "        <cell name=\"SUBSCR_ID\" type=\"NUMERIC\">30873219</cell>\n" +
                    "      </values>\n" +
                    "      <oldValues>\n" +
                    "        <cell name=\"CONTRACT_ID\" type=\"INT\">11384752</cell>\n" +
                    "      </oldValues>\n" +
                    "    </update>\n" +
                    "  </tran>\n" +
                    "</dbStream>";

    private static final String BAD_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<dbStream xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://dhttm12:8000/RepraWebApp/dtds/dbeventstream.xsd\" environment=\"REP_connector.RepCondb\">\n" +
            "  <tran eventId=\"190:000000086294a6c70216449c00060216449a00050000a05100a4fe190000000000010003\">\n" +
            "    <update schema=\"SUBSCRIPTION\">\n" +
            "      <values>\n" +
            "        <cell name=\"INFO_CHG_DATE\" type=\"DATETIME\">20120514 10:00:43:553</cell>\n" +
            "        <cell name=\"SUBSCR_ID\" type=\"NUMERIC\">30873219</cell>\n" +
            "      </values>\n" +
            "      <oldValues>\n" +
            "        <cell name=\"CONTRACT_ID\" type=\"INT\">11384752</cell>\n" +
            "      </oldValues>\n" +
            "    </update>\n" +
            "  </tran>\n" ;

}