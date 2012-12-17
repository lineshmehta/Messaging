package com.telenor.cos.messaging.event.userresource;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.UserResource;

/**
 * Event for Insert Messages in USER_RESOURCE table.
 * @author Babaprakash D
 *
 */
public class UserResourceNewEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;
    
    private UserResource userResource;
    
    /**
     * Creates New UserResourceEvent.
     * @param userResource userResource.
     */
    public UserResourceNewEvent(UserResource userResource) {
        super(userResource.getResource().getResourceId(), ACTION.CREATED, TYPE.USERRESOURCE);
        this.userResource = userResource;
    }

    public UserResource getUserResource() {
        return userResource;
    }
}
