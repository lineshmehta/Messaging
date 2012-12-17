package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.DEAD_LETTER_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.INVALID_MESSAGE_QUEUE;

import javax.xml.stream.XMLStreamException;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.event.Event;

public abstract class AbstractRouter extends RouteBuilder{

    /**
     * Check id an {@link Exchange} has the given {@link com.telenor.cos.messaging.event.Event.ACTION}.
     *
     * @param action the action
     * @return true if the {@link Exchange} has the given {@link com.telenor.cos.messaging.event.Event.ACTION}, otherwise false
     */
     protected Predicate isEventOfType(final Event.ACTION action) {
        return new Predicate() {
            @Override
            public boolean matches(Exchange exchange) {
                Event event = exchange.getIn().getBody(Event.class);
                if(event != null) {
                    return event.getAction().equals(action);
                }else {
                    return false;
                }
            }
        };
    }

     /**
      * Set the error handlers
      */
     protected void setErrorHandlers() {
         getContext().setHandleFault(true);
         errorHandler(defaultErrorHandler()
                 .maximumRedeliveries(3)
                 .redeliveryDelay(2000)
                 .useOriginalMessage()
                 .logStackTrace(true)
                 .retryAttemptedLogLevel(LoggingLevel.WARN));

         onException(CamelExchangeException.class)
                 .log(LoggingLevel.ERROR, "INVALID_MESSAGE_QUEUE: Check " + INVALID_MESSAGE_QUEUE + " to see message details. \n ${exception.stacktrace}")
                 .handled(true)
                 .useOriginalMessage()
                 .to(INVALID_MESSAGE_QUEUE);

         onException(XMLStreamException.class)
                 .log(LoggingLevel.ERROR, "INVALID_MESSAGE_QUEUE: ${exception.stacktrace}")
                 .handled(true)
                 .useOriginalMessage()
                 .to(INVALID_MESSAGE_QUEUE);

         onException(CosMessagingException.class)
                 .log(LoggingLevel.ERROR, "INVALID_MESSAGE_QUEUE: Check " + INVALID_MESSAGE_QUEUE + " to see message details. \n ${exception.stacktrace}")
                 .handled(true)
                 .useOriginalMessage()
                 .to(INVALID_MESSAGE_QUEUE);

         onException(Exception.class)
                 .log(LoggingLevel.ERROR, "DEADLETTER_QUEUE: Check " + DEAD_LETTER_QUEUE + " to see message details. \n ${exception.stacktrace}")
                 .handled(true)
                 .useOriginalMessage()
                 .to(DEAD_LETTER_QUEUE);
     }
}
