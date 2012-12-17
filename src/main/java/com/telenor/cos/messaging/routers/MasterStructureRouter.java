package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.MASTERSTRUCTURE_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.MASTERSTRUCTURE_NEW_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.MASTERSTRUCTURE_UPDATED_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.DELETE;
import static com.telenor.cos.messaging.event.Event.ACTION.UPDATED;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;

/**
 * Router for MasterStructure Events.
 * @author t798435
 *
 */
@Component
public class MasterStructureRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed a Master Structure Event of Type [";

    /**
     * Configures the MasterStructureRouter.
     * @return the choice definition
     *
     * @throws Exception if error
     */
    @Override
    public ChoiceDefinition whenPart() throws Exception {
        return from(EndPointUri.MASTERSTRUCTURE_INCOMING_QUEUE)
                .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
                .to(MASTERSTRUCTURE_NEW_TOPIC)
                .when(isEventOfType(UPDATED)).log(LoggingLevel.DEBUG, logMessage + UPDATED + CLOSE_BRACKET)
                .to(MASTERSTRUCTURE_UPDATED_TOPIC)
                .when(isEventOfType(DELETE)).log(LoggingLevel.DEBUG, logMessage + DELETE + CLOSE_BRACKET)
                .to(MASTERSTRUCTURE_DELETE_TOPIC);
    }
}
