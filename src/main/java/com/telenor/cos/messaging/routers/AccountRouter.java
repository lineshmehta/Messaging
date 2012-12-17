package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.ACCOUNT_INVOICE_FORMAT_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.ACCOUNT_LOGICAL_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.ACCOUNT_NAME_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.ACCOUNT_NEW_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.ACCOUNT_PAYER_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.ACCOUNT_OWNER_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.ACCOUNT_PAYMENTSTATUS_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.ACCOUNT_STATUS_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.ACCOUNT_TYPE_CHANGE_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.INVOICE_FORMAT_CHANGE;
import static com.telenor.cos.messaging.event.Event.ACTION.LOGICAL_DELETE;
import static com.telenor.cos.messaging.event.Event.ACTION.NAME_CHANGE;
import static com.telenor.cos.messaging.event.Event.ACTION.PAYER_CHANGE;
import static com.telenor.cos.messaging.event.Event.ACTION.OWNER_CHANGE;
import static com.telenor.cos.messaging.event.Event.ACTION.PAYMENT_STATUS_CHANGE;
import static com.telenor.cos.messaging.event.Event.ACTION.STATUS_UPDATE;
import static com.telenor.cos.messaging.event.Event.ACTION.TYPE_CHANGE;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;


@Component
public class AccountRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed an Account Event of Type [";

    /**
     * Configures the AccountRouter.
     * @return the choice definition
     * @throws Exception if error
     */
    @Override
    protected ChoiceDefinition whenPart() throws Exception{
       return from(EndPointUri.ACCOUNT_INCOMING_QUEUE)
            .choice()
            .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED +  CLOSE_BRACKET)
            .to(ACCOUNT_NEW_TOPIC)
            .when(isEventOfType(NAME_CHANGE)).log(LoggingLevel.DEBUG, logMessage + NAME_CHANGE.toString() + CLOSE_BRACKET)
            .to(ACCOUNT_NAME_CHANGE_TOPIC)
            .when(isEventOfType(STATUS_UPDATE)).log(LoggingLevel.DEBUG, logMessage + STATUS_UPDATE.toString() + CLOSE_BRACKET)
            .to(ACCOUNT_STATUS_CHANGE_TOPIC)
            .when(isEventOfType(INVOICE_FORMAT_CHANGE)).log(LoggingLevel.DEBUG, logMessage + INVOICE_FORMAT_CHANGE.toString() + CLOSE_BRACKET)
            .to(ACCOUNT_INVOICE_FORMAT_CHANGE_TOPIC)
            .when(isEventOfType(LOGICAL_DELETE)).log(LoggingLevel.DEBUG, logMessage + LOGICAL_DELETE.toString() + CLOSE_BRACKET)
            .to(ACCOUNT_LOGICAL_DELETE_TOPIC)
            .when(isEventOfType(PAYER_CHANGE)).log(LoggingLevel.DEBUG, logMessage + PAYER_CHANGE.toString() + CLOSE_BRACKET)
            .to(ACCOUNT_PAYER_CHANGE_TOPIC)
            .when(isEventOfType(OWNER_CHANGE)).log(LoggingLevel.DEBUG, logMessage + OWNER_CHANGE.toString() + CLOSE_BRACKET)
            .to(ACCOUNT_OWNER_CHANGE_TOPIC)
            .when(isEventOfType(PAYMENT_STATUS_CHANGE)).log(LoggingLevel.DEBUG, logMessage + PAYMENT_STATUS_CHANGE.toString() + CLOSE_BRACKET)
            .to(ACCOUNT_PAYMENTSTATUS_CHANGE_TOPIC)
            .when(isEventOfType(TYPE_CHANGE)).log(LoggingLevel.DEBUG, logMessage + TYPE_CHANGE.toString() + CLOSE_BRACKET)
            .to(ACCOUNT_TYPE_CHANGE_TOPIC);
    }
}
