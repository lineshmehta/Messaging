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
public class AgreementMemberUpdatetXpathExtractorTest extends AbstractXpathUnitTest{

    
    @Autowired
    private AgreementMemberUpdateXpathExtractor extractor;
    
    private static final String LOGICAL_DELETE_XML="dataset/agreementMember_logical_delete.xml";
    
    private Node message;
    
    @Before
    public void setUp() throws Exception {
        message = new TestHelper().fileToDom(LOGICAL_DELETE_XML);
    }
    
    @Test
    public void getAgreementMemberId(){
        assertEquals(Long.valueOf("666"), extractor.getOldAgreementMemberId(message).getValue());
    }
    
    @Test
    public void getNewInfoIsDeleted(){
        assertEquals("Y", extractor.getNewInfoIsDeleted(message).getValue());
    }
    
    @Test
    public void getOldMemberId(){
        assertEquals(Long.valueOf("1"), extractor.getOldAgreementId(message).getValue());
    }

    @Test
    public void getOldAgreementMemberMasterId() {
        assertEquals(Long.valueOf("555"), extractor.getOldAgreementMemberMasterId(message).getValue());
    }

    @Test
    public void getOldAgreementMemberCustUnitNumber() {
        assertEquals(Long.valueOf("121212"), extractor.getOldAgreementMemberCustUnitNumber(message).getValue());
    }
}
