package com.telenor.cos.messaging.event.userresource;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.UserResource;

/**
 * Event for CS_USER_ID column change in USER_RESOURCE table.
 * @author Babaprakash D
 *
 */
public class UserResourceCsUserIdUpdateEvent extends Event {

    /**
     *
     */
    private static final long serialVersionUID = -570560444364332150L;

    private String csUserIdOld;
    private UserResource userResource;

    /**
     * Constructor of UserResourceCsUserIdUpdateEvent.
     *
     * @param userResource userResource.
     * @param csUserIdOld csUserIdOld.
     */
    public UserResourceCsUserIdUpdateEvent(UserResource userResource, String csUserIdOld) {
        super(userResource.getResource().getResourceId(), ACTION.CS_USERID_CHANGE, Event.TYPE.USERRESOURCE);
        this.userResource = userResource;
        this.csUserIdOld = csUserIdOld;
    }

    public String getOldCsUserId() {
        return csUserIdOld;
    }

    public UserResource getUserResource() {
        return userResource;
    }
}