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
 * Test case for {@link MasterStructureInsertXpathExtractor}
 * @author t798435
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class MasterStructureInsertXpathExtractorTest extends AbstractMasterStructureXpathUnitTest {
    
    private Node message;
    
    @Autowired
    private MasterStructureInsertXpathExtractor masterStructureInsertXpathExtrac;
    
    @Before
    public void setUp() throws Exception {
        message = stringToDom(NEW_MASTER_STRUCTURE_XML);
    }
    
    @Test
    public void checkMemberId() throws Exception {
        assertEquals("Unexpected Master Id Member (Master Id) ", Long.valueOf("666"), masterStructureInsertXpathExtrac.getMasterIdMember(message).getValue());
    }
    
    @Test
    public void checkOwnerId() throws Exception {
        assertEquals("Unexpected Master Id Owner ", Long.valueOf("777"), masterStructureInsertXpathExtrac.getMasterIdOwner(message).getValue());
    }
}
