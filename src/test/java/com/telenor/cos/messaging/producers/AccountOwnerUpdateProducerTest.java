package com.telenor.cos.messaging.producers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountOwnerUpdateEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.producers.xpath.AccountUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * Test case for {@link com.telenor.cos.messaging.producers.AccountOwnerUpdateProducer}
 * 
 * @author Sanjin Cevro (Capgemini)
 * 
 */
public class AccountOwnerUpdateProducerTest {

    private static final Long ACCOUNT_ID = Long.valueOf(32143317);
    private static final Long CUST_ID = Long.valueOf(176544);
    private static final String FIRST_NAME = "Nils";
    private static final String MIDDLE_NAME = "Ole";
    private static final String LAST_NAME = "Larsen";
    private static final String POST_CODE_ID_MAIN = "6480";
    private static final Long CUST_UNIT_NUMBER = Long.valueOf(555);
    private static final String POST_CODE_NAME_MAIN = "AUKRA";

    @Mock
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    @Mock
    private CustomerCache customerCache;

    private AccountOwnerUpdateProducer accountOwnerUpdateProducer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountOwnerUpdateProducer = new AccountOwnerUpdateProducer();
        ReflectionTestUtils.setField(accountOwnerUpdateProducer, "accountUpdateXpathExtractor", accountUpdateXpathExtractor);
        ReflectionTestUtils.setField(accountOwnerUpdateProducer, "customerCache", customerCache);
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
    }

    @Test
    public void testIsApplicableToProducer() throws Exception {
        when(accountUpdateXpathExtractor.getNewCustIdResp(any(Node.class))).thenReturn(XPathString.valueOf("32143317"));
        assertTrue(accountOwnerUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testNotApplicableToProducer() throws Exception {
        when(accountUpdateXpathExtractor.getNewCustIdResp(any(Node.class))).thenReturn(null);
        assertFalse(accountOwnerUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage() throws Exception {
        CachableCustomer customer = createCachableCustomer();
        when(accountUpdateXpathExtractor.getNewCustIdResp(any(Node.class))).thenReturn(XPathString.valueOf("176544"));
        when(customerCache.get(CUST_ID)).thenReturn(customer);
        AccountOwnerUpdateEvent event = (AccountOwnerUpdateEvent) (accountOwnerUpdateProducer.produceMessage(any(Node.class))).get(0);
        assertEquals(ACCOUNT_ID, event.getDomainId());
        assertEquals(ACTION.OWNER_CHANGE, event.getAction());
        assertEquals(CUST_ID, event.getNewOwnerCustomerName().getCustomerId());
        assertEquals("Unexpected new Owner Customer Id", CUST_ID, event.getNewOwnerCustomerName().getCustomerId());
        assertEquals("Unexpected New Owner Customer FirstName", FIRST_NAME, event.getNewOwnerCustomerName().getFirstName());
        assertEquals("Unexpected New Owner Customer MiddleName", MIDDLE_NAME, event.getNewOwnerCustomerName().getMiddleName());
        assertEquals("Unexpected New Owner Customer LastName", LAST_NAME, event.getNewOwnerCustomerName().getLastName());
        assertEquals("Unexpected New Owner Customer PostCodeIdManin", POST_CODE_ID_MAIN, event.getNewOwnerCustomerAddress().getPostcodeIdMain());
        assertEquals("Unexpected New Owner Customer PostCodeNameMain", POST_CODE_NAME_MAIN, event.getNewOwnerCustomerAddress().getPostcodeNameMain());
    }

    @Test
    public void whenOwnerIsRemovedFromAccount() throws Exception {
        when(accountUpdateXpathExtractor.getNewCustIdResp(any(Node.class))).thenReturn(XPathString.valueOf(null));
        AccountOwnerUpdateEvent event = (AccountOwnerUpdateEvent) (accountOwnerUpdateProducer.produceMessage(any(Node.class))).get(0);
        assertDomainIdAndAction(event);
        assertNull(event.getNewOwnerCustomerName());
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

    private void assertDomainIdAndAction(AccountOwnerUpdateEvent accountOwnerUpdateEvent) {
        assertEquals(ACCOUNT_ID, accountOwnerUpdateEvent.getDomainId());
        assertEquals(ACTION.OWNER_CHANGE, accountOwnerUpdateEvent.getAction());
    }
}
