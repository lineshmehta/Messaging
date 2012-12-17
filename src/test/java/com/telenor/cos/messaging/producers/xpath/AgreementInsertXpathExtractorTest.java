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
public class AgreementInsertXpathExtractorTest extends AbstractXpathUnitTest {

    @Autowired
    private AgreementInsertXpathExtractor extractor;

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
    public void testCustUnitNumber() throws Exception {
        assertEquals("Unexpected custUnitNumber", Long.valueOf(123456789), extractor.getCustUnitNumber(message)
                .getValue());
    }

    public String getXml() {
        return "<insert schema=\"AGREEMENT_NEW\">" + "<values>"
                + "<cell name=\"AGREEMENT_ID\" type=\"NUMERIC\">4274399</cell>"
                + "<cell name=\"CUST_UNIT_NUMBER\" type=\"NUMERIC\">123456789</cell>" + "</values>" + "</insert>";
    }
}
