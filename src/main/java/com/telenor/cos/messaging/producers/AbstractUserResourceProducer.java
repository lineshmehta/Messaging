package com.telenor.cos.messaging.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.UserResource;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Producer for UserResourceDelete Event.
 *
 * @author Babaprakash D
 */
@Component
public abstract class AbstractUserResourceProducer extends AbstractProducer {

    @Autowired
    private ResourceCache resourceCache;

    /**
     * Creates UserResource Based on resourceId and csUserId.
     * @param resourceId resourceId.
     * @param csUserId csUserId.
     * @return UserResource.
     */
    protected UserResource createUserResource(Long resourceId,String csUserId) {
        UserResource userResource = new UserResource();
        Resource resource = new Resource(resourceId);
        resource.setResourceId(resourceId);
        CachableResource cachableResource = resourceCache.get(resourceId);
        if (cachableResource != null) {
            resource.setResourceHasContentInherit("Y".equals(cachableResource.getResourceHasContentInherit()));
            resource.setResourceHasStructureInherit("Y".equals(cachableResource.getResourceHasStructureInherit()));
            resource.setResourceTypeId(cachableResource.getResourceTypeId());//ResourceType in graph is determined from ResourceTypeId.
            resource.setResourceTypeIdKey(cachableResource.getResourceTypeIdKey());//This field is same as resourceId in UserAccess domain object in graph.
        } else {
            throw new CosMessagingException("Could not find a cachableResource for ResourceId [" + resourceId +"]. UserResource Message with CsUserId ["+ csUserId +"] And ResourceId [" + resourceId +"] is failed to update", null);
        }
        userResource.setUserId(csUserId);
        userResource.setResource(resource);
        return userResource;
    }
}
