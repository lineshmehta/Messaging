package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.USERRESOURCE_CSUSER_ID_UPDATE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.USERRESOURCE_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.USERRESOURCE_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.USERRESOURCE_NEW_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.USERRESOURCE_RESOURCE_ID_UPDATE_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.CS_USERID_CHANGE;
import static com.telenor.cos.messaging.event.Event.ACTION.DELETE;
import static com.telenor.cos.messaging.event.Event.ACTION.RESOURCE_ID_CHANGE;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

/**
 * Camel Router for UserResource Events.
 * @author Babaprakash D
 */
@Component
public class UserResourceRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed an UserResource Event of Type [";

    /**
     * Configures the UserResourceRouter.
     *
     * @return the choice definition
     * @throws Exception if error
     */
    @Override
    protected ChoiceDefinition whenPart() throws Exception {
        return from(USERRESOURCE_INCOMING_QUEUE)
                .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
                .to(USERRESOURCE_NEW_TOPIC)
                .when(isEventOfType(RESOURCE_ID_CHANGE)).log(LoggingLevel.DEBUG, logMessage + RESOURCE_ID_CHANGE + CLOSE_BRACKET)
                .to(USERRESOURCE_RESOURCE_ID_UPDATE_TOPIC)
                .when(isEventOfType(CS_USERID_CHANGE)).log(LoggingLevel.DEBUG, logMessage + CS_USERID_CHANGE + CLOSE_BRACKET)
                .to(USERRESOURCE_CSUSER_ID_UPDATE_TOPIC)
                .when(isEventOfType(DELETE)).log(LoggingLevel.DEBUG, logMessage + DELETE + CLOSE_BRACKET)
                .to(USERRESOURCE_DELETE_TOPIC);
    }
}
