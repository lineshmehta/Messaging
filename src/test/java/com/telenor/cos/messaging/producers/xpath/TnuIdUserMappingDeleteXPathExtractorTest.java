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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class TnuIdUserMappingDeleteXPathExtractorTest extends AbstractUserMappingXpathUnitTest{

    @Autowired
    private TnuIdUserMappingDeleteXPathExtractor xpathExtrac;
    
    private Node message;
    
    @Before
    public void setUp() throws Exception {
        message = stringToDom(DELETE_TNUID_USERMAPPING);
    }
    
    @Test
    public void shouldReturnOldTelenorId() throws Exception{
        String telenorId = xpathExtrac.getTelenorUserIdOld(message).getValue();
        assertEquals("_1000030", telenorId);
    }
    
    @Test
    public void shouldReturnCsUserId() throws Exception{
        String csUserId = xpathExtrac.getCosSecurityUserId(message).getValue();
        assertEquals("tn_9234", csUserId);
    }
    
    @Test
    public void shouldReturnApplicationId() throws Exception{
        Integer appId = xpathExtrac.getApplicationIdOld(message).getValue();
        assertEquals(Integer.valueOf(62), appId);
    }
    
    
    
}
