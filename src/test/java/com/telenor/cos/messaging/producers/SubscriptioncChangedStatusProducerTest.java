package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangedStatusEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class SubscriptioncChangedStatusProducerTest {

    private static final String SUBSCRIPTION_STATUS = "M";

    private static final Long SUBSCRIPTION_ID = 32143317L;

    private SubscriptionChangedStatusProducer subscriptionChangedStatusProducer;

    @Mock
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subscriptionChangedStatusProducer = new SubscriptionChangedStatusProducer();
        ReflectionTestUtils.setField(subscriptionChangedStatusProducer, "subscriptionUpdateXpathExtractor",
                subscriptionUpdateXpathExtractor);
    }

    @Test
    public void testProduceMessageBarred() throws Exception {
        setUpXpathExtractorMock(SUBSCRIPTION_STATUS);
        SubscriptionChangedStatusEvent event = (SubscriptionChangedStatusEvent) (subscriptionChangedStatusProducer.produceMessage(any(Node.class))).get(0);
        assertEvent(event, SUBSCRIPTION_STATUS);
    }

    @Test
    public void testProduceMessageNotBarred() throws Exception {
        setUpXpathExtractorMock(null);
        SubscriptionChangedStatusEvent event = (SubscriptionChangedStatusEvent) (subscriptionChangedStatusProducer.produceMessage(any(Node.class))).get(0);
        assertEvent(event, null);
    }

    private void setUpXpathExtractorMock(String expectedStatus) {
        when(subscriptionUpdateXpathExtractor.getOldSubscrId(any(Node.class))).thenReturn(XPathLong.valueOf(SUBSCRIPTION_ID));
        when(subscriptionUpdateXpathExtractor.getNewSubscrStatusId(any(Node.class))).thenReturn(XPathString.valueOf(expectedStatus));
    }

    private void assertEvent(SubscriptionChangedStatusEvent event, String expectedStatus) {
        assertEquals("Unexpected subscription id", SUBSCRIPTION_ID, event.getDomainId());
        assertEquals("Uexpected action", ACTION.STATUS_CHANGE, event.getAction());
        assertEquals("Unexpected status", expectedStatus, event.getStatus());
    }

}