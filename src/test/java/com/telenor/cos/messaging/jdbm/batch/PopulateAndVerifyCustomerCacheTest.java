package com.telenor.cos.messaging.jdbm.batch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.util.TestHelper;

/**
 *
 * Test case for {@link PopulateAndVerifyCustomerCache}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3-batch.xml")
@DirtiesContext
public class PopulateAndVerifyCustomerCacheTest {

    private static final String customerCsvFilePath = "csv/";

    private Long[] listOfCustomerIds = {Long.valueOf(6),Long.valueOf(7),Long.valueOf(10),Long.valueOf(33),Long.valueOf(8004420)};

    private TestHelper testHelper = new TestHelper();

    @Autowired
    private PopulateAndVerifyCustomerCache populateAndVerifyCustomerCache;

    @Autowired
    private CustomerCache customerCache;

    @Test
    public void testCustomerCacheDataPopulation() throws Exception {
        for(int i = 0;i <listOfCustomerIds.length;i++) {
            customerCache.remove(listOfCustomerIds[i]);
        }
        long noOfInsertedRecords = populateAndVerifyCustomerCache.populateCache(testHelper.getFullPath(customerCsvFilePath+JDBMUtils.CUSTOMER_DETAILS_CSV_FILENAME), 5);
        assertEquals("No of Inserted Records",5,noOfInsertedRecords);
    }

    @Test
    public void testInsertedRecords() throws Exception {
        CachableCustomer customer = customerCache.get(Long.valueOf(6));
        assertEquals("Unexpected MasterCustomerId",Long.valueOf(100000002),customer.getMasterCustomerId());
    }
}
