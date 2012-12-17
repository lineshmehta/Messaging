package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.AGREEMENT_OWNER_NEW_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;

@Component
public class AgreementOwnerRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed an Agreement Owner Event of Type [";

    @Override
    public ChoiceDefinition whenPart() throws Exception {
        return from(EndPointUri.AGREEMENT_OWNER_INCOMING_QUEUE)
                .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
                .to(AGREEMENT_OWNER_NEW_TOPIC);
    }
}
