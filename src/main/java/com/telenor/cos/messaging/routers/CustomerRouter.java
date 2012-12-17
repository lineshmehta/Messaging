package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.CUSTOMER_ADRESS_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.CUSTOMER_LOGICAL_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.CUSTOMER_NAME_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.CUSTOMER_NEW_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.ADRESS_CHANGE;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.LOGICAL_DELETE;
import static com.telenor.cos.messaging.event.Event.ACTION.NAME_CHANGE;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;

@Component
public class CustomerRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed a Customer Event of Type [";

    /**
     * Configures the CustomerRouter.
     * @return the choice definition
     * @throws Exception if error
     */
    public ChoiceDefinition whenPart() throws Exception {
        return from(EndPointUri.CUSTOMER_INCOMING_QUEUE)
                .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
                .to(CUSTOMER_NEW_TOPIC)
                .when(isEventOfType(LOGICAL_DELETE)).log(LoggingLevel.DEBUG, logMessage + LOGICAL_DELETE + CLOSE_BRACKET)
                .to(CUSTOMER_LOGICAL_DELETE_TOPIC)
                .when(isEventOfType(NAME_CHANGE)).log(LoggingLevel.DEBUG, logMessage + NAME_CHANGE + CLOSE_BRACKET)
                .to(CUSTOMER_NAME_CHANGE_TOPIC)
                .when(isEventOfType(ADRESS_CHANGE)).log(LoggingLevel.DEBUG, logMessage + ADRESS_CHANGE + CLOSE_BRACKET)
                .to(CUSTOMER_ADRESS_CHANGE_TOPIC);
    }

}
