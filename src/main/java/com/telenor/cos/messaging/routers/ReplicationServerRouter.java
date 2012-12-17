package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.INVALID_MESSAGE_QUEUE;
import static org.apache.camel.builder.PredicateBuilder.not;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.predicates.IrrelevantMessagePredicate;

@Component
public class ReplicationServerRouter extends RouteBuilder {

    @Autowired
    private IrrelevantMessagePredicate irrelevantMessagePredicate;

    private String repServerQueue;

    public void setRepServerQueue(String repServerQueue) {
        this.repServerQueue = repServerQueue;
    }

    @Override
    public void configure() throws Exception {

        errorHandler(defaultErrorHandler()
                .maximumRedeliveries(1)
                .redeliveryDelay(2000)
                .useOriginalMessage()
                .logStackTrace(true)
                .retryAttemptedLogLevel(LoggingLevel.WARN));

        onException(Exception.class)
                .log(LoggingLevel.ERROR, "Exception Encountered while processing message. It has been sent to INVALID MESSAGE QUEUE. Error Stack:\n${exception.stacktrace}")
                .handled(true)
                .useOriginalMessage()
                .to(INVALID_MESSAGE_QUEUE);

        from(repServerQueue, EndPointUri.REPSERVER_QUEUE)
                .to("log:com.telenor.cos.messaging.routers.ReplicationServerRouter?level=DEBUG&showAll=true&multiline=true&maxChars=240")
                .convertBodyTo(String.class, Charsets.UTF_8.name())
                .filter(header("JMSRedelivered").isNotEqualTo("true"))
                .filter(not(isIrrelevantMessage()))
                .to(EndPointUri.INCOMING_QUEUE);
    }

    private Predicate isIrrelevantMessage() {
        return irrelevantMessagePredicate;
    }
}