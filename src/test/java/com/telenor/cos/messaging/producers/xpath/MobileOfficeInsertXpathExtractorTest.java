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
public class MobileOfficeInsertXpathExtractorTest extends AbstractXpathUnitTest {

    @Autowired
    private MobileOfficeInsertXpathExtractor mobileOfferInsertXpathExtractor;

    private Node message;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(getMobileOfferInsertXml());
    }

    @Test
    public void getExtensionNumberTest() {
        assertEquals("45678945", mobileOfferInsertXpathExtractor.getExtensionNumber(message).getValue());
    }

    @Test
    public void getDirectoryNumberIdTest() {
        assertEquals("111222555444", mobileOfferInsertXpathExtractor.getDirectoryNumber(message).getValue());
    }

    private String getMobileOfferInsertXml() {
        return "<insert schema=\"MOBILE_OFFICE_MEMBERS\">" + "<values>"
                + "<cell name=\"EXTENSION_NUMBER\" type=\"VARCHAR\">45678945</cell>"
                + "<cell name=\"DIRECTORY_NUMBER_ID\" type=\"VARCHAR\">111222555444</cell>" + "</values>" + "</insert>";
    }
}
