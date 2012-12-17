package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.ACCOUNT_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.AGREEMENT_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.AGREEMENT_MEMBER_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.AGREEMENT_OWNER_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.CUSTOMER_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.DEAD_LETTER_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.INVALID_MESSAGE_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.MASTERCUSTOMER_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.MASTERSTRUCTURE_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.MOBILE_OFFICE_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.RESOURCE_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.TNUIDUSERMAPPING_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.USERRESOURCE_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.USER_REFERENCE_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_EQUPMENT_INCOMING_QUEUE;
import static com.telenor.cos.messaging.event.Event.TYPE.ACCOUNT;
import static com.telenor.cos.messaging.event.Event.TYPE.AGREEMENT;
import static com.telenor.cos.messaging.event.Event.TYPE.AGREEMENT_MEMBER;
import static com.telenor.cos.messaging.event.Event.TYPE.AGREEMENT_OWNER;
import static com.telenor.cos.messaging.event.Event.TYPE.CUSTOMER;
import static com.telenor.cos.messaging.event.Event.TYPE.MASTERCUSTOMER;
import static com.telenor.cos.messaging.event.Event.TYPE.MASTERSTRUCTURE;
import static com.telenor.cos.messaging.event.Event.TYPE.MOBILE_OFFER;
import static com.telenor.cos.messaging.event.Event.TYPE.RESOURCE;
import static com.telenor.cos.messaging.event.Event.TYPE.SUBSCRIPTION;
import static com.telenor.cos.messaging.event.Event.TYPE.USERMAPPING;
import static com.telenor.cos.messaging.event.Event.TYPE.USERRESOURCE;
import static com.telenor.cos.messaging.event.Event.TYPE.USER_REFERENCE;
import static com.telenor.cos.messaging.event.Event.TYPE.SUBSCRIPTION_EQUIPMENT;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.ShutdownRoute;
import org.apache.camel.ShutdownRunningTask;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event;

@Component
public class EventRoute extends RouteBuilder {

    private static final String startLogMessage = "Recieved an Event of Type [";
    private static final String completeLogMessage = "] and sent it to [";
    private static final String closeBracket = "]";

    /**
     * Configures the EventRoute
     *
     * @throws Exception if error
     */
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
                .log(LoggingLevel.ERROR, "CamelExchangeException Encountered while routing message. It has been sent to INVALID_MESSAGE_QUEUE. Error Stack:\n${exception.stacktrace}")
                .handled(true)
                .useOriginalMessage()
                .to(INVALID_MESSAGE_QUEUE);

        onException(CosMessagingException.class)
                .log(LoggingLevel.ERROR, "CosMessagingException Encountered while routing message. It has been sent to INVALID_MESSAGE_QUEUE. Error Stack:\n${exception.stacktrace}")
                .handled(true)
                .useOriginalMessage()
                .to(INVALID_MESSAGE_QUEUE);

        onException(Exception.class)
                .log(LoggingLevel.ERROR, "Exception Encountered while routing message. It has been sent to DEAD_LETTER_QUEUE. Error Stack:\n${exception.stacktrace}")
                .handled(true)
                .useOriginalMessage()
                .to(DEAD_LETTER_QUEUE);

        redirectToCorrectDomainQueue();
    }

    private void redirectToCorrectDomainQueue() {
        from(EndPointUri.INCOMING_EVENT_QUEUE)
                .shutdownRoute(ShutdownRoute.Defer)
                .shutdownRunningTask(ShutdownRunningTask.CompleteAllTasks)
                .choice()
                .when(isEventOfType(CUSTOMER)).log(LoggingLevel.DEBUG, startLogMessage + CUSTOMER + completeLogMessage + CUSTOMER_INCOMING_QUEUE + closeBracket).to(CUSTOMER_INCOMING_QUEUE)
                .when(isEventOfType(SUBSCRIPTION)).log(LoggingLevel.DEBUG, startLogMessage + SUBSCRIPTION + completeLogMessage + SUBSCRIPTION_INCOMING_QUEUE + closeBracket).to(SUBSCRIPTION_INCOMING_QUEUE)
                .when(isEventOfType(SUBSCRIPTION_EQUIPMENT)).log(LoggingLevel.DEBUG, startLogMessage + SUBSCRIPTION_EQUIPMENT + completeLogMessage + SUBSCRIPTION_EQUPMENT_INCOMING_QUEUE + closeBracket).to(SUBSCRIPTION_EQUPMENT_INCOMING_QUEUE)
                .when(isEventOfType(ACCOUNT)).log(LoggingLevel.DEBUG, startLogMessage + ACCOUNT + completeLogMessage + ACCOUNT_INCOMING_QUEUE + closeBracket).to(ACCOUNT_INCOMING_QUEUE)
                .when(isEventOfType(AGREEMENT)).log(LoggingLevel.DEBUG, startLogMessage + AGREEMENT + completeLogMessage + AGREEMENT_INCOMING_QUEUE + closeBracket).to(AGREEMENT_INCOMING_QUEUE)
                .when(isEventOfType(MASTERCUSTOMER)).log(LoggingLevel.DEBUG, startLogMessage + MASTERCUSTOMER + completeLogMessage + MASTERCUSTOMER_INCOMING_QUEUE + closeBracket).to(MASTERCUSTOMER_INCOMING_QUEUE)
                .when(isEventOfType(MASTERSTRUCTURE)).log(LoggingLevel.DEBUG, startLogMessage + MASTERSTRUCTURE + completeLogMessage + MASTERSTRUCTURE_INCOMING_QUEUE + closeBracket).to(MASTERSTRUCTURE_INCOMING_QUEUE)
                .when(isEventOfType(USERRESOURCE)).log(LoggingLevel.DEBUG, startLogMessage + USERRESOURCE + completeLogMessage + USERRESOURCE_INCOMING_QUEUE + closeBracket).to(USERRESOURCE_INCOMING_QUEUE)
                .when(isEventOfType(USERMAPPING)).log(LoggingLevel.DEBUG, startLogMessage + USERMAPPING + completeLogMessage + TNUIDUSERMAPPING_INCOMING_QUEUE + closeBracket).to(TNUIDUSERMAPPING_INCOMING_QUEUE)
                .when(isEventOfType(RESOURCE)).log(LoggingLevel.DEBUG, startLogMessage + RESOURCE + completeLogMessage + RESOURCE_INCOMING_QUEUE + closeBracket).to(RESOURCE_INCOMING_QUEUE)
                .when(isEventOfType(MOBILE_OFFER)).log(LoggingLevel.DEBUG, startLogMessage + MOBILE_OFFER + completeLogMessage + MOBILE_OFFICE_INCOMING_QUEUE + closeBracket).to(MOBILE_OFFICE_INCOMING_QUEUE)
                .when(isEventOfType(USER_REFERENCE)).log(LoggingLevel.DEBUG, startLogMessage + USER_REFERENCE + completeLogMessage + USER_REFERENCE_INCOMING_QUEUE + closeBracket).to(USER_REFERENCE_INCOMING_QUEUE)
                .when(isEventOfType(AGREEMENT_MEMBER)).log(LoggingLevel.DEBUG, startLogMessage + AGREEMENT_MEMBER + completeLogMessage + AGREEMENT_MEMBER_INCOMING_QUEUE + closeBracket).to(AGREEMENT_MEMBER_INCOMING_QUEUE)
                .when(isEventOfType(AGREEMENT_OWNER)).log(LoggingLevel.DEBUG, startLogMessage + AGREEMENT_OWNER + completeLogMessage + AGREEMENT_OWNER_INCOMING_QUEUE + closeBracket).to(AGREEMENT_OWNER_INCOMING_QUEUE)
                .otherwise()
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Event event = exchange.getIn().getBody(Event.class);
                        if (event != null) {
                            throw new CosMessagingException("CosMessagingException Encountered while routing event:" + event.toString(), null);
                        } else {
                            throw new CosMessagingException("CosMessagingException Encountered while routing event: [null] event recieved!", null);
                        }
                    }
                })
                .end();
    }

    private Predicate isEventOfType(final Event.TYPE type) {
        return new Predicate() {
            @Override
            public boolean matches(Exchange exchange) {
                Event event = exchange.getIn().getBody(Event.class);
                return event != null && event.getType().equals(type);
            }
        };
    }
}
