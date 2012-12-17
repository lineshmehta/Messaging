package com.telenor.cos.messaging.routers;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Subscription;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.test.category.ServiceTest;

import javax.jms.JMSException;

/**
 * Description of the class.
 *
 * @author T553048
 * @since COS
 */
@Category(ServiceTest.class)
@DirtiesContext
public class SubscriptionRouterExceptionTest extends CamelSpringTestSupport {

    @EndpointInject(uri = "mock:" + EndPointUri.DEAD_LETTER_QUEUE)
    private MockEndpoint deadLetterMock;

    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_NEW_TOPIC)
    private MockEndpoint subscriptionNewTopicMock;
    
    @EndpointInject(uri = "mock:" + EndPointUri.INVALID_MESSAGE_QUEUE)
    private MockEndpoint invalidMessageQueueMockEndPoint;

    private static final Long SUBSCRIPTION_ID = Long.valueOf("666");
    private RouteDefinition routeDefinition;


    /**
     * Setting up the test with an advised route.
     * We intercept the sending of messages to SUBSCRIPTION_NEW_TOPIC and simulates a JMSException.
     * To be able to count the number of messages sendt to the DEAD_LETTER_QUEUE we need to intercept the
     * messages sent to the real DEAD_LETTER_QUEUE and route them to the mock version of that endpoint.
     *
     * @throws Exception
     */
    public void setUp() throws Exception {
        super.setUp();
        routeDefinition = context.getRouteDefinitions().get(0);
        routeDefinition.adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint(EndPointUri.SUBSCRIPTION_NEW_TOPIC)
                        .skipSendToOriginalEndpoint()
                        .process(new SimulateJMSFailure());
                
                interceptSendToEndpoint(EndPointUri.INVALID_MESSAGE_QUEUE)
                .skipSendToOriginalEndpoint()
                .to("mock:" + EndPointUri.INVALID_MESSAGE_QUEUE);

                interceptSendToEndpoint(EndPointUri.DEAD_LETTER_QUEUE)
                        .skipSendToOriginalEndpoint()
                        .to("mock:" + EndPointUri.DEAD_LETTER_QUEUE);
            }
        });
    }
    
    public boolean isCreateCamelContextPerClass() {
        return true;
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/subscriptionRouter.xml");
    }

    @Test(timeout = 10000)
    public void testRouteNewSubscriptionError() throws Exception {
        deadLetterMock.expectedMessageCount(1);
        subscriptionNewTopicMock.expectedMessageCount(0);

        Event subscriptionEvent = new NewSubscriptionEvent(SUBSCRIPTION_ID, new Subscription());
        template.sendBody(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE, subscriptionEvent);

        assertMockEndpointsSatisfied();
    }

    @Test(timeout = 10000)
    public void testRouteNoAction() throws Exception {
        invalidMessageQueueMockEndPoint.expectedMessageCount(1);
        deadLetterMock.expectedMessageCount(0);
        subscriptionNewTopicMock.expectedMessageCount(0);

        template.sendBody(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE, "Unknown Message");

        assertMockEndpointsSatisfied();
    }

    private static class SimulateJMSFailure implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            throw new JMSException("Simulated JMS exception");
        }
    }
}
