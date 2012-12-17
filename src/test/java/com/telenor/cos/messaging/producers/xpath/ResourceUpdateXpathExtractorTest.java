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
 * Test case for {@link ResourceUpdateXpathExtractor}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class ResourceUpdateXpathExtractorTest extends AbstractResourceXpathUnitTest {

    @Autowired
    private ResourceUpdateXpathExtractor resourceUpdateXpathExtractor;

    private Node logicalDeleteMessage;
    private Node updateMessage;

    @Before
    public void setUp() throws Exception {
        logicalDeleteMessage = stringToDom(LOGICAL_DELETE_RESOURCE_XML);
        updateMessage = stringToDom(UPDATE_RESOURCE_XML);
    }

    @Test
    public void testGetOldInfoIsDeleted() {
        assertEquals("Unexpected old infoIsDeleted","N",resourceUpdateXpathExtractor.getOldInfoIsDeleted(logicalDeleteMessage).getValue());
    }

    @Test
    public void testGetNewInfoIsDeleted() {
        assertEquals("Unexpected new infoIsDeleted","Y",resourceUpdateXpathExtractor.getNewInfoIsDeleted(logicalDeleteMessage).getValue());
    }

    @Test
    public void testGetOldResourceHasContentInherit() {
        assertEquals("Unexpected old ContentInherit","N",resourceUpdateXpathExtractor.getOldResourceHasContentInherit(updateMessage).getValue());
    }

    @Test
    public void testGetNewResourceHasContentInherit() {
        assertEquals("Unexpected new ContentInherit","Y",resourceUpdateXpathExtractor.getNewResourceHasContentInherit(updateMessage).getValue());
    }

    @Test
    public void testGetOldResourceHasStructureInherit() {
        assertEquals("Unexpected old StructureInherit","Y",resourceUpdateXpathExtractor.getOldResourceHasStructureInherit(updateMessage).getValue());
    }

    @Test
    public void testGetNewResourceHasStructureInherit() {
        assertEquals("Unexpected new StructureInherit","N",resourceUpdateXpathExtractor.getNewResourceHasStructureInherit(updateMessage).getValue());
    }

    @Test
    public void testGetOldResourceTypeIdKey() {
        assertEquals("Unexpected old ResourceTypeIdKey","Superaccount",resourceUpdateXpathExtractor.getOldResourceTypeIdKey(updateMessage).getValue());
    }

    @Test
    public void testGetNewResourceTypeIdKey() {
        assertEquals("Unexpected new ResourceTypeIdKey","Superaccount1",resourceUpdateXpathExtractor.getNewResourceTypeIdKey(updateMessage).getValue());
    }

    @Test
    public void testGetOldResourceTypeId() {
        assertEquals("Unexpected old ResourceTypeId",Integer.valueOf(456),resourceUpdateXpathExtractor.getOldResourceTypeId(updateMessage).getValue());
    }

    @Test
    public void testGetNewResourceTypeId() {
        assertEquals("Unexpected new ResourceTypeId",Integer.valueOf(457),resourceUpdateXpathExtractor.getNewResourceTypeId(updateMessage).getValue());
    }
}
