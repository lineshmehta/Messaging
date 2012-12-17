package com.telenor.cos.messaging.handlers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.userresource.UserResourceDeleteEvent;
import com.telenor.cos.messaging.jdbm.UserResourceCache;

/**
 * Handler for {@link UserResourceDeleteEvent}
 * @author Babaprakash D
 *
 */
@Component
public class UserResourceDeleteHandler {

    @Autowired
    private UserResourceCache userResourceCache;

    private static final Logger LOG = LoggerFactory.getLogger(UserResourceDeleteHandler.class);

    /**
     * Handles UserResource Delete Event.
     * @param userResourceDeleteEvent userResourceDeleteEvent.
     */
    public void handle(UserResourceDeleteEvent userResourceDeleteEvent) {
        Long resourceId = userResourceDeleteEvent.getUserResource().getResource().getResourceId();
        String csUserId = userResourceDeleteEvent.getUserResource().getUserId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [UserResourceDeleteEvent] with [resourceId] as ["+ resourceId +"]"); 
        }
        List<String> csUserIdsListForResourceId = userResourceCache.get(resourceId);
        if(csUserIdsListForResourceId == null || csUserIdsListForResourceId.isEmpty()) {
            LOG.error("Received [UserResourceDeleteEvent] with [resourceId] as ["+ resourceId +"] but there are no [CSUsers] associated with this id!");
        } else {
            csUserIdsListForResourceId.remove(csUserId);
            userResourceCache.insert(resourceId, csUserIdsListForResourceId);
        }
    }
}
