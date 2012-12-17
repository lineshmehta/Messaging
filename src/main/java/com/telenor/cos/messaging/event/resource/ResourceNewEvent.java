package com.telenor.cos.messaging.event.resource;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;

/**
 * Event for New Insert in RESOURCE table.
 * @author Babaprakash D
 *
 */
public class ResourceNewEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1029388457307112603L;

    private Resource resource;

    /**
     * Constructor for ResourceNewEvent.
     * @param resource resource.
     */
    public ResourceNewEvent(Resource resource) {
        super(resource.getResourceId(),ACTION.CREATED,TYPE.RESOURCE);
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
