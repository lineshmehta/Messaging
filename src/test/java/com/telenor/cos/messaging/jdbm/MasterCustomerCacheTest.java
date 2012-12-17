package com.telenor.cos.messaging.jdbm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
 * Test case for {@link MasterCustomerCache}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3.xml")
@DirtiesContext
public class MasterCustomerCacheTest {

    private static final Long ORG_NUM_ONE = Long.valueOf(4556789);
    private static final Long ORG_NUM_TWO = Long.valueOf(4556788);
    private static final Long ORG_NUM_THREE = Long.valueOf(4556787);
    private static final Long ORG_NUM_FOUR = Long.valueOf(4556786);
    private static final Long MASTER_ID_ONE = Long.valueOf(1);
    private static final Long MASTER_ID_TWO = Long.valueOf(2);
    private static final Long MASTER_ID_THREE = Long.valueOf(3);
    private static final Long MASTER_ID_FOUR = Long.valueOf(4);

    @Autowired
    private MasterCustomerCache masterCustomerCache;

    @Before
    public void setUp() {
        masterCustomerCache.clear();
    }

    @After
    public void delete() {
        masterCustomerCache.remove(ORG_NUM_ONE);
        masterCustomerCache.remove(ORG_NUM_TWO);
        masterCustomerCache.remove(ORG_NUM_THREE);
        masterCustomerCache.remove(ORG_NUM_FOUR);
    }

    @Test
    public void testMasterCustomerCache() throws Exception {
        //Tests Insert
        masterCustomerCache.insert(ORG_NUM_ONE, MASTER_ID_ONE);
        Long masterCustomerId = masterCustomerCache.get(ORG_NUM_ONE);
        assertEquals("Unexpected MasterCustomer Id",MASTER_ID_ONE,masterCustomerId);
        //Tests removal
        masterCustomerCache.remove(ORG_NUM_ONE);
        Long masterCustomerIdAfterRemoval = masterCustomerCache.get(ORG_NUM_ONE);
        assertNull(masterCustomerIdAfterRemoval);
        Map<Long,Long> masterCustomerCacheMap = new HashMap<Long,Long>();
        masterCustomerCacheMap.put(ORG_NUM_ONE, MASTER_ID_ONE);
        masterCustomerCacheMap.put(ORG_NUM_TWO, MASTER_ID_TWO);
        masterCustomerCacheMap.put(ORG_NUM_THREE, MASTER_ID_THREE);
        masterCustomerCacheMap.put(ORG_NUM_FOUR, MASTER_ID_FOUR);
        //Tests Batch Insert.
        masterCustomerCache.batchInsert(masterCustomerCacheMap);
        Long masterCustomerIdOne = masterCustomerCache.get(ORG_NUM_ONE);
        Long masterCustomerIdTwo = masterCustomerCache.get(ORG_NUM_TWO);
        Long masterCustomerIdThree = masterCustomerCache.get(ORG_NUM_THREE);
        Long masterCustomerIdFour = masterCustomerCache.get(ORG_NUM_FOUR);
        assertEquals("Unexpected MasterCustomer Id",MASTER_ID_ONE,masterCustomerIdOne);
        assertEquals("Unexpected MasterCustomer Id",MASTER_ID_TWO,masterCustomerIdTwo);
        assertEquals("Unexpected MasterCustomer Id",MASTER_ID_THREE,masterCustomerIdThree);
        assertEquals("Unexpected MasterCustomer Id",MASTER_ID_FOUR,masterCustomerIdFour);
    }
}
