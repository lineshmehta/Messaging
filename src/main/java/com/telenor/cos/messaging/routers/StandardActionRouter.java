package com.telenor.cos.messaging.routers;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.ChoiceDefinition;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event.ACTION;

public abstract class StandardActionRouter extends StandardRouter {

    protected static class ActionQueueMapping {
        private ACTION action;
        private String queueName;

        ActionQueueMapping(ACTION action, String queueName) {
            this.action = action;
            this.queueName = queueName;
        }
    }

    @Override
    public ChoiceDefinition whenPart() throws Exception {
        String domainName = getDomainName();
        ChoiceDefinition choice = from(EndPointUri.INCOMING_QUEUE + "." + domainName.toLowerCase()).choice();
        ActionQueueMapping[] actions = getActions();
        for (ActionQueueMapping actionMapping : actions) {
            choice = choice
                    .when(isEventOfType(actionMapping.action)).log(LoggingLevel.DEBUG, "Recieved a [" + domainName + "] of type [" + actionMapping.action + "]. This will be sent to [" + actionMapping.queueName + "]")
                    .to("activemq:topic:VirtualTopic." + domainName + "." + actionMapping.queueName);
        }

        return choice;
    }

    /**
     * @return the domain name
     */
    protected abstract String getDomainName();

    /**
     * @return the allowed actions
     */
    protected abstract ActionQueueMapping[] getActions();
}