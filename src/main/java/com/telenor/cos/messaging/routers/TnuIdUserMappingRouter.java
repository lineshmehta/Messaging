package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.TNUIDUSERMAPPING_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.TNUIDUSERMAPPING_INCOMING_QUEUE;
import static com.telenor.cos.messaging.constants.EndPointUri.TNUIDUSERMAPPING_NEW_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.DELETE;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

@Component
public class TnuIdUserMappingRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed an TnuId-User Mapping Event of Type [";

    /**
     * Configures the TnuIdUserMappingRouter.
     *
     * @return the choice definition
     * @throws Exception if error
     */
    @Override
    protected ChoiceDefinition whenPart() throws Exception {
        return from(TNUIDUSERMAPPING_INCOMING_QUEUE)
           .choice()
           .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
           .to(TNUIDUSERMAPPING_NEW_TOPIC)
           .when(isEventOfType(DELETE)).log(LoggingLevel.DEBUG, logMessage + DELETE + CLOSE_BRACKET)
           .to(TNUIDUSERMAPPING_DELETE_TOPIC);
    }
}
