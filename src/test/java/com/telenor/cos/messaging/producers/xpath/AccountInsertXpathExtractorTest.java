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

import com.telenor.cos.test.category.ServiceTest;

/**
 * Test case for {@link AccountInsertXpathExtractor}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
public class AccountInsertXpathExtractorTest extends AbstractAccountXpathUnitTest {

    @Autowired
    private AccountInsertXpathExtractor extractor;
    private Node message;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(XML_NEW_ACCOUNT);
    }

    @Test
    public void getAccountId() throws Exception {
        assertEquals(Long.valueOf("456"), extractor.getAccountId(message).getValue());
    }

    @Test
    public void getAccountName() throws Exception {
        assertEquals("Superaccount", extractor.getAccountName(message).getValue());
    }

    @Test
    public void getTypeId() throws Exception {
        assertEquals("NSFO", extractor.getAccountTypeId(message).getValue());
    }

    @Test
    public void getAccountStatusId() throws Exception {
        assertEquals("ÅP", extractor.getAccountStatusId(message).getValue());
    }

    @Test
    public void getAccountPaymentTypeId() throws Exception {
        assertEquals("55", extractor.getAccountPaymentStatus(message).getValue());
    }

    @Test
    public void getAccountCustIdPayer() throws Exception {
        assertEquals(Long.valueOf("123"), extractor.getAccountCustIdPayer(message).getValue());
    }
    
    @Test
    public void getAccountCustIdResp() throws Exception {
        assertEquals(Long.valueOf("789"), extractor.getAccountCustIdResp(message).getValue());
    }
    
    @Test
    public void getAccountInvoiceFormat() throws Exception {
        assertEquals("Letter", extractor.getAccountInvoiceFormat(message).getValue());
    }
}
