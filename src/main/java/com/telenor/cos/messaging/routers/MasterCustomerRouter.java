package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.MASTERCUSTOMER_LOGICAL_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.MASTERCUSTOMER_NAME_CHANGE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.MASTERCUSTOMER_NEW_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.LOGICAL_DELETE;
import static com.telenor.cos.messaging.event.Event.ACTION.NAME_CHANGE;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;

/**
 * Router for MasterCustomer Events.
 * @author Babaprakash D
 */
@Component
public class MasterCustomerRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed a Master Customer Event of Type [";

    /**
     * Configures the MasterCustomerRouter.
     * @return the choice definition
     *
     * @throws Exception if error
     */
    @Override
    public ChoiceDefinition whenPart() throws Exception {
        return from(EndPointUri.MASTERCUSTOMER_INCOMING_QUEUE)
                .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
                .to(MASTERCUSTOMER_NEW_TOPIC)
                .when(isEventOfType(NAME_CHANGE)).log(LoggingLevel.DEBUG, logMessage + NAME_CHANGE + CLOSE_BRACKET)
                .to(MASTERCUSTOMER_NAME_CHANGE_TOPIC)
                .when(isEventOfType(LOGICAL_DELETE)).log(LoggingLevel.DEBUG, logMessage + LOGICAL_DELETE + CLOSE_BRACKET)
                .to(MASTERCUSTOMER_LOGICAL_DELETE_TOPIC);
    }

}
