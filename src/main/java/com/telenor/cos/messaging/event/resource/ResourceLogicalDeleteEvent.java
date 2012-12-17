package com.telenor.cos.messaging.event.resource;

import com.telenor.cos.messaging.event.Event;

/**
 * Event for  column INFO_IS_DELETED change in RESOURCE.
 * @author Babaprakash D
 *
 */
public class ResourceLogicalDeleteEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    /**
     * Constructor of ResourceLogicalDeleteEvent.
     * @param resourceId resourceId.
     */
    public ResourceLogicalDeleteEvent(Long resourceId) {
        super(resourceId, ACTION.LOGICAL_DELETE, TYPE.RESOURCE);
    }
}
