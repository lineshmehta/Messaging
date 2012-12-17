package com.telenor.cos.messaging.producers.xpath;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
public class AgreementUpdateXpathExtractorTest extends AbstractXpathUnitTest {

    @Autowired
    private AgreementUpdateXpathExtractor extractor;

    private Node message;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(getXml());
    }
    
    @Test
    public void testAgreementId() throws Exception {
        assertEquals("Unexpected agreementId", Long.valueOf(4274399), extractor.getAgreementId(message).getValue());
    }
    
    @Test
    public void testInfoIsDeleted() throws Exception {
        assertEquals("Unexpected info is deleted", "Y", extractor.getInfoIsDeleted(message).getValue());
    }
    
    public String getXml() {
        return "<update schema=\"AGREEMENT_NEW\">"
                + "<values>"
                +     "<cell name=\"INFO_IS_DELETED\" type=\"CHAR\">Y</cell>" 
                + "</values>"
                + "<oldValues>"
                +     "<cell name=\"AGREEMENT_ID\" type=\"NUMERIC\">4274399</cell>"
                + "</oldValues></update>";
    }
}


