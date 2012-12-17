package com.telenor.cos.messaging.producers.xpath;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.test.category.ServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class RelSubscriptionXpathExtractorTest extends AbstractRelSubscriptionXpathExtractorTest{
    
    private final static Long OWNER_SUBSCRIPTION_ID = Long.valueOf("30870128");
    
    private final static String REL_SUBSCRIPTION_TYPE = "DK";
    
    @Autowired
    private RelSubscriptionXpathExtractor relSubscriptionXpathExtractor;

    @Test
    public void testGetOwnerSubscriptionId() throws Exception {
        assertEquals("Unexpected id", OWNER_SUBSCRIPTION_ID, relSubscriptionXpathExtractor.getOwnerSubscriptionId(stringToDom(INSERT_XML)).getValue());
    }
    
    @Test
    public void testGetRelSubscriptionType() throws Exception {
        assertEquals("Unexpected subscription type", REL_SUBSCRIPTION_TYPE, relSubscriptionXpathExtractor.getRelSubscriptionType(stringToDom(INSERT_XML)).getValue());
    }

    
}
