package com.telenor.cos.messaging.routers;

import javax.jms.JMSException;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class SubscriptionProducerExceptionTest extends RouterBaseTest{

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterQueueMock;

    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_INCOMING_QUEUE)
    private MockEndpoint subscriptionIncomingQueueMock;
    
    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;

    private final static String XML = "<insert schema=\"Subscription\">"
            + "<values>"
            + "<cell name=\"subscr_id\" type=\"NUMERIC\">32143317</cell>"
            + "<cell name=\"acc_id\" type=\"INT\">999999001</cell>"
            + "<cell name=\"cust_id_user\" type=\"NUMERIC\">6935066</cell>"
            + "<cell name=\"subscr_valid_from_date\" type=\"DATETIME\">09.01.2012 00:00:00</cell>" 
            + "<cell name=\"subscr_valid_to_date\" type=\"DATETIME\" />"
            + "<cell name=\"subscr_has_secret_number\" type=\"CHAR\" />"
            + "</values>"
            + "</insert>";
    
    private final static String INVALID_XML = "<insert schema=\"Subscription\">"
            + "<values>"
            + "</values>"
            + "</insert>";
    
    public void setUp() throws Exception {
        super.setUp();
        context.removeEndpoints("*");
        resetMocks();
        RouteDefinition routeDefinition = context.getRouteDefinitions().get(0);
        routeDefinition.adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                
                interceptSendToEndpoint(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE)
                        .skipSendToOriginalEndpoint()
                        .process(new SimulateJMSFailure());

                interceptSendToEndpoint(EndPointUri.DEAD_LETTER_QUEUE)
                        .skipSendToOriginalEndpoint()
                        .to(deadLetterQueueMock);
                
                interceptSendToEndpoint(EndPointUri.INVALID_MESSAGE_QUEUE)
                       .skipSendToOriginalEndpoint()
                       .to(invalidMessageQueueMockEndPoint);
            }
        });
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[] {"/producerTestContext.xml", "/camelTestContext.xml"});
    }
    
    @Test
    public void testMessagesCouldNotTranslate() throws Exception {
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        template.sendBody(EndPointUri.INCOMING_QUEUE,INVALID_XML);
        assertMockEndpointsSatisfied();
    }

    @Test(timeout = 20000)
    public void testJMSFailures() throws Exception {
        invalidMessageQueueMockEndPoint.expectedMessageCount(0);
        deadLetterQueueMock.expectedMessageCount(1);
        subscriptionIncomingQueueMock.expectedMessageCount(0);
        template.sendBody(EndPointUri.INCOMING_QUEUE, XML);
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testInvalidMessages() throws Exception {
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterQueueMock.expectedMessageCount(0);
        template.sendBody(EndPointUri.INCOMING_QUEUE,"This is test message");
        assertMockEndpointsSatisfied();
    }
    
    private static class SimulateJMSFailure implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            throw new JMSException("Simulated JMS exception");
        }
    }
}
