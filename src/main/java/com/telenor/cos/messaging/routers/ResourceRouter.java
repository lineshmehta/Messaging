package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.RESOURCE_LOGICAL_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.RESOURCE_NEW_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.RESOURCE_STRUCTURE_INHERIT_UPDATE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.RESOURCE_TYPE_ID_KEY_UPDATE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.RESOURCE_TYPE_ID_UPDATE_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CONTENT_INHERIT_UPDATE;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.LOGICAL_DELETE;
import static com.telenor.cos.messaging.event.Event.ACTION.STRUCTURE_INHERIT_UPDATE;
import static com.telenor.cos.messaging.event.Event.ACTION.TYPE_ID_KEY_UPDATE;
import static com.telenor.cos.messaging.event.Event.ACTION.TYPE_ID_UPDATE;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;

/**
 * Router for Resource Events.
 * @author Babaprakash D
 */
@Component
public class ResourceRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed a Resource Event of Type [";

    /**
     * Configures the ResourceRouter.
     * @return the choice definition
     * @throws Exception if error
     */
    @Override
    public ChoiceDefinition whenPart() throws Exception {
       return from(EndPointUri.RESOURCE_INCOMING_QUEUE)
                .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
                .to(RESOURCE_NEW_TOPIC)
                .when(isEventOfType(TYPE_ID_UPDATE)).log(LoggingLevel.DEBUG, logMessage + TYPE_ID_UPDATE + CLOSE_BRACKET)
                .to(RESOURCE_TYPE_ID_UPDATE_TOPIC)
                .when(isEventOfType(TYPE_ID_KEY_UPDATE)).log(LoggingLevel.DEBUG, logMessage + TYPE_ID_KEY_UPDATE + CLOSE_BRACKET)
                .to(RESOURCE_TYPE_ID_KEY_UPDATE_TOPIC)
                .when(isEventOfType(CONTENT_INHERIT_UPDATE)).log(LoggingLevel.DEBUG, logMessage + CONTENT_INHERIT_UPDATE + CLOSE_BRACKET)
                .to(RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC)
                .when(isEventOfType(STRUCTURE_INHERIT_UPDATE)).log(LoggingLevel.DEBUG, logMessage + STRUCTURE_INHERIT_UPDATE + CLOSE_BRACKET)
                .to(RESOURCE_STRUCTURE_INHERIT_UPDATE_TOPIC)
                .when(isEventOfType(LOGICAL_DELETE)).log(LoggingLevel.DEBUG, logMessage + LOGICAL_DELETE + CLOSE_BRACKET)
                .to(RESOURCE_LOGICAL_DELETE_TOPIC);
    }
}