package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.MOBILE_OFFICE_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.MOBILE_OFFICE_NEW_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.MOBILE_OFFICE_UPADTE_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.DELETE;
import static com.telenor.cos.messaging.event.Event.ACTION.UPDATED;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;

@Component
public class MobileOfficeRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed a Mobile Office Event of Type [";

    /**
     * Configures the MasterCustomerRouter.
     * @return the choice definition
     *
     * @throws Exception if error
     */
    @Override
    public ChoiceDefinition whenPart() throws Exception {
        return from(EndPointUri.MOBILE_OFFICE_INCOMING_QUEUE)
                .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
                .to(MOBILE_OFFICE_NEW_TOPIC)
                .when(isEventOfType(UPDATED)).log(LoggingLevel.DEBUG, logMessage + UPDATED + CLOSE_BRACKET)
                .to(MOBILE_OFFICE_UPADTE_TOPIC)
                .when(isEventOfType(DELETE)).log(LoggingLevel.DEBUG, logMessage + DELETE + CLOSE_BRACKET)
                .to(MOBILE_OFFICE_DELETE_TOPIC);
    }

}
