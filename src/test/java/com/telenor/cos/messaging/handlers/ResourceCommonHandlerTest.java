package com.telenor.cos.messaging.handlers;

import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.jdbm.CachableResource;

public class ResourceCommonHandlerTest {

    public static final Long RESOURCE_ID = Long.valueOf(678);

    protected CachableResource createCachableResource(Integer resourceTypeId,String resourceTypeIdKey,String contentInherit,String structureInherit) {
        CachableResource oldCachableResource = new CachableResource(RESOURCE_ID);
        oldCachableResource.setResourceTypeId(resourceTypeId);
        oldCachableResource.setResourceTypeIdKey(resourceTypeIdKey);
        oldCachableResource.setResourceHasContentInherit(contentInherit);
        oldCachableResource.setResourceHasStructureInherit(structureInherit);
        return oldCachableResource;
    }

    protected Resource createResource(Integer resourceTypeId,String resourceTypeIdKey,String contentInherit,String structureInherit) {
        Resource resource = new Resource(RESOURCE_ID);
        resource.setResourceHasContentInherit("Y".equals(contentInherit));
        resource.setResourceTypeId(resourceTypeId);
        resource.setResourceTypeIdKey(resourceTypeIdKey);
        resource.setResourceHasStructureInherit("Y".equals(structureInherit));
        return resource;
    }
}
