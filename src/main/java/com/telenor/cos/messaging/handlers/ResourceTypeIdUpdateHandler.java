package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.resource.ResourceTypeIdUpdateEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Event Handler for {@link ResourceTypeIdUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class ResourceTypeIdUpdateHandler {

    @Autowired
    private ResourceCache resourceCache;

    private static final Logger LOG = LoggerFactory.getLogger(ResourceTypeIdUpdateHandler.class);

    /**
     * Handles ResourceTypeIdUpdateEvent.
     * @param resourceTypeIdUpdateEvent resourceTypeIdUpdateEvent.
     */
    public void handle(ResourceTypeIdUpdateEvent resourceTypeIdUpdateEvent) {
        Long resourceId = resourceTypeIdUpdateEvent.getDomainId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [ResourceTypeIdUpdateEvent] with [resourceId] as ["+ resourceId +"]");
        }
        CachableResource cachableResource = resourceCache.get(resourceId);
        if (cachableResource != null) {
            cachableResource.setResourceTypeId(resourceTypeIdUpdateEvent.getResourceTypeId());
            resourceCache.insert(cachableResource.getResourceId(), cachableResource);
        } else {
            LOG.error("ResourceTypeIdUpdateEvent with [resourceId] as [" + resourceId + "] has [resource] as [null]");
        }
    }
}