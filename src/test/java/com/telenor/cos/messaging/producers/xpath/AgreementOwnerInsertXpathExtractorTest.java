package com.telenor.cos.messaging.producers.xpath;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.util.TestHelper;
import com.telenor.cos.test.category.ServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
public class AgreementOwnerInsertXpathExtractorTest {

    @Autowired
    private AgreementOwnerInsertXpathExtractor extractor;
    
    private static final String NEW_XML = "dataset/agreement_owner_new.xml";
    
    private Node message;
    
    @Before
    public void setUp() throws Exception{
        message = new TestHelper().fileToDom(NEW_XML);
    }
    
    @Test
    public void getAgreementOwnerId(){
        assertEquals(Long.valueOf("531314"), extractor.getAgreementOwnerId(message).getValue());
    }
    
    @Test
    public void getMasterId(){
        assertEquals(Long.valueOf("100318909"), extractor.getMasterId(message).getValue());
    }
}
