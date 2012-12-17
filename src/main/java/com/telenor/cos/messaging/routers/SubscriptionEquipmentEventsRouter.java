package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_EQUPMENT_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_EQUPMENT_NEW_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.SUBSCRIPTION_EQUPMENT_UPDATE_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.LOGICAL_DELETE;
import static com.telenor.cos.messaging.event.Event.ACTION.UPDATED;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;

@Component
public class SubscriptionEquipmentEventsRouter extends StandardXmlFormattingRouter {

    /**
     * Configures the SubscriptionEquipmentRouter.
     *
     * @return the choice definition for this offer
     * @throws Exception if error
     */
    @Override
    protected ChoiceDefinition whenPart() throws Exception {
        return from(EndPointUri.SUBSCRIPTION_EQUPMENT_INCOMING_QUEUE)
                .choice()
                    .when(isEventOfType(CREATED))
                    .log(LoggingLevel.INFO, "Got a event of type: " + CREATED)
                    .to(SUBSCRIPTION_EQUPMENT_NEW_TOPIC)

                    .when(isEventOfType(UPDATED))
                    .log(LoggingLevel.INFO, "Got a event of type: " + UPDATED)
                    .to(SUBSCRIPTION_EQUPMENT_UPDATE_TOPIC)

                    .when(isEventOfType(LOGICAL_DELETE))
                    .log(LoggingLevel.INFO, "Got a event of type: " + LOGICAL_DELETE)
                    .to(SUBSCRIPTION_EQUPMENT_DELETE_TOPIC);
    }

}
