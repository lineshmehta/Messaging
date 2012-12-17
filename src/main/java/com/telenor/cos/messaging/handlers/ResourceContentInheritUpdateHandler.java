package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.resource.ResourceContentInheritUpdateEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Event Handler for {@link ResourceContentInheritUpdateEvent}.
 * @author Babaprakash D
 *
 */
@Component
public class ResourceContentInheritUpdateHandler {

    @Autowired
    private ResourceCache resourceCache;

    private static final Logger LOG = LoggerFactory.getLogger(ResourceContentInheritUpdateHandler.class);

    /**
     * Handles ResourceContentInheritUpdateEvent.
     * @param resourceContentInheritUpdateEvent resourceContentInheritUpdateEvent.
     */
    public void handle(ResourceContentInheritUpdateEvent resourceContentInheritUpdateEvent) {
        Long resourceId = resourceContentInheritUpdateEvent.getDomainId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [ResourceContentInheritUpdateEvent] with [resourceId] as ["+ resourceId +"]");
        }
        CachableResource cachableResource = resourceCache.get(resourceId);
        if (cachableResource != null) {
            String resourceContentInherit = resourceContentInheritUpdateEvent.getResourceHasContentInherit() ? "Y":"N";
            cachableResource.setResourceHasContentInherit(resourceContentInherit);
            resourceCache.insert(cachableResource.getResourceId(), cachableResource);
        } else {
            LOG.error("ResourceContentInheritUpdateEvent with [resourceId] as [" + resourceId + "] has [resource] as [null]");
        }
    }
}
