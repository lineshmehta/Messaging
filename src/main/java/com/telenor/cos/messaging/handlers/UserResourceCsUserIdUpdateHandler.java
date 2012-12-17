package com.telenor.cos.messaging.handlers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.userresource.UserResourceCsUserIdUpdateEvent;
import com.telenor.cos.messaging.jdbm.UserResourceCache;

/**
 * Handler for {@link UserResourceCsUserIdUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class UserResourceCsUserIdUpdateHandler {

    @Autowired
    private UserResourceCache userResourceCache;

    private static final Logger LOG = LoggerFactory.getLogger(UserResourceCsUserIdUpdateHandler.class);

    /**
     * Handles UserResourceCsUserIdUpdateEvent Event.
     * @param userResourceCsUserIdUpdateEvent userResourceCsUserIdUpdateEvent.
     */
    public void handle(UserResourceCsUserIdUpdateEvent userResourceCsUserIdUpdateEvent) {
        Long oldResourceId = userResourceCsUserIdUpdateEvent.getUserResource().getResource().getResourceId();
        String newCsUserId = userResourceCsUserIdUpdateEvent.getUserResource().getUserId();
        String oldCsUserId = userResourceCsUserIdUpdateEvent.getOldCsUserId();
        List<String> csUserIdsListForOldResourceId = userResourceCache.get(oldResourceId);
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [UserResourceCsUserIdUpdateEvent] with [csUserId] as ["+ newCsUserId +"] and [resourceId] as [" + oldResourceId + "]");
        }
        if(csUserIdsListForOldResourceId == null || csUserIdsListForOldResourceId.isEmpty()) {
            LOG.error("Received [UserResourceCsUserIdUpdateEvent] with [csUserId] as ["+ newCsUserId +"] but there are no [CSUsers] associated with it's resource id [" + oldResourceId + "]");
        } else {
            if(csUserIdsListForOldResourceId.contains(oldCsUserId)) {
                csUserIdsListForOldResourceId.remove(oldCsUserId);
                csUserIdsListForOldResourceId.add(newCsUserId);
            }
            userResourceCache.insert(oldResourceId, csUserIdsListForOldResourceId);
        }
    }
}
