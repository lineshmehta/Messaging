package com.telenor.cos.messaging.producers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountPayerUpdateEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.producers.xpath.AccountUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

/**
 * Test case for {@link com.telenor.cos.messaging.producers.AccountPayerUpdateProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class AccountPayerUpdateProducerTest {

    private static final Long ACCOUNT_ID = Long.valueOf(32143317);
    private static final Long CUST_ID = Long.valueOf(176544);
    private static final String FIRST_NAME = "Nils";
    private static final String MIDDLE_NAME = "Ole";
    private static final String LAST_NAME = "Larsen";
    private static final String POST_CODE_ID_MAIN = "648";
    private static final Long CUST_UNIT_NUMBER = Long.valueOf(555);
    private static final String POST_CODE_NAME_MAIN = "AUKRA";

    @Mock
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    @Mock
    private CustomerCache customerCache;

    private AccountPayerUpdateProducer accountPayerUpdateProducer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountPayerUpdateProducer = new AccountPayerUpdateProducer();
        ReflectionTestUtils.setField(accountPayerUpdateProducer, "accountUpdateXpathExtractor",accountUpdateXpathExtractor);
        ReflectionTestUtils.setField(accountPayerUpdateProducer, "customerCache", customerCache);
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
    }

    @Test
    public void testIsApplicableToProducer() throws Exception {
        when(accountUpdateXpathExtractor.getNewCustIdPayer(any(Node.class))).thenReturn(XPathString.valueOf("32143317"));
        assertTrue(accountPayerUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testNotApplicableToProducer() throws Exception {
        when(accountUpdateXpathExtractor.getNewCustIdPayer(any(Node.class))).thenReturn(null);
        assertFalse(accountPayerUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage() throws Exception {
        CachableCustomer customer = createCachableCustomer();
        when(accountUpdateXpathExtractor.getNewCustIdPayer(any(Node.class))).thenReturn(XPathString.valueOf("176544"));
        when(customerCache.get(CUST_ID)).thenReturn(customer);
        AccountPayerUpdateEvent event = (AccountPayerUpdateEvent) (accountPayerUpdateProducer.produceMessage(any(Node.class))).get(0);
        assertEquals(ACCOUNT_ID, event.getDomainId());
        assertEquals(ACTION.PAYER_CHANGE, event.getAction());
        assertEquals(CUST_ID, event.getNewPayerCustomerName().getCustomerId());
        assertEquals("Unexpected new Payer Customer Id", CUST_ID, event.getNewPayerCustomerName().getCustomerId());
        assertEquals("Unexpected New Payer Customer FirstName", FIRST_NAME, event.getNewPayerCustomerName().getFirstName());
        assertEquals("Unexpected New Payer Customer MiddleName", MIDDLE_NAME, event.getNewPayerCustomerName().getMiddleName());
        assertEquals("Unexpected New Payer Customer LastName", LAST_NAME, event.getNewPayerCustomerName().getLastName());
        assertEquals("Unexpected New Payer Customer PostCodeIdManin",POST_CODE_ID_MAIN,event.getNewPayerCustomerAddress().getPostcodeIdMain());
        assertEquals("Unexpected New Payer Customer PostCodeNameMain",POST_CODE_NAME_MAIN,event.getNewPayerCustomerAddress().getPostcodeNameMain());
    }

    @Test
    public void whenPayerIsRemovedFromAccount() throws Exception {
        when(accountUpdateXpathExtractor.getNewCustIdPayer(any(Node.class))).thenReturn(XPathString.valueOf(null));
        AccountPayerUpdateEvent event = (AccountPayerUpdateEvent) (accountPayerUpdateProducer.produceMessage(any(Node.class))).get(0);
        assertDomainIdAndAction(event);
        assertNull(event.getNewPayerCustomerName());
    }

    private CachableCustomer createCachableCustomer() {
        CachableCustomer customer = new CachableCustomer(CUST_ID);
        customer.setFirstName(FIRST_NAME);
        customer.setMiddleName(MIDDLE_NAME);
        customer.setLastName(LAST_NAME);
        customer.setCustUnitNumber(CUST_UNIT_NUMBER);
        customer.setPostcodeIdMain(POST_CODE_ID_MAIN);
        customer.setPostcodeNameMain(POST_CODE_NAME_MAIN);
        return customer;
    }

    private void assertDomainIdAndAction(AccountPayerUpdateEvent accountPayerUpdateEvent) {
        assertEquals(ACCOUNT_ID, accountPayerUpdateEvent.getDomainId());
        assertEquals(ACTION.PAYER_CHANGE, accountPayerUpdateEvent.getAction());
    }
}
