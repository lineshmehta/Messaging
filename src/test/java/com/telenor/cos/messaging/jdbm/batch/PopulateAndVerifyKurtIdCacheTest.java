package com.telenor.cos.messaging.jdbm.batch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.messaging.jdbm.KurtIdCache;
import com.telenor.cos.messaging.util.TestHelper;

/**
 * Test case for {@link PopulateAndVerifyKurtIdCache}
 * @author t808074
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3-batch.xml")
@DirtiesContext
public class PopulateAndVerifyKurtIdCacheTest {

    private static final String csvFilePath = "csv/";

    private TestHelper testHelper = new TestHelper();

    private Long[] listOfKurtIds = {Long.valueOf(170161),Long.valueOf(954411),Long.valueOf(1076437),Long.valueOf(1139012),Long.valueOf(1143716)};

    @Autowired
    private PopulateAndVerifyKurtIdCache populateAndVerifyKurtIdCache;

    @Autowired
    private KurtIdCache kurtIdCache;

    @Test
    public void testMasterCustomerCacheDataPopulation() throws Exception {
        for(int i = 0;i <listOfKurtIds.length;i++) {
            kurtIdCache.remove(listOfKurtIds[i]);
        }
        long noOfInsertedRecords = populateAndVerifyKurtIdCache.populateCache(testHelper.getFullPath(csvFilePath+JDBMUtils.MASTER_CUSTOMER_KURT_DETAILS_CSV_FILENAME), 5);
        assertEquals("No of Inserted Records",5,noOfInsertedRecords);
    }

    @Test
    public void verifyInsertedRecords() throws Exception {
        Long masterCustomerId = kurtIdCache.get(Long.valueOf(170161));
        assertEquals("Unexpected MasterCustomerId",Long.valueOf(1292055),masterCustomerId);
    }
}
