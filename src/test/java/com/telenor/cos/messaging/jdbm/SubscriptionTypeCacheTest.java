package com.telenor.cos.messaging.jdbm;

import static org.junit.Assert.assertEquals;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3.xml")
@DirtiesContext
public class SubscriptionTypeCacheTest {

    @Autowired
    private SubscriptionTypeCache subscriptionTypeCache;

    @Before
    public void setUp(){
        subscriptionTypeCache.clear();
    }

    @After
    public void delete() {
        subscriptionTypeCache.remove("1");
        subscriptionTypeCache.remove("2");
        subscriptionTypeCache.remove("3");
    }
    
    @Test
    public void testInsert() throws Exception{
        Map<String, String> subMap = new HashMap<String, String>();
        subMap.put("1", "ole");
        subMap.put("2", "dole");
        subMap.put("3", "doffen");
        subscriptionTypeCache.batchInsert(subMap);
        String name = subscriptionTypeCache.get("2");
        assertEquals("dole", name);
    }
}
