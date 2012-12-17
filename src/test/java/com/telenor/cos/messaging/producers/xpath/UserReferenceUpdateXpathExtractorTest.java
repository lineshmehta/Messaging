package com.telenor.cos.messaging.producers.xpath;

import com.telenor.cos.test.category.ServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class UserReferenceUpdateXpathExtractorTest extends AbstractXpathUnitTest{

    @Autowired
    private UserReferenceUpdateXpathExtractor extractor;
    private Node message;


    @Before
    public void setUp() throws Exception {
        message = stringToDom(XML);
    }

    @Test
    public void getEnvoiceRef() throws Exception{
        String einvoiceRef = extractor.getNewEinvoiceRef(message).getValue();
        assertEquals("trololol", einvoiceRef);
    }

    @Test
    public void getUserRefDesc() throws Exception{
        String userRefDescr = extractor.getNewUserRefDescr(message).getValue();
        assertEquals("bittelitt", userRefDescr);
    }

    @Test
    public void getSubscrId() throws Exception{
        Long subscrId = extractor.getOldSusbcrId(message).getValue();
        assertEquals(Long.valueOf("32143317"), subscrId);
    }

    @Test
    public void getNewInfoIsDelete() throws Exception {
        String oldInfoIsDelete = extractor.getNewInfoIsDeleted(message).getValue();
        assertEquals("Y", oldInfoIsDelete);
    }

    @Test
    public void getOldUserRefDescr() throws Exception {
        String oldUserRefDescr = extractor.getOldUserRefDescr(message).getValue();
        assertEquals("bittelitt_test", oldUserRefDescr);
    }

    @Test
    public void getOldEInvoiceRef() throws Exception {
        String oldInvoiceRef = extractor.getOldEinvoiceRef(message).getValue();
        assertEquals("trololol_test", oldInvoiceRef);
    }

    private static final String XML= "<update schema=\"USER_REFERENCE\">"
            + "<values>"
            + "<cell name=\"EINVOICE_REF\" type=\"CHAR\">trololol</cell>"
            + "<cell name=\"NUMBER_TYPE\" type=\"CHAR\">TE</cell>"
            + "<cell name=\"USER_REF_DESCR\" type=\"CHAR\">bittelitt</cell>"
            + "<cell name=\"INFO_IS_DELETED\" type=\"CHAR\">Y</cell>"
            + "</values>"
            + "<oldValues>"
            + "<cell name=\"SUBSCR_ID\" type=\"NUMERIC\">32143317</cell>"
            + "<cell name=\"INFO_IS_DELETED\" type=\"CHAR\">N</cell>"
            + "<cell name=\"USER_REF_DESCR\" type=\"CHAR\">bittelitt_test</cell>"
            + "<cell name=\"EINVOICE_REF\" type=\"CHAR\">trololol_test</cell>"
            + "</oldValues>"
            + "</update>";
}
