package com.telenor.cos.messaging.event.userresource;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.UserResource;

/**
 * Event for Delete in USER_RESOURCE table.
 * @author Babaprakash D
 *
 */
public class UserResourceDeleteEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;
    
    private UserResource userResource;
    
    /**
     * Creates UserResource Delete Event.
     * @param userResource userResource.
     */
    public UserResourceDeleteEvent(UserResource userResource) {
        super(userResource.getResource().getResourceId(), ACTION.DELETE, TYPE.USERRESOURCE);
        this.userResource = userResource;
    }
    
    public UserResource getUserResource() {
        return userResource;
    }
}