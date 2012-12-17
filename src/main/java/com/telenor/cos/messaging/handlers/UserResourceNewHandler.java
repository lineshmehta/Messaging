package com.telenor.cos.messaging.handlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.userresource.UserResourceNewEvent;
import com.telenor.cos.messaging.jdbm.UserResourceCache;

/**
 * Handler for {@link UserResourceNewEvent}
 * @author Babaprakash D
 *
 */
@Component
public class UserResourceNewHandler {

    @Autowired
    private UserResourceCache userResourceCache;

    private static final Logger LOG = LoggerFactory.getLogger(UserResourceNewHandler.class);

    /**
     * Handles UserResource New Event.
     * @param userResourceNewEvent new UserResource.
     */
    public void handle(UserResourceNewEvent userResourceNewEvent) {
        Long resourceId = userResourceNewEvent.getUserResource().getResource().getResourceId();
        String csUserId = userResourceNewEvent.getUserResource().getUserId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [UserResourceNewEvent] with [resourceId] as ["+ resourceId +"]");
        }
        List<String> csUserIdsListForResourceId = userResourceCache.get(resourceId);
        if(csUserIdsListForResourceId == null || csUserIdsListForResourceId.isEmpty()) {
            List<String> csUserIdsList = new ArrayList<String>();
            csUserIdsList.add(csUserId);
            userResourceCache.insert(resourceId, csUserIdsList);
        } else {
            csUserIdsListForResourceId.add(csUserId);
            userResourceCache.insert(resourceId, csUserIdsListForResourceId);
        }
    }
}
