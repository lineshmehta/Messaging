package com.telenor.cos.messaging.producers.xpath;

import com.telenor.cos.test.category.ServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link UserResourceInsertXpathExtractor}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class UserResourceInsertXpathExtractorTest extends AbstractUserResourceXpathUnitTest {

    @Autowired
    private UserResourceInsertXpathExtractor userResourceInsertXpathExtrac;

    private Node message;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(NEW_USER_RESOURCE);
    }

    @Test
    public void testCsUserId() throws Exception {
        assertEquals("Unexpected csuser Id", "cosmaster", userResourceInsertXpathExtrac.getCsUserId(message).getValue());
    }

    @Test
    public void testResourceId() throws Exception {
        assertEquals("Unexpected resource Id", Long.valueOf(1), userResourceInsertXpathExtrac.getResourceId(message).getValue());
    }
}
