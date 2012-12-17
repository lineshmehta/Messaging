package com.telenor.cos.messaging.producers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Account;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.account.AccountNewEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.producers.xpath.AccountInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;
/**
 * Test case for {@link AccountNewProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class AccountNewProducerTest{

    private static final Long CUST_ID_PAYER = 2L;
    private static final Long CUST_ID_RESP = 1L;
    private static final Long MASTER_ID_OWNER = 11L;
    private static final Long MASTER_ID_PAYER = 22L;
    private static final String FIRST_NAME = "Nils";
    private static final String MIDDLE_NAME = "Ole";
    private static final String LAST_NAME = "Larsen";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String STATUS = "status";
    private static final String PSTATUS = "pStatus";
    private static final String FORMAT = "format";
    private static final String POSTCODE_ID_MAIN = "300";
    public static final String POST_CODE_NAME = "OSLO";

    private AccountNewProducer accountNewProducer;

    @Mock
    private AccountInsertXpathExtractor accountInsertXpathExtractor;

    @Mock
    private CustomerCache customerCacheMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountNewProducer = new AccountNewProducer();
        ReflectionTestUtils.setField(accountNewProducer, "accountInsertXpathExtractor", accountInsertXpathExtractor);
        ReflectionTestUtils.setField(accountNewProducer, "customerCache", customerCacheMock);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(accountInsertXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf(4L));
        boolean isApplicable = accountNewProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testProduceMessage() throws Exception {
        CachableCustomer customerOwner = new CachableCustomer(CUST_ID_RESP);
        customerOwner.setMasterCustomerId(MASTER_ID_OWNER);
        CachableCustomer customerPayer = createCachablePayerCustomer();
        createAccountMock(customerOwner, customerPayer);
        Account account = getAccount();
        assertAccount(account);
        assertCustomerName(account,FIRST_NAME,MIDDLE_NAME,LAST_NAME);
        assertMasterId(account,MASTER_ID_PAYER,MASTER_ID_OWNER);
        assertEquals("Unexpected Invoice Address", POSTCODE_ID_MAIN + ' ' + POST_CODE_NAME , account.getInvoiceAddress());
    }

    @Test
    public void testProduceMessageWhenCachableCustomerIsNull() throws Exception {
        createAccountMock(null,null);
        Account account = getAccount();
        assertAccount(account);
        assertCustomerName(account,null,null,null);
        assertMasterId(account,null,null);
    }

    private void createAccountMock(CachableCustomer customerOwner,CachableCustomer customerPayer) {
        when(accountInsertXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf(1L));
        when(accountInsertXpathExtractor.getAccountName(any(Node.class))).thenReturn(XPathString.valueOf(NAME));
        when(accountInsertXpathExtractor.getAccountTypeId(any(Node.class))).thenReturn(XPathString.valueOf(TYPE));
        when(accountInsertXpathExtractor.getAccountStatusId(any(Node.class))).thenReturn(XPathString.valueOf(STATUS));
        when(accountInsertXpathExtractor.getAccountPaymentStatus(any(Node.class))).thenReturn(XPathString.valueOf(PSTATUS));
        when(accountInsertXpathExtractor.getAccountCustIdResp(any(Node.class))).thenReturn(XPathLong.valueOf(1L));
        when(accountInsertXpathExtractor.getAccountCustIdPayer(any(Node.class))).thenReturn(XPathLong.valueOf(2L));
        when(customerCacheMock.get(CUST_ID_RESP)).thenReturn(customerOwner);
        when(customerCacheMock.get(CUST_ID_PAYER)).thenReturn(customerPayer);
        when(accountInsertXpathExtractor.getAccountInvoiceFormat(any(Node.class))).thenReturn(XPathString.valueOf(FORMAT));
    }

    private CachableCustomer createCachablePayerCustomer() {
        CachableCustomer customerPayer = new CachableCustomer(CUST_ID_PAYER);
        customerPayer.setFirstName(FIRST_NAME);
        customerPayer.setMiddleName(MIDDLE_NAME);
        customerPayer.setLastName(LAST_NAME);
        customerPayer.setCustUnitNumber(555L);
        customerPayer.setMasterCustomerId(MASTER_ID_PAYER);
        customerPayer.setPostcodeIdMain(POSTCODE_ID_MAIN);
        customerPayer.setPostcodeNameMain(POST_CODE_NAME);
        return customerPayer;
    }

    private Account getAccount() {
        List<Event> eventsList = accountNewProducer.produceMessage(any(Node.class));
        AccountNewEvent event = (AccountNewEvent) eventsList.get(0);
        return  event.getAccount();
    }

    private void assertAccount(Account account) {
        assertNotNull(account);
        assertEquals(Long.valueOf(1), account.getAccountId());
        assertEquals(NAME,account.getName());
        assertEquals(TYPE, account.getType());
        assertEquals(STATUS, account.getStatus());
        assertEquals(FORMAT, account.getInvoiceFormat());
        assertEquals(PSTATUS, account.getPaymentStatus());
        assertEquals(CUST_ID_RESP, account.getCustIdResp());
        assertEquals(CUST_ID_PAYER, account.getCustIdPayer());
    }

    private void assertCustomerName(Account account,String firstName,String middleName,String lastName) {
        assertEquals(firstName, account.getCustFirstNamePayer());
        assertEquals(middleName, account.getCustMiddleNamePayer());
        assertEquals(lastName, account.getCustLastNamePayer());
    }
    private void assertMasterId(Account account, Long masterIdPayer, Long masterIdOwner) {
        assertEquals(masterIdOwner, account.getMasterIdOwner());
        assertEquals(masterIdPayer, account.getMasterIdPayer());
    }
}
