package com.telenor.cos.messaging.jdbm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test case for {@link ImsiCache}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3.xml")
@DirtiesContext
public class ImsiCacheTest {

    private static final String IMSI = "123456";
    private static final Long SUBSCRIPTION_ID = Long.valueOf(1);

    @Autowired
    private ImsiCache imsiCache;

    @Before
    public void setUp() {
        imsiCache.clear();
    }

    @Test
    public void testHappyDay() throws Exception {
        imsiCache.insert(SUBSCRIPTION_ID, IMSI);
        assertEquals(IMSI, imsiCache.get(SUBSCRIPTION_ID));

        imsiCache.remove(SUBSCRIPTION_ID);
        assertNull(imsiCache.get(SUBSCRIPTION_ID));
    }
}
