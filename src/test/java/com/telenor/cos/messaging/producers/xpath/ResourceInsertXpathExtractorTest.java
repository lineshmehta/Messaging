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
 * Test case for {@link ResourceInsertXpathExtractor}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class ResourceInsertXpathExtractorTest extends AbstractResourceXpathUnitTest {

    @Autowired
    private ResourceInsertXpathExtractor resourceInsertXpathExtractor;

    private Node message;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(NEW_RESOURCE_XML);
    }

    @Test
    public void testGetResourceId() {
        assertEquals("Unexpected Resource Id",Long.valueOf(456),resourceInsertXpathExtractor.getResourceId(message).getValue());
    }

    @Test
    public void testGetResourceTypeId() {
        assertEquals("Unexpected Resource Type Id",Integer.valueOf(456),resourceInsertXpathExtractor.getResourceTypeId(message).getValue());
    }

    @Test
    public void testGetResourceTypeIdKey() {
        assertEquals("Unexpected Resource Type Id Key","Superaccount",resourceInsertXpathExtractor.getResourceTypeIdKey(message).getValue());
    }

    @Test
    public void testGetResourceHasContentInherit() {
        assertEquals("Unexpected ResourceHasContentInherit","N",resourceInsertXpathExtractor.getResourceHasContentInherit(message).getValue());
    }

    @Test
    public void testGetResourceHasStructureInherit() {
        assertEquals("Unexpected ResourceHasStructureInherit","Y",resourceInsertXpathExtractor.getResourceHasStructureInherit(message).getValue());
    }
}
