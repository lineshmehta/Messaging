package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.UserResource;
import com.telenor.cos.messaging.jdbm.CachableResource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractUserResourceProducerTest {

    public static final String CS_USER_ID = "cosmaster";
    public static final String CSUSERID_NEW = "cosmaster1";
    public static final Integer RESOURCE_TYPE_ID = 456;
    public static final String RESOURCE_TYPE_ID_KEY = "Superaccount";
    public static final String INHERIT_CONTENT = "N";
    public static final String INHERIT_STRUCTURE = "Y";

    public CachableResource createCachableResource(Long resourceId) {
        CachableResource cachableResource = new CachableResource(resourceId);
        cachableResource.setResourceHasContentInherit(INHERIT_CONTENT);
        cachableResource.setResourceHasStructureInherit(INHERIT_STRUCTURE);
        cachableResource.setResourceTypeId(RESOURCE_TYPE_ID);
        cachableResource.setResourceTypeIdKey(RESOURCE_TYPE_ID_KEY);
        return cachableResource;
    }

    public void assertUserResource(UserResource userResource) {
        Resource resource = userResource.getResource();
        assertEquals("Unexpected csuser Id", CS_USER_ID, userResource.getUserId());
        assertEquals("Unexpected Resource TypeId", RESOURCE_TYPE_ID, resource.getResourceTypeId());
        assertEquals("Unexpected Resource TypeId Key", RESOURCE_TYPE_ID_KEY, resource.getResourceTypeIdKey());
        assertFalse(resource.getResourceHasContentInherit());
        assertTrue(resource.getResourceHasStructureInherit());
    }
}
