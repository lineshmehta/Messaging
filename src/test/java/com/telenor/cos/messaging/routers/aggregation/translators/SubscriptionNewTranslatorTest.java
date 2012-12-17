package com.telenor.cos.messaging.routers.aggregation.translators;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Subscription;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.producers.xpath.AbstractSubscriptionXpathUnitTest;
import com.telenor.cos.messaging.producers.xpath.SubscriptionInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Unittest of {@link com.telenor.cos.messaging.routers.aggregation.translators.SubscriptionNewTranslator}
 *
 * @author Eirik Bergande (Capgemini)
 *
 */
public class SubscriptionNewTranslatorTest extends AbstractSubscriptionXpathUnitTest {

    private static final XPathLong ACC_ID = XPathLong.valueOf("999999001");
    private static final XPathLong DOMAIN_ID = XPathLong.valueOf("32143317");
    private static final Long CUST_ID = Long.valueOf(6935066);
    private static final XPathLong CONTRACT_ID = XPathLong.valueOf("12716621");
    public static final XPathString PRODUCT_ID = XPathString.valueOf("04713");
    public static final XPathString HAS_SECRET_NUMBER = XPathString.valueOf("N");
    public static final XPathLong MSISDN = XPathLong.valueOf("580000506010");
    public static final String BASE_PRODUCT_ID = "123";
    public static final XPathString STATUS_ID = XPathString.valueOf(" ");
    private static final String FIRST_NAME = "Eirik";
    private static final String MIDDLE_NAME = "Falk Georg";
    private static final String LAST_NAME = "Bergande";

    @Mock
    private SubscriptionInsertXpathExtractor subscriptionInsertXpathExtractor;

    @Mock
    private CustomerCache customerCache;

    @Mock
    private SubscriptionTypeCache subscriptionTypeCache;

    private SubscriptionNewTranslator subscriptionTranslator;
    private CachableCustomer customer;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subscriptionTranslator = new SubscriptionNewTranslator();
        createCachableCustomer();
        ReflectionTestUtils.setField(subscriptionTranslator, "subscriptionInsertXpathExtractor", subscriptionInsertXpathExtractor);
        ReflectionTestUtils.setField(subscriptionTranslator, "customerCache", customerCache);
        ReflectionTestUtils.setField(subscriptionTranslator, "subscriptionTypeCache", subscriptionTypeCache);
    }

    @Test
    public void shouldReturnSubscriptionNewEvent() throws Exception {
        final XPathDate date = createSubscriptionMock(customer);
        List<Event> eventList = subscriptionTranslator.translate(any(Node.class));
        NewSubscriptionEvent newSubscriptionEvent = (NewSubscriptionEvent) eventList.get(0);
        Subscription subscription = newSubscriptionEvent.getData();
        assertSubscription(date, newSubscriptionEvent, subscription);
        assertCustomerName(subscription,FIRST_NAME,MIDDLE_NAME,LAST_NAME);
    }

    @Test
    public void shouldNotPopulateCustomerDataWhenCustomerIsNotExistingInCache() throws Exception{
        final XPathDate date = createSubscriptionMock(null);
        List<Event> eventList = subscriptionTranslator.translate(any(Node.class));
        NewSubscriptionEvent newSubscriptionEvent = (NewSubscriptionEvent) eventList.get(0);
        Subscription subscription = newSubscriptionEvent.getData();
        assertSubscription(date, newSubscriptionEvent, subscription);
        assertCustomerName(subscription,null,null,null);
    }

    private void createCachableCustomer() {
        customer = new CachableCustomer(33L);
        customer.setFirstName(FIRST_NAME);
        customer.setMiddleName(MIDDLE_NAME);
        customer.setLastName(LAST_NAME);
    }

    private XPathDate createSubscriptionMock(CachableCustomer cachableCustomer) throws ParseException {
        final XPathDate date = XPathDate.valueOf(DateUtils.parseDate("09.01.2012 00:00:00", new String[]{"dd.MM.yyyy HH:mm:ss"}));

        when(subscriptionInsertXpathExtractor.getAccId(any(Node.class))).thenReturn(ACC_ID);
        when(subscriptionInsertXpathExtractor.getCustId(any(Node.class))).thenReturn(XPathLong.valueOf(CUST_ID));
        when(customerCache.get(any(Long.class))).thenReturn(cachableCustomer);
        when(subscriptionInsertXpathExtractor.getSubscrValidFromDate(any(Node.class))).thenReturn(date);
        when(subscriptionInsertXpathExtractor.getSubscrValidToDate(any(Node.class))).thenReturn(date);
        when(subscriptionInsertXpathExtractor.getDirectoryNumberId(any(Node.class))).thenReturn(MSISDN);
        when(subscriptionInsertXpathExtractor.getContractId(any(Node.class))).thenReturn(CONTRACT_ID);
        when(subscriptionInsertXpathExtractor.getS212ProductId(any(Node.class))).thenReturn(PRODUCT_ID);
        when(subscriptionInsertXpathExtractor.getSubscrHasSecretNumber(any(Node.class))).thenReturn(HAS_SECRET_NUMBER);
        when(subscriptionInsertXpathExtractor.getSubscrId(any(Node.class))).thenReturn(DOMAIN_ID);
        when(subscriptionTypeCache.get(any(String.class))).thenReturn(BASE_PRODUCT_ID);
        when(subscriptionInsertXpathExtractor.getStatusId(any(Node.class))).thenReturn(STATUS_ID);
        return date;
    }

    private void assertSubscription(final XPathDate date,NewSubscriptionEvent newSubscriptionEvent, Subscription subscription) {
        assertNotNull(newSubscriptionEvent);
        assertEquals("Unexpected Subscription id",DOMAIN_ID.getValue(), newSubscriptionEvent.getDomainId());
        assertEquals("Unexpected Account id",ACC_ID.getValue(), subscription.getAccountId());
        assertEquals("Unexpected userCustomer id",CUST_ID, subscription.getUserCustomerId());
        assertEquals("Unexpected validFromDate",date.getValue(), subscription.getValidFromDate());
        assertEquals("Unexpected validToDate",date.getValue(), subscription.getValidToDate());
        assertEquals("Unexpected msisdn",MSISDN.getValue(), subscription.getMsisdn());
        assertEquals("Unexpected contractId",CONTRACT_ID.getValue(), subscription.getContractId());
        assertEquals("Unexpected baseProduct id",BASE_PRODUCT_ID, subscription.getSubscriptionType());
        assertFalse("Unexpected secretNumber",subscription.getIsSecretNumber());
        assertEquals("Unexpected status id", STATUS_ID.getValue(), subscription.getStatusId());
    }
    private void assertCustomerName(Subscription subscription,String firstName,String middleName,String lastName) {
        assertEquals("Unexpected CustomerId",CUST_ID,subscription.getUserCustomerId());
        assertEquals("Unexpected FirstName",firstName, subscription.getFirstName());
        assertEquals("Unexpected MiddleName",middleName, subscription.getMiddleName());
        assertEquals("Unexpected LastName",lastName, subscription.getLastName());
    }
}
