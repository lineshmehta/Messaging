package com.telenor.cos.messaging.routers.aggregation.translators;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.subscription.UpdatedSubscriptionEvent;
import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test {@link com.telenor.cos.messaging.routers.aggregation.translators.SubscriptionUpdatedAggregationTranslator}.
 */
public class SubscriptionUpdatedAggregationTranslatorTest {
    
    private static final XPathLong SUBSCRIPTION_ID = XPathLong.valueOf("666");
    private static final XPathDate EXPECTED_DATE = new XPathDate();
    private static final XPathLong USER_CUSTOMER_ID = XPathLong.valueOf("6");
    private static final XPathString SUBSCRIPTION_TYPE = XPathString.valueOf("5");
    private static final XPathString DIRECTORY_NUMBER = XPathString.valueOf("999999");
    private static final XPathString CONTRACT_ID = XPathString.valueOf("2");
    private static final XPathLong ACCOUNT_ID = XPathLong.valueOf("1");
    public static final String BASE_PRODUCT_ID = "123";

    @Mock
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractorMock;

    @Mock
    private SubscriptionTypeCache subscriptionTypeCache;
    
    
    private SubscriptionUpdatedAggregationTranslator subscriptionUpdatedTranslator;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subscriptionUpdatedTranslator = new SubscriptionUpdatedAggregationTranslator();
        ReflectionTestUtils.setField(subscriptionUpdatedTranslator, "subscriptionUpdateXpathExtractor", subscriptionUpdateXpathExtractorMock);
        ReflectionTestUtils.setField(subscriptionUpdatedTranslator, "subscriptionTypeCache", subscriptionTypeCache);
    }

    @Test
    public void testTranslate() throws Exception {
       setUpMock();
       List<Event> eventList = subscriptionUpdatedTranslator.translate(any(Node.class));
       UpdatedSubscriptionEvent updatedSubscriptionEvent = (UpdatedSubscriptionEvent) eventList.get(0);
       assertTranslatedEvent(updatedSubscriptionEvent);
    }
    
    @Test
    public void testNegative() {
        setUpMockToReturnNullValues();
        subscriptionUpdatedTranslator.translate(any(Node.class));
    }
    
    private void setUpMock() {
        when(subscriptionUpdateXpathExtractorMock.getNewAccId(any(Node.class))).thenReturn(ACCOUNT_ID);
        when(subscriptionUpdateXpathExtractorMock.getNewContractId(any(Node.class))).thenReturn(CONTRACT_ID);
        when(subscriptionUpdateXpathExtractorMock.getNewDirNumberId(any(Node.class))).thenReturn(DIRECTORY_NUMBER);
        when(subscriptionUpdateXpathExtractorMock.getNewSubscrHasSecretNumber(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        when(subscriptionUpdateXpathExtractorMock.getNewProductId(any(Node.class))).thenReturn(SUBSCRIPTION_TYPE);
        when(subscriptionUpdateXpathExtractorMock.getNewCustIdUser(any(Node.class))).thenReturn(USER_CUSTOMER_ID);
        when(subscriptionUpdateXpathExtractorMock.getNewSubscrValidToDate(any(Node.class))).thenReturn(EXPECTED_DATE);
        when(subscriptionUpdateXpathExtractorMock.getNewSubscrValidFromDate(any(Node.class))).thenReturn(EXPECTED_DATE);
        when(subscriptionUpdateXpathExtractorMock.getOldSubscrId(any(Node.class))).thenReturn(SUBSCRIPTION_ID);
        when(subscriptionTypeCache.get(any(String.class))).thenReturn(BASE_PRODUCT_ID);
    }

    private void assertTranslatedEvent(UpdatedSubscriptionEvent updatedSubscriptionEvent) {
        assertEquals("Unexpected account id", ACCOUNT_ID.getValue(), updatedSubscriptionEvent.getData().getAccountId());
        assertEquals("Unexpected contract id", CONTRACT_ID.getValue(), updatedSubscriptionEvent.getData().getContractId().toString());
        assertEquals("Unexpected directory number", DIRECTORY_NUMBER.getValue(), updatedSubscriptionEvent.getData().getMsisdn().toString());
        assertEquals("Expected to be secret number", Boolean.TRUE, updatedSubscriptionEvent.getData().getIsSecretNumber());
        assertEquals("Unexpected product id", BASE_PRODUCT_ID, updatedSubscriptionEvent.getData().getSubscriptionType());
        assertEquals("Unexpected user customer id", USER_CUSTOMER_ID.getValue(), updatedSubscriptionEvent.getData().getUserCustomerId());
        assertEquals("Unexpected valid to date", EXPECTED_DATE.getValue(), updatedSubscriptionEvent.getData().getValidToDate());
        assertEquals("Unexpected valid from date", EXPECTED_DATE.getValue(), updatedSubscriptionEvent.getData().getValidFromDate());
        assertEquals("Unexpected domain/subscription id", SUBSCRIPTION_ID.getValue(), updatedSubscriptionEvent.getDomainId());
    }

    private void setUpMockToReturnNullValues() {
        when(subscriptionUpdateXpathExtractorMock.getNewAccId(any(Node.class))).thenReturn(null);
        when(subscriptionUpdateXpathExtractorMock.getNewContractId(any(Node.class))).thenReturn(null);
        when(subscriptionUpdateXpathExtractorMock.getNewDirNumberId(any(Node.class))).thenReturn(null);
        when(subscriptionUpdateXpathExtractorMock.getNewSubscrHasSecretNumber(any(Node.class))).thenReturn(null);
        when(subscriptionUpdateXpathExtractorMock.getNewProductId(any(Node.class))).thenReturn(null);
        when(subscriptionUpdateXpathExtractorMock.getNewCustIdUser(any(Node.class))).thenReturn(null);
        when(subscriptionUpdateXpathExtractorMock.getNewSubscrValidToDate(any(Node.class))).thenReturn(null);
        when(subscriptionUpdateXpathExtractorMock.getNewSubscrValidFromDate(any(Node.class))).thenReturn(null);
    }
}
