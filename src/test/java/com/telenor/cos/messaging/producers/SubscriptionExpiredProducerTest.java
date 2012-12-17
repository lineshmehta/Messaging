package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.event.subscription.SubscriptionExpiredEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test {@link SubscriptionExpiredProducer}.
 */
@Category(ServiceTest.class)
public class SubscriptionExpiredProducerTest {

    private final static XPathLong EXPECTED_SUBSCRIPTION_ID = XPathLong.valueOf(666L);

    private final static XPathDate VALID_FROM_DATE = new XPathDate();

    private final static XPathDate VALID_TO_DATE = new XPathDate(DateUtils.addDays(VALID_FROM_DATE.getValue(), 1));
   
    private final static XPathString DK = XPathString.valueOf("DK");
    
    private final static XPathString MSIDN = XPathString.valueOf("95846785");

    private SubscriptionExpiredProducer subscriptionExpiredProducer;

    @Mock
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subscriptionExpiredProducer = new SubscriptionExpiredProducer();
        ReflectionTestUtils.setField(subscriptionExpiredProducer, "subscriptionUpdateXpathExtractor", subscriptionUpdateXpathExtractor);
    }
    
    @Test
    public void testIsApplicable() throws Exception{
        when(subscriptionUpdateXpathExtractor.getOldSubscrValidFromDate(any(Node.class))).thenReturn(VALID_FROM_DATE);
        when(subscriptionUpdateXpathExtractor.getNewSubscrValidToDate(any(Node.class))).thenReturn(VALID_TO_DATE);
        boolean isApplicable = subscriptionExpiredProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(subscriptionUpdateXpathExtractor.getOldSubscrId(any(Node.class))).thenReturn(EXPECTED_SUBSCRIPTION_ID);
        when(subscriptionUpdateXpathExtractor.getNewSubscrValidToDate(any(Node.class))).thenReturn(VALID_TO_DATE);
        when(subscriptionUpdateXpathExtractor.getOldSubscrTypeId(any(Node.class))).thenReturn(DK);
        when(subscriptionUpdateXpathExtractor.getOldDirNumberId(any(Node.class))).thenReturn(MSIDN);

        SubscriptionExpiredEvent event = (SubscriptionExpiredEvent) (subscriptionExpiredProducer.produceMessage(any(Node.class)).get(0));

        verify(subscriptionUpdateXpathExtractor).getNewSubscrValidToDate(any(Node.class));
        verify(subscriptionUpdateXpathExtractor).getOldSubscrId(any(Node.class));
        assertEquals("Unexpected subscription id", EXPECTED_SUBSCRIPTION_ID.getValue(), event.getDomainId());
        assertEquals("Unexpected valid to date", VALID_TO_DATE.getValue(), event.getValidToDate());
        assertEquals("DK", event.getSusbcrType());
        assertEquals("95846785", event.getMsisdn());

    }

    @Test(expected = CosMessagingException.class)
    public void testProduceMessageNegative() throws Exception {
        when(subscriptionUpdateXpathExtractor.getOldSubscrId(null)).thenThrow(new CosMessagingException("Oops", null));
        subscriptionExpiredProducer.produceMessage(null);
    }

}
