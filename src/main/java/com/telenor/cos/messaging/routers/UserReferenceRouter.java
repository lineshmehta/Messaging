package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.USER_REFERENCE_DESCR_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.USER_REFERENCE_INVOICE_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.USER_REFERENCE_LOGICAL_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.USER_REFERENCE_NEW_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.INVOICE_CHANGE;
import static com.telenor.cos.messaging.event.Event.ACTION.LOGICAL_DELETE;
import static com.telenor.cos.messaging.event.Event.ACTION.USERREF_DESC_CHG;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;

/**
 * Router for UserReference Events.
 * @author Babaprakash D
 *
 */
@Component
public class UserReferenceRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed an UserReference Event of Type [";

    /**
     * Configures the UserReferenceRouter.
     * @return ChoiceDefinition
     * @throws Exception if error
     */
    @Override
    protected ChoiceDefinition whenPart() throws Exception {
        return from(EndPointUri.USER_REFERENCE_INCOMING_QUEUE)
                .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
                .to(USER_REFERENCE_NEW_TOPIC)
                .when(isEventOfType(INVOICE_CHANGE)).log(LoggingLevel.DEBUG, logMessage + INVOICE_CHANGE + CLOSE_BRACKET)
                .to(USER_REFERENCE_INVOICE_CHANGE_TOPIC)
                .when(isEventOfType(USERREF_DESC_CHG)).log(LoggingLevel.DEBUG, logMessage + USERREF_DESC_CHG + CLOSE_BRACKET)
                .to(USER_REFERENCE_DESCR_CHANGE_TOPIC)
                .when(isEventOfType(LOGICAL_DELETE)).log(LoggingLevel.DEBUG, logMessage + LOGICAL_DELETE + CLOSE_BRACKET)
                .to(USER_REFERENCE_LOGICAL_DELETE_TOPIC);
    }
}
