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
 * Test case for {@link TnuIdUserMappingInsertXPathExtractor}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class TnuIdUserMappingInsertXPathExtractorTest extends AbstractUserMappingXpathUnitTest {
    
    @Autowired
    private TnuIdUserMappingInsertXPathExtractor xpathExtrac;
    
    private Node message;
    
    @Before
    public void setUp() throws Exception {
        message = stringToDom(NEW_TNUID_USERMAPPING);
    }
    
    @Test
    public void testTnuId() throws Exception {
        assertEquals("Unexpected telenor user Id", "_1000030", xpathExtrac.getTelenorUserId(message).getValue());
    }
    @Test
    public void testApplicationId() throws Exception {
        assertEquals("Unexpected application Id",Integer.valueOf(62), xpathExtrac.getApplicationId(message).getValue());
    }
    @Test
    public void testCsUserId() throws Exception {
        assertEquals("Unexpected cs user id", "tn_9234", xpathExtrac.getCosSecurityUserId(message).getValue());
    }
}
