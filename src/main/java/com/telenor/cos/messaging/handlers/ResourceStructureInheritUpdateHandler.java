package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.resource.ResourceStructureInheritUpdateEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Event Handler for {@link ResourceStructureInheritUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class ResourceStructureInheritUpdateHandler {

    @Autowired
    private ResourceCache resourceCache;

    private static final Logger LOG = LoggerFactory.getLogger(ResourceStructureInheritUpdateHandler.class);

    /**
     * Handles ResourceStructureInheritUpdateEvent.
     * @param resourceStructureInheritUpdateEvent resourceStructureInheritUpdateEvent.
     */
    public void handle(ResourceStructureInheritUpdateEvent resourceStructureInheritUpdateEvent) {
        Long resourceId = resourceStructureInheritUpdateEvent.getDomainId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [ResourceStructureInheritUpdateEvent] with [resourceId] as ["+ resourceId +"]");
        }
        CachableResource cachableResource = resourceCache.get(resourceStructureInheritUpdateEvent.getDomainId());
        if (cachableResource != null) {
            String resourceStructureInherit = resourceStructureInheritUpdateEvent.getResourceHasStructureInherit() ? "Y":"N";
            cachableResource.setResourceHasStructureInherit(resourceStructureInherit);
            resourceCache.insert(cachableResource.getResourceId(), cachableResource);
        } else {
            LOG.error("ResourceStructureInheritUpdateEvent with [resourceId] as [" + resourceId + "] has [resource] as [null]");
        }
    }
}
