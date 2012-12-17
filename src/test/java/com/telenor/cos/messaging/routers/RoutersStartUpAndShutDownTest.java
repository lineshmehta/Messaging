package com.telenor.cos.messaging.routers;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ShutdownRoute;
import org.apache.camel.ShutdownRunningTask;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractApplicationContext;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Subscription;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.test.category.ServiceTest;

/**
 * Unit Test case for relaible startup and shutdown of camel.
 * Observe following message in the logs
 * o.a.c.i.DefaultShutdownStrategy - Waiting as there are still 1 inflight
 * and pending exchanges to complete, timeout in X seconds.
 * Shutting down with no inflight exchanges.
 * <p/>
 * Time Taken to complete the Test is ~4 seconds
 *
 * @author Babaprakash
 */
@Category(ServiceTest.class)
@Ignore
//TODO This test is very unstable, i suggest we remove it (Eirik)
public class RoutersStartUpAndShutDownTest extends RouterBaseTest {

    private final static String XML = "<insert schema=\"Subscription\">"
            + "<values>"
            + "<cell name=\"subscr_id\" type=\"NUMERIC\">32143317</cell>"
            + "<cell name=\"acc_id\" type=\"INT\">999999001</cell>"
            + "<cell name=\"cust_id_user\" type=\"NUMERIC\">6935066</cell>"
            + "<cell name=\"subscr_valid_from_date\" type=\"DATETIME\">09.01.2012 00:00:00</cell>"
            + "<cell name=\"subscr_valid_to_date\" type=\"DATETIME\" />"
            + "<cell name=\"subscr_has_secret_number\" type=\"CHAR\" />"
            + "<cell name=\"directory_number_id\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"contract_id\" type=\"INT\">12716621</cell>"
            + "<cell name=\"s212_product_id\" type=\"NUMERIC\">04713</cell>"
            + "</values>"
            + "</insert>";

    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_INCOMING_QUEUE)
    private MockEndpoint subscriptionIncomingEndpointMock;

    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_NEW_TOPIC)
    private MockEndpoint subscriptionNewTopicMock;
    
    @EndpointInject(uri = "mock:" + EndPointUri.INCOMING_EVENT_QUEUE)
    private MockEndpoint incominEventQueueMock;

    private static final Long SUBSCRIPTION_ID = Long.valueOf("666");

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/producerTestContext.xml");
    }

    public void setUp() throws Exception {
        super.setUp();
        context.getShutdownStrategy().setTimeout(8);
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(EndPointUri.INCOMING_QUEUE)
                        .routeId(ProducerRoute.class.getName())
                        .shutdownRoute(ShutdownRoute.Defer)
                        .shutdownRunningTask(ShutdownRunningTask.CompleteAllTasks)
                        .to(incominEventQueueMock)
                        .delay(250);

                from(EndPointUri.INCOMING_EVENT_QUEUE)
                        .routeId(EventRoute.class.getName())
                        .shutdownRoute(ShutdownRoute.Defer)
                        .shutdownRunningTask(ShutdownRunningTask.CompleteAllTasks)
                        .to(subscriptionIncomingEndpointMock)
                        .delay(100);
                
                from(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE)
                        .routeId(SubscriptionRouter.class.getName())
                        .shutdownRoute(ShutdownRoute.Defer)
                        .shutdownRunningTask(ShutdownRunningTask.CompleteAllTasks)
                        .to(subscriptionNewTopicMock)
                        .delay(100);
            }
        });
    }

    @Test(timeout = 10000)
    public void testTranslatorStartUpAndShutDown() throws Exception {
        Event newSubscriptionEvent = null;
        for (int i = 0; i < 2; i++) {
            template.sendBody(EndPointUri.INCOMING_QUEUE, XML);
        }
        context.stopRoute(ProducerRoute.class.getName());
        assertTrue(context.getRouteStatus(ProducerRoute.class.getName()).isStopped());
        assertReceivedMessage(incominEventQueueMock, newSubscriptionEvent);
    }

    @Test(timeout = 10000)
    public void testSubscriptionRouterStartUpAndShutDown() throws Exception {
        Event subscriptionEvent = new NewSubscriptionEvent(SUBSCRIPTION_ID, new Subscription());
        for (int i = 0; i < 2; i++) {
            template.sendBody(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE, subscriptionEvent);
        }
        context.stopRoute(SubscriptionRouter.class.getName());
        assertTrue(context.getRouteStatus(SubscriptionRouter.class.getName()).isStopped());
        subscriptionNewTopicMock.expectedMessageCount(2);
        subscriptionNewTopicMock.assertIsSatisfied();
        List<Exchange> actualMessages = subscriptionNewTopicMock.getReceivedExchanges();
        Event actualSubscriptionEvent = actualMessages.get(0).getIn().getBody(Event.class);
        assertEquals("Unexpected subscription id", subscriptionEvent.getDomainId(), actualSubscriptionEvent.getDomainId());
    }


    private void assertReceivedMessage(MockEndpoint mockEndpoint, Event subscriptionEvent) throws Exception {
        mockEndpoint.expectedMessageCount(2);
        mockEndpoint.assertIsSatisfied();
        assertMessage(mockEndpoint, subscriptionEvent);
    }

    private void assertMessage(MockEndpoint mockEndpoint, Event subscriptionEvent) {
        List<Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        String xml = actualMessages.get(0).getIn().getBody(String.class);
        assertTrue(xml.contains("32143317"));
    }

}
