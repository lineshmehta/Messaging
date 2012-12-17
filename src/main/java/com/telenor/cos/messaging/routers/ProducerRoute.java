package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.DEAD_LETTER_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.INVALID_MESSAGE_QUEUE;

import java.util.Collection;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.ShutdownRoute;
import org.apache.camel.ShutdownRunningTask;
import org.apache.camel.model.MulticastDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.producers.Producer;
import com.telenor.cos.messaging.routers.aggregation.NewSubscriptionAggregationStrategy;


@SuppressWarnings("rawtypes")
public class ProducerRoute extends AbstractRouter {
    private static final String SUBSCRIPTION_XPATH = "//*[@schema='SUBSCRIPTION'] | //insert[@schema='REL_SUBSCRIPTION'] | //insert[@schema='USER_REFERENCE']";
    private static final String NEW_SUBSCRIPTION_AGGREGATION_XPATH =
            "//insert[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_ID']/text() | " +
                    "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='SUBSCR_ID']/text() | " +
                    "//insert[@schema='REL_SUBSCRIPTION']//values//cell[@name='subscr_id_member']/text() |" +
                    "//insert[@schema='USER_REFERENCE']//values//cell[@name='SUBSCR_ID']/text() ";

    private static final String MESSAGES_TO_SEND_TO_PRODUCERS = "direct:messagesToProducers";
    private static final String POTENTIAL_SUBSCRIPTION_AGGREGATE_MESSAGES = "direct:potentialNewSubscriptionMessages";
    private String incomingQueue;
    private String completionTimeout;

    @Autowired
    private Collection<Producer> producers;

    @Autowired
    private NewSubscriptionAggregationStrategy newSubscriptionAggregationStrategy;

    public void setIncomingQueue(String incomingQueue) {
        this.incomingQueue = incomingQueue;
    }

    public void setCompletionTimeout(String completionTimeout) {
        this.completionTimeout = completionTimeout;
    }

    @Override
    public void configure() throws Exception {
        getContext().setHandleFault(true);
        errorHandler(defaultErrorHandler()
                .maximumRedeliveries(3)
                .redeliveryDelay(2000)
                .useOriginalMessage()
                .logStackTrace(true)
                .retryAttemptedLogLevel(LoggingLevel.WARN));

        onException(CamelExchangeException.class)
                .log(LoggingLevel.ERROR, "CamelExchangeException while trying to Route! Sending it to [" + INVALID_MESSAGE_QUEUE + "]\nError stack:\n${exception.stacktrace}")
                .handled(true)
                .useOriginalMessage()
                .to(INVALID_MESSAGE_QUEUE);

        onException(CosMessagingException.class)
                .log(LoggingLevel.ERROR, "CosMessagingException while trying to Route! Sending it to [" + INVALID_MESSAGE_QUEUE + "]\nError stack:\n${exception.stacktrace}")
                .handled(true)
                .useOriginalMessage()
                .to(INVALID_MESSAGE_QUEUE);

        onException(Exception.class)
                .log(LoggingLevel.ERROR, "Exception while trying to Route! Sending it to [" + DEAD_LETTER_QUEUE + "]\nError stack:\n${exception.stacktrace}")
                .handled(true)
                .useOriginalMessage()
                .to(DEAD_LETTER_QUEUE);

        filterSubscriptionMessages();
        aggregateAndRouteNewSubscriptionMessages();
        multicastMessages();
    }

    private void filterSubscriptionMessages() {
        from(incomingQueue)
                .startupOrder(1)
                .convertBodyTo(Document.class)
                .choice()
                .when().xpath(SUBSCRIPTION_XPATH)
                .to(POTENTIAL_SUBSCRIPTION_AGGREGATE_MESSAGES)
                .otherwise()
                .to(MESSAGES_TO_SEND_TO_PRODUCERS)
                .end();
    }

    private void aggregateAndRouteNewSubscriptionMessages() {
        from(POTENTIAL_SUBSCRIPTION_AGGREGATE_MESSAGES)
                .startupOrder(2)
                .aggregate(xpath(NEW_SUBSCRIPTION_AGGREGATION_XPATH), newSubscriptionAggregationStrategy)
                .completionPredicate(header("abortAggregation").isEqualTo(true))
                .ignoreInvalidCorrelationKeys()
                .completionTimeout(Long.valueOf(completionTimeout))
                .choice()
                .when(isEventOfType(Event.ACTION.CREATED))
                .to(EndPointUri.SUBSCRIPTION_NEW_TOPIC)
                .otherwise()
                .to(MESSAGES_TO_SEND_TO_PRODUCERS)
                .end();
    }

    private void multicastMessages() {
        ProcessorDefinition processorDefinition = from(MESSAGES_TO_SEND_TO_PRODUCERS)
                .startupOrder(3)
                .routeId("ProducerRoute")
                .shutdownRoute(ShutdownRoute.Defer)
                .shutdownRunningTask(ShutdownRunningTask.CompleteAllTasks);

        MulticastDefinition multicastDefinition = processorDefinition.multicast();
        addAllProducers(multicastDefinition);
        multicastDefinition
                .choice()
                .when(isFilterUnMatched())
                .log(LoggingLevel.DEBUG, "Message was not applicable to any of the filters. Sending it to [" + DEAD_LETTER_QUEUE + "]")
                .to(DEAD_LETTER_QUEUE)
                .end();
    }

    private void addAllProducers(MulticastDefinition multicastDefinition) {
        for (Producer producer : producers) {
            multicastDefinition
                    .filter().method(producer, "filter")
                    .bean(producer, "createEvent").split(body())
                    .to(EndPointUri.INCOMING_EVENT_QUEUE).end();
        }
        multicastDefinition.end();
    }

    private Predicate isFilterUnMatched() {
        return new Predicate() {
            @Override
            public boolean matches(Exchange exchange) {
                Object userData = exchange.getIn().getBody(Document.class).getUserData(Exchange.FILTER_MATCHED);
                Boolean filterMatched = userData != null ? (Boolean) userData : Boolean.FALSE;
                return !filterMatched;
            }
        };
    }
}