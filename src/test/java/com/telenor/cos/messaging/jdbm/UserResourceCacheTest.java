package com.telenor.cos.messaging.jdbm;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test case for {@link UserResourceCache}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3.xml")
@DirtiesContext
public class UserResourceCacheTest {

    @Autowired
    private UserResourceCache userResourceCache;

    private static final Long RESOURCE_ID = Long.valueOf(1);

    @Before
    public void setUp() {
        userResourceCache.clear();
    }

    @After
    public void delete() {
        userResourceCache.remove(RESOURCE_ID);
    }

    @Test
    public void testUserResourceCache() throws Exception {
        List<String> csUserIdsList = new ArrayList<String>();
        csUserIdsList.add("test");
        csUserIdsList.add("test1");
        csUserIdsList.add("test2");
        userResourceCache.insert(RESOURCE_ID, csUserIdsList);
        List<String> csUserIdsListForResourceId = userResourceCache.get(RESOURCE_ID);
        for(String csUserId : csUserIdsList) {
            if(!csUserIdsListForResourceId.contains(csUserId)) {
                fail("Does not contain valid csUserId");
            }
        }
        userResourceCache.remove(RESOURCE_ID);
        List<String> csUserIdsListForResourceIdAfterRemoval = userResourceCache.get(RESOURCE_ID);
        assertNull(csUserIdsListForResourceIdAfterRemoval);
    }
}
