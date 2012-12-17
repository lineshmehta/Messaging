package com.telenor.cos.messaging.jdbm.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;
import com.telenor.cos.messaging.util.TestHelper;

/**
 *
 * Test case for {@link PopulateAndVerifyResourceCache}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3-batch.xml")
@DirtiesContext
public class PopulateAndVerifyResourceCacheTest {

    private static final String csvFilePath = "csv/";

    private TestHelper testHelper = new TestHelper();

    private Long[] listOfResourceIds = {Long.valueOf(7),Long.valueOf(8),Long.valueOf(13),Long.valueOf(14),Long.valueOf(15)};

    @Autowired
    private PopulateAndVerifyResourceCache populateAndVerifyResourceCache;

    @Autowired
    private ResourceCache resourceCache;

    @Test
    public void testResourceCacheDataPopulation() throws Exception {
        for(int i = 0;i <listOfResourceIds.length;i++) {
            resourceCache.remove(listOfResourceIds[i]);
        }
        populateAndVerifyResourceCache.populateCache(testHelper.getFullPath(csvFilePath+JDBMUtils.RESOURCE_DETAILS_CSV_FILENAME), 5);
        CachableResource resource = resourceCache.get(Long.valueOf(7));
        assertNotNull(resource);
        assertEquals("Unexpected ResourceId",Long.valueOf(7),resource.getResourceId());
        assertEquals("Unexpected ResourceType Id",Integer.valueOf(2),resource.getResourceTypeId());
        assertEquals("Unexpected ResourceTypeIdKey","44671",resource.getResourceTypeIdKey());
        assertEquals("Unexpected ResourceContent Inherit","Y",resource.getResourceHasContentInherit());
        assertEquals("Unexpected Resource StructureInherit","N",resource.getResourceHasStructureInherit());
    }
}
