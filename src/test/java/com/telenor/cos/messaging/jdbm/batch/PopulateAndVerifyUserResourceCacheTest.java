package com.telenor.cos.messaging.jdbm.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.messaging.jdbm.UserResourceCache;
import com.telenor.cos.messaging.util.TestHelper;

/**
 * Test case for {@link PopulateAndVerifyUserResourceCache}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3-batch.xml")
@DirtiesContext
public class PopulateAndVerifyUserResourceCacheTest {

    private static final String csvFilePath = "csv/";

    private TestHelper testHelper = new TestHelper();

    private Long[] listOfResourceIds = {Long.valueOf(1),Long.valueOf(2),Long.valueOf(100114)};

    @Autowired
    private PopulateAndVerifyUserResourceCache populateAndVerifyUserResourceCache;

    @Autowired
    private UserResourceCache userResourceCache;

    @Test
    public void testUserResourceCacheDataPopulation() throws Exception {
        for(int i = 0;i <listOfResourceIds.length;i++) {
            userResourceCache.remove(listOfResourceIds[i]);
        }
        populateAndVerifyUserResourceCache.populateCache(testHelper.getFullPath(csvFilePath+JDBMUtils.USERRESOURCE_DETAILS_CSV_FILENAME), 5);
        List<String> csUserIdsListForResourceId1 = userResourceCache.get(Long.valueOf(1));
        List<String> csUserIdsListForResourceId2 = userResourceCache.get(Long.valueOf(2));
        List<String> csUserIdsListForResourceId3 = userResourceCache.get(Long.valueOf(100114));
        assertNotNull(csUserIdsListForResourceId1);
        assertNotNull(csUserIdsListForResourceId2);
        assertNotNull(csUserIdsListForResourceId3);
        assertEquals(3,csUserIdsListForResourceId1.size());
        assertEquals(2,csUserIdsListForResourceId2.size());
        assertEquals(10,csUserIdsListForResourceId3.size());
    }
}
