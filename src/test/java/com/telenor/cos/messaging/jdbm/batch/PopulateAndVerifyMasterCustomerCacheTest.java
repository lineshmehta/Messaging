package com.telenor.cos.messaging.jdbm.batch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.messaging.jdbm.MasterCustomerCache;
import com.telenor.cos.messaging.util.TestHelper;

/**
 * Test case for {@link PopulateAndVerifyMasterCustomerCache}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3-batch.xml")
@DirtiesContext
public class PopulateAndVerifyMasterCustomerCacheTest {

    private static final String csvFilePath = "csv/";

    private TestHelper testHelper = new TestHelper();

    private Long[] listOfOrgNums = {Long.valueOf(170161),Long.valueOf(954411),Long.valueOf(1076437),Long.valueOf(1139012),Long.valueOf(1143716)};

    @Autowired
    private PopulateAndVerifyMasterCustomerCache populateAndVerifyMasterCustomerCache;

    @Autowired
    private MasterCustomerCache masterCustomerCache;

    @Test
    public void testMasterCustomerCacheDataPopulation() throws Exception {
        for(int i = 0;i <listOfOrgNums.length;i++) {
            masterCustomerCache.remove(listOfOrgNums[i]);
        }
        long noOfInsertedRecords = populateAndVerifyMasterCustomerCache.populateCache(testHelper.getFullPath(csvFilePath+JDBMUtils.MASTER_CUSTOMER_DETAILS_CSV_FILENAME), 5);
        assertEquals("No of Inserted Records",5,noOfInsertedRecords);
    }

    @Test
    public void verifyInsertedRecords() throws Exception {
        Long masterCustomerId = masterCustomerCache.get(Long.valueOf(170161));
        assertEquals("Unexpected MasterCustomerId",Long.valueOf(1292055),masterCustomerId);
    }
}