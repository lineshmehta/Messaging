package com.telenor.cos.messaging.jdbm.batch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.util.TestHelper;

/**
 * Test case for {@link PopulateAndVerifySubscriptionTypeCache}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3-batch.xml")
@DirtiesContext
public class PopulateAndVerifySubscriptionTypeCacheTest {

    private static final String csvFilePath = "csv/";

    private TestHelper testHelper = new TestHelper();

    private String[] listOfResourceIds = {"11","04011","87","04087","119"};

    @Autowired
    private PopulateAndVerifySubscriptionTypeCache populateAndVerifySubscriptionTypeCache;

    @Autowired
    private SubscriptionTypeCache subscriptionTypeCache;

    @Test
    public void testSubscriptionTypeCacheDataPopulation() throws Exception {
        for(int i = 0;i <listOfResourceIds.length;i++) {
            subscriptionTypeCache.remove(listOfResourceIds[i]);
        }
        populateAndVerifySubscriptionTypeCache.populateCache(testHelper.getFullPath(csvFilePath+JDBMUtils.SUBSCRIPTION_TYPE_CSV_FILENAME), 5);
        String productId = subscriptionTypeCache.get("11");
        assertEquals("Unexpected productId","100",productId);
    }
}