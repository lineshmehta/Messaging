package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_CHANGE_ACCOUNT_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_CHANGE_STATUS_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_CHANGE_TYPE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_CHANGE_USER_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_EXPIRED_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_LOGICAL_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_NEW_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_SECRET_NUMBER_UPDATE_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CHANGE_ACCOUNT;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.EXPIRED;
import static com.telenor.cos.messaging.event.Event.ACTION.LOGICAL_DELETE;
import static com.telenor.cos.messaging.event.Event.ACTION.SECRET_NUMBER;
import static com.telenor.cos.messaging.event.Event.ACTION.STATUS_CHANGE;
import static com.telenor.cos.messaging.event.Event.ACTION.TYPE_CHANGE;
import static com.telenor.cos.messaging.event.Event.ACTION.USER_CHANGE;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;

@Component
public class SubscriptionRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed a Susbcription Event of Type [";

    /**
     * Configures the SubscriptionRouter.
     * @return nothing document-able!
     * @throws Exception if error
     */
    @Override
    protected ChoiceDefinition whenPart() throws Exception {
        return from(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE)
                .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
                .to(SUBSCRIPTION_NEW_TOPIC)
                .when(isEventOfType(CHANGE_ACCOUNT)).log(LoggingLevel.DEBUG, logMessage + CHANGE_ACCOUNT + CLOSE_BRACKET)
                .to(SUBSCRIPTION_CHANGE_ACCOUNT_TOPIC)
                .when(isEventOfType(EXPIRED)).log(LoggingLevel.DEBUG, logMessage + EXPIRED + CLOSE_BRACKET)
                .to(SUBSCRIPTION_EXPIRED_TOPIC)
                .when(isEventOfType(LOGICAL_DELETE)).log(LoggingLevel.DEBUG, logMessage + LOGICAL_DELETE + CLOSE_BRACKET)
                .to(SUBSCRIPTION_LOGICAL_DELETE_TOPIC)
                .when(isEventOfType(USER_CHANGE)).log(LoggingLevel.DEBUG, logMessage + USER_CHANGE + CLOSE_BRACKET)
                .to(SUBSCRIPTION_CHANGE_USER_TOPIC)
                .when(isEventOfType(TYPE_CHANGE)).log(LoggingLevel.DEBUG, logMessage + TYPE_CHANGE + CLOSE_BRACKET)
                .to(SUBSCRIPTION_CHANGE_TYPE_TOPIC)
                .when(isEventOfType(STATUS_CHANGE)).log(LoggingLevel.DEBUG, logMessage + STATUS_CHANGE + CLOSE_BRACKET)
                .to(SUBSCRIPTION_CHANGE_STATUS_TOPIC)
                .when(isEventOfType(SECRET_NUMBER)).log(LoggingLevel.DEBUG, logMessage + SECRET_NUMBER + CLOSE_BRACKET)
                .to(SUBSCRIPTION_SECRET_NUMBER_UPDATE_TOPIC);
    }
}

