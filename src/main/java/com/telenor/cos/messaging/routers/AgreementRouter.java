package com.telenor.cos.messaging.routers;

import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;

import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.Event.ACTION;

/**
 * Router for Agreement Events.
 */
@Component
public class AgreementRouter extends StandardActionRouter {

    protected String getDomainName() {
        return "Agreement";
    }
    
    protected ActionQueueMapping[] getActions() {
        return new ActionQueueMapping[] {
                new ActionQueueMapping(CREATED, "New"), 
                new ActionQueueMapping(ACTION.LOGICAL_DELETE, "LogicalDelete")
        };
    }

}
