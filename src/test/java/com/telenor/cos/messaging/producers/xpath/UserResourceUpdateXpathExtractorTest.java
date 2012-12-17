package com.telenor.cos.messaging.producers.xpath;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import com.telenor.cos.test.category.ServiceTest;

/**
 * Test case for {@link UserResourceUpdateXpathExtractor}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class UserResourceUpdateXpathExtractorTest extends AbstractUserResourceXpathUnitTest {

    @Autowired
    private UserResourceUpdateXpathExtractor userResourceUpdateXpathExtractor;

    private Node resourceidUpdateMessage;
    private Node csUserIdUpdateMessage;

    @Before
    public void setUp() throws Exception {
        resourceidUpdateMessage = stringToDom(UPDATE_RESOURCEID_USER_RESOURCE);
        csUserIdUpdateMessage = stringToDom(UPDATE_CSUSERID_USER_RESOURCE);
    }

    @Test
    public void testCsUserId() throws Exception {
        assertEquals("Unexpected csuser Id", "cosmaster1", userResourceUpdateXpathExtractor.getCsUserId(csUserIdUpdateMessage).getValue());
    }

    @Test
    public void testResourceId() throws Exception {
        assertEquals("Unexpected resource Id", Long.valueOf(2), userResourceUpdateXpathExtractor.getResourceId(resourceidUpdateMessage).getValue());
    }

    @Test
    public void testResourceIdOld() throws Exception {
        assertEquals("Unexpected old resource Id", Long.valueOf(1), userResourceUpdateXpathExtractor.getResourceIdOld(resourceidUpdateMessage).getValue());
    }

    @Test
    public void testCsuserIdOld() throws Exception {
        assertEquals("Unexpected old csUserId", "cosmaster", userResourceUpdateXpathExtractor.getCsUserIdOld(csUserIdUpdateMessage).getValue());
    }
}
