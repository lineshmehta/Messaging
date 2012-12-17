package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.resource.ResourceTypeIdKeyUpdateEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Event Handler for {@link ResourceTypeIdKeyUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class ResourceTypeIdKeyUpdateHandler {

    @Autowired
    private ResourceCache resourceCache;

    private static final Logger LOG = LoggerFactory.getLogger(ResourceTypeIdKeyUpdateHandler.class);

    /**
     * Handles ResourceTypeIdKeyUpdateEvent.
     * @param resourceTypeIdKeyUpdateEvent resourceTypeIdKeyUpdateEvent.
     */
    public void handle(ResourceTypeIdKeyUpdateEvent resourceTypeIdKeyUpdateEvent) {
        Long resourceId = resourceTypeIdKeyUpdateEvent.getDomainId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [ResourceTypeIdKeyUpdateEvent] with [resourceId] as ["+ resourceId +"]");
        }
        CachableResource cachableResource = resourceCache.get(resourceId);
        if (cachableResource != null) {
            cachableResource.setResourceTypeIdKey(resourceTypeIdKeyUpdateEvent.getResourceTypeIdKey());
            resourceCache.insert(cachableResource.getResourceId(), cachableResource);
        } else {
            LOG.error("ResourceTypeIdKeyUpdateEvent with [resourceId] as [" + resourceId + "] has [resource] as [null]");
        }
    }
}
