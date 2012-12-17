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
 * Test case for {@link UserReferenceInsertXpathExtractor}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class UserReferenceInsertXpathExtractorTest extends AbstractXpathUnitTest {

    @Autowired
    private UserReferenceInsertXpathExtractor userReferenceInsertXpathExtractor;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(XML);
    }

    private Node message;

    @Test
    public void getEnvoiceRef() throws Exception{
        String einvoiceRef = userReferenceInsertXpathExtractor.getEInvoiceRef(message).getValue();
        assertEquals("trololol", einvoiceRef);
    }

    @Test
    public void getNumberType() throws Exception{
        String numberType = userReferenceInsertXpathExtractor.getNumberType(message).getValue();
        assertEquals("TE", numberType);
    }

    @Test
    public void getUserRefDesc() throws Exception{
        String userRefDescr = userReferenceInsertXpathExtractor.getUserRefDescription(message).getValue();
        assertEquals("bittelitt", userRefDescr);
    }

    @Test
    public void getSubscrId() throws Exception{
        Long subscrId = userReferenceInsertXpathExtractor.getSubscriptionId(message).getValue();
        assertEquals(Long.valueOf("32143317"), subscrId);
    }

    private static final String XML= "<insert schema=\"USER_REFERENCE\">"
            + "<values>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">32143317</cell>"
            + "<cell name=\"EINVOICE_REF\" type=\"CHAR\">trololol</cell>"
            + "<cell name=\"NUMBER_TYPE\" type=\"CHAR\">TE</cell>"
            + "<cell name=\"USER_REF_DESCR\" type=\"CHAR\">bittelitt</cell>"
            + "</values>"
            + "</insert>";
}
