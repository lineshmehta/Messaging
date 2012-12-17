package com.telenor.cos.messaging.event.userresource;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.UserResource;

/**
 * Event for RESOURCE_ID column change in USER_RESOURCE table.
 * @author Per Jørgen Walstrøm
 *
 */
public class UserResourceResourceIdUpdateEvent extends Event {

    private static final long serialVersionUID = -1L;

    private UserResource newUserResource;
    private UserResource oldUserResource;
    

    /**
     * Creates UserResourceResourceIdUpdateEvent.
     *
     * @param newUserResource newResource for csUserId.
     * @param oldUserResource oldResource for csUserId.
     */
    public UserResourceResourceIdUpdateEvent(UserResource newUserResource,UserResource oldUserResource) {
        super(null, ACTION.RESOURCE_ID_CHANGE, Event.TYPE.USERRESOURCE);
        this.newUserResource = newUserResource;
        this.oldUserResource = oldUserResource;
    }

    public UserResource getNewUserResource() {
        return newUserResource;
    }

    public UserResource getOldUserResource() {
        return oldUserResource;
    }
}
