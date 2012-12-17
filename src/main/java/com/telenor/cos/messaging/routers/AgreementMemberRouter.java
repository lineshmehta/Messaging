package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.constants.EndPointUri.AGREEMENT_MEMBER_LOGICAL_DELETE_TOPIC;
import static com.telenor.cos.messaging.constants.EndPointUri.AGREEMENT_MEMBER_NEW_TOPIC;
import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;
import static com.telenor.cos.messaging.event.Event.ACTION.LOGICAL_DELETE;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.constants.EndPointUri;

/**
 * Router for AgreementMember Events.
 */
@Component
public class AgreementMemberRouter extends StandardRouter {

    private static final String logMessage = "Successfully processed an Agreement Member Event of Type [";

    @Override
    public ChoiceDefinition whenPart() throws Exception {
        return from(EndPointUri.AGREEMENT_MEMBER_INCOMING_QUEUE)
                .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, logMessage + CREATED + CLOSE_BRACKET)
                .to(AGREEMENT_MEMBER_NEW_TOPIC)
                .when(isEventOfType(LOGICAL_DELETE)).log(LoggingLevel.DEBUG, logMessage + LOGICAL_DELETE + CLOSE_BRACKET)
                .to(AGREEMENT_MEMBER_LOGICAL_DELETE_TOPIC);
    }

}
