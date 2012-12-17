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
import static org.junit.Assert.assertNotNull;


/**
 * Test case for {@link AccountUpdateXpathExtractor}
 * @author Babaprakash D
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class AccountUpdateXpathExtractorTest extends AbstractAccountXpathUnitTest {

    @Autowired
    private AccountUpdateXpathExtractor accountUpdateXPathExtrac;

    private Node message;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(XML_UPDATED_ACCOUNT);
    }
    @Test
    public void testAccountUpdateFilter() throws Exception {
        assertNotNull(accountUpdateXPathExtrac.getUpdateFilter(message).getValue());
    }
    @Test
    public void testAccountId() throws Exception {
        assertEquals("Unexpected AccountId",Long.valueOf(9138026),accountUpdateXPathExtrac.getAccountId(message).getValue());
    }

    @Test
    public void testAccoutNameOld() throws Exception {
        assertEquals("Unexpected AccountName","176544\\David345",accountUpdateXPathExtrac.getOldAccountName(message).getValue());
    }

    @Test
    public void testAccoutNameNew() throws Exception {
        assertEquals("Unexpected AccountName","176544\\DAVID345",accountUpdateXPathExtrac.getNewAccountName(message).getValue()); 
    }

    @Test
    public void testAccoutTypeIdOld() throws Exception {
        assertEquals("Unexpected AccountType","NO",accountUpdateXPathExtrac.getOldAccountTypeId(message).getValue());  
    }
    @Test
    public void testAccoutTypeIdNew() throws Exception {
        assertEquals("Unexpected AccountType","NS",accountUpdateXPathExtrac.getNewAccountTypeId(message).getValue()); 
    }

    @Test
    public void testStatusId2Old() throws Exception {
        assertEquals("Unexpected Account StatusId2","ÅP",accountUpdateXPathExtrac.getOldAccountStatusId2(message).getValue()); 
    }
    @Test
    public void testStatusId2New() throws Exception {
        assertEquals("Unexpected Account StatusId2","PA",accountUpdateXPathExtrac.getNewAccountStatusId2(message).getValue()); 
    }

    @Test
    public void testStatusIdOld() throws Exception {
        assertEquals("Unexpected Account StatusId","",accountUpdateXPathExtrac.getOldAccountStatusId(message).getValue());
    }
    @Test
    public void testStatusIdNew() throws Exception {
        assertEquals("Unexpected Account StatusId"," ",accountUpdateXPathExtrac.getNewAccountStatusId(message).getValue());
    }

    @Test
    public void testInvoiceMediumOld() throws Exception {
        assertEquals("Unexpected Account Invoice Medium"," ",accountUpdateXPathExtrac.getOldAccountInvMedium(message).getValue());
    }
    @Test
    public void testInvoiceMediumNew() throws Exception {
        assertEquals("Unexpected Account Invoice Medium","",accountUpdateXPathExtrac.getNewAccountInvMedium(message).getValue());
    }
    @Test
    public void testCustIdPayerOld() throws Exception {
        assertEquals("Unexpected CustId Payer","6951817",accountUpdateXPathExtrac.getOldCustIdPayer(message).getValue());
    }
    @Test
    public void testCustIdPayerNew() throws Exception {
        assertEquals("Unexpected CustId Payer","6951818",accountUpdateXPathExtrac.getNewCustIdPayer(message).getValue());
    }
    @Test
    public void testAccountInfoIsDeletedOld() throws Exception {
        assertEquals("Unexpected Info is deleted","N",accountUpdateXPathExtrac.getOldInfoIsDeleted(message).getValue()); 
    }
    @Test
    public void testAccountInfoIsDeletedNew() throws Exception {
        assertEquals("Unexpected Info is deleted","Y",accountUpdateXPathExtrac.getNewInfoIsDeleted(message).getValue());
    }
}
