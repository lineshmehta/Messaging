package com.telenor.cos.messaging.jdbm.batch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.messaging.jdbm.ImsiCache;
import com.telenor.cos.messaging.util.TestHelper;

/**
 * Test case for PopulateAndVerifyImsiCache
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3-batch.xml")
@DirtiesContext
public class PopulateAndVerifyImsiCacheTest {

    private static final String rootPath = "csv/";

    private TestHelper testHelper = new TestHelper();

    @Autowired
    private PopulateAndVerifyImsiCache populateAndVerifyImsiCache;

    @Autowired
    private ImsiCache imsiCache;

    @Test
    public void testCustomerCacheDataPopulation() throws Exception {
        imsiCache.clear();

        String path = testHelper.getFullPath(rootPath + JDBMUtils.SUBSCRIPTION_IMSI_CSV_FILENAME);
        long noOfInsertedRecords = populateAndVerifyImsiCache.populateCache(path, 1000);
        assertEquals("No of Inserted Records", 10, noOfInsertedRecords);

        populateAndVerifyImsiCache.verifyJdbmDataPopulation(path, noOfInsertedRecords);
    }

}
