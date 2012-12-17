package com.telenor.cos.messaging.handlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.userresource.UserResourceResourceIdUpdateEvent;
import com.telenor.cos.messaging.jdbm.UserResourceCache;

/**
 * Handler for {@link UserResourceResourceIdUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class UserResourceResourceIdUpdateHandler {

    @Autowired
    private UserResourceCache userResourceCache;

    private static final Logger LOG = LoggerFactory.getLogger(UserResourceResourceIdUpdateHandler.class);

    /**
     * Handles UserResourceResourceIdUpdateEvent Event.
     * @param userResourceResourceIdUpdateEvent userResourceResourceIdUpdateEvent.
     */
    public void handle(UserResourceResourceIdUpdateEvent userResourceResourceIdUpdateEvent) {
        Long newResourceId = userResourceResourceIdUpdateEvent.getNewUserResource().getResource().getResourceId();
        Long oldResourceId = userResourceResourceIdUpdateEvent.getOldUserResource().getResource().getResourceId();
        String oldCsUserId = userResourceResourceIdUpdateEvent.getNewUserResource().getUserId();
        List<String> csUserIdsListForOldResourceId = userResourceCache.get(oldResourceId);
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [UserResourceResourceIdUpdateEvent] with [csUserId] as ["+ oldCsUserId +"]");
        }
        if(csUserIdsListForOldResourceId == null || csUserIdsListForOldResourceId.isEmpty()) {
            LOG.error("Received [UserResourceResourceIdUpdateEvent] with [csUserId] as ["+ oldCsUserId +"] but there are no [CSUsers] associated with this id!");
        } else {
            csUserIdsListForOldResourceId.remove(oldCsUserId);
            userResourceCache.insert(oldResourceId, csUserIdsListForOldResourceId);
        }
        List<String> csUserIdsListForNewResourceId = userResourceCache.get(newResourceId);
        if(csUserIdsListForNewResourceId == null || csUserIdsListForNewResourceId.isEmpty()) {
            csUserIdsListForNewResourceId = new ArrayList<String>();
        }
        csUserIdsListForNewResourceId.add(oldCsUserId);
        userResourceCache.insert(newResourceId, csUserIdsListForNewResourceId);
    }
}
