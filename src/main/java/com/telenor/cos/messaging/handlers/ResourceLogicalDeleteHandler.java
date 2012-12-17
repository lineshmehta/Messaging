package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.resource.ResourceLogicalDeleteEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Handler for {@link ResourceLogicalDeleteEvent}
 * @author Babaprakash D
 *
 */
@Component
public class ResourceLogicalDeleteHandler {

    @Autowired
    private ResourceCache resourceCache;

    private static final Logger LOG = LoggerFactory.getLogger(ResourceLogicalDeleteHandler.class);

    /**
     * Handles ResourceLogicalDeleteEvent.
     * @param resourceLogicalDeleteEvent ResourceLogicalDeleteEvent.
     */
    public void handle(ResourceLogicalDeleteEvent resourceLogicalDeleteEvent) {
        Long resourceId = resourceLogicalDeleteEvent.getDomainId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [ResourceLogicalDeleteEvent] with [resourceId] as ["+ resourceId +"]");
        }
        CachableResource cachableResource = resourceCache.get(resourceId);
        if (cachableResource != null) {
            resourceCache.remove(cachableResource.getResourceId());
        } else {
            LOG.error("ResourceLogicalDeleteEvent with [resourceId] as [" + resourceId + "] has [resource] as [null]");
        }
    }
}
