package com.telenor.cos.messaging.jdbm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test case for {@link ResourceCache}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3.xml")
@DirtiesContext
public class ResourceCacheTest {

    @Autowired
    private ResourceCache resourceCache;

    @Before
    public void setUp() {
        resourceCache.clear();
    }

    @After
    public void delete() {
        resourceCache.remove(1L);
        resourceCache.remove(666L);
        resourceCache.remove(777L);
    }

    @Test
    public void testInsert() throws Exception {
        CachableResource resource = new CachableResource(1L);
        resource.setResourceTypeId(0);
        resource.setResourceTypeIdKey("442867");
        resource.setResourceHasContentInherit("Y");
        resource.setResourceHasStructureInherit("N");
        resourceCache.insert(resource.getResourceId(), resource);
        CachableResource cachableResource = resourceCache.get(1L);
        assertNotNull(cachableResource);
        assertEquals(Integer.valueOf(0), cachableResource.getResourceTypeId());
        assertEquals("442867", cachableResource.getResourceTypeIdKey());
        assertEquals("Y", cachableResource.getResourceHasContentInherit());
        assertEquals("N", cachableResource.getResourceHasStructureInherit());
    }

    @Test
    public void testDetachedObject() throws Exception {
        CachableResource resource = new CachableResource(666L);
        CachableResource resource2 = new CachableResource(777L);
        resourceCache.insert(resource.getResourceId(), resource);
        CachableResource resource3 = resourceCache.get(666L);
        resourceCache.insert(777L, resource2);
        CachableResource resource4 = resourceCache.get(666L);
        assertTrue(resource4 != resource3);
    }

    @Test
    public void testThreads() throws Exception {
        int j = 30000;
        Map<Long, CachableResource> tempMap = new HashMap<Long, CachableResource>();
        for (int i = 1; i <= j; i++) {
            CachableResource resource = new CachableResource(Long.valueOf(i));
            tempMap.put(Long.valueOf(i), resource);

            if (i % 5000 == 0) {
                resourceCache.batchInsert(tempMap);
                tempMap.clear();
            }

        }
        assertEquals(j, resourceCache.size());
    }

    @Test
    public void testUpdate() throws Exception {
        CachableResource resource1 = new CachableResource(1L);
        resource1.setResourceTypeId(0);
        resource1.setResourceTypeIdKey("442867");
        resource1.setResourceHasContentInherit("Y");
        resource1.setResourceHasStructureInherit("N");
        CachableResource resource2 = new CachableResource(1L);
        resource2.setResourceTypeId(0);
        resource2.setResourceTypeIdKey("442867");
        resource2.setResourceHasContentInherit("Y");
        resource2.setResourceHasStructureInherit("N");
        resourceCache.insert(resource1.getResourceId(), resource1);
        resourceCache.insert(resource2.getResourceId(), resource2);
        CachableResource outputResource = resourceCache.get(1L);
        assertEquals("442867", outputResource.getResourceTypeIdKey());
    }

    @Test
    public void testRemove() throws Exception {
        CachableResource resource1 = new CachableResource(1L);
        resource1.setResourceTypeId(0);
        resource1.setResourceTypeIdKey("442867");
        resource1.setResourceHasContentInherit("Y");
        resource1.setResourceHasStructureInherit("N");
        resourceCache.insert(resource1.getResourceId(), resource1);
        CachableResource outputResource = resourceCache.get(1L);
        assertNotNull(outputResource);
        resourceCache.remove(1L);
        CachableResource outputResource1 = resourceCache.get(1L);
        assertNull(outputResource1);
    }
}
