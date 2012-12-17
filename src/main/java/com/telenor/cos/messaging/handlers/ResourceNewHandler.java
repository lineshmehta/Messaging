package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceNewEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Handler for {@link ResourceNewEvent}
 * @author Babaprakash D
 *
 */
@Component
public class ResourceNewHandler {

    @Autowired
    private ResourceCache resourceCache;

    private static final Logger LOG = LoggerFactory.getLogger(ResourceNewHandler.class);

    /**
     * Handles ResourceNewEvent.
     * @param resourceNewEvent resourceNewEvent.
     */
    public void handle(ResourceNewEvent resourceNewEvent) {
        Long resourceId = resourceNewEvent.getDomainId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [ResourceNewEvent] with [resourceId] as ["+ resourceId+"]");
        }
        CachableResource cachableResource = toCachableResource(resourceNewEvent.getResource());
            resourceCache.insert(cachableResource.getResourceId(), cachableResource);
    }

    private CachableResource toCachableResource(Resource resource) {
        CachableResource cachableResource = new CachableResource(resource.getResourceId());
        cachableResource.setResourceTypeId(resource.getResourceTypeId());
        cachableResource.setResourceTypeIdKey(resource.getResourceTypeIdKey());
        cachableResource.setResourceHasContentInherit((resource.getResourceHasContentInherit() ? "Y" :"N"));
        cachableResource.setResourceHasStructureInherit((resource.getResourceHasStructureInherit() ? "Y" :"N"));
        return cachableResource;
    }
}
