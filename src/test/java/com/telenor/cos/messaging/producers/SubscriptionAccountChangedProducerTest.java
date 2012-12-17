package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.subscription.SubscriptionChangedAccountEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.test.category.ServiceTest;
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
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class SubscriptionAccountChangedProducerTest {

    private static final XPathLong ACCOUNT_ID = XPathLong.valueOf("999999001");

    private static final XPathLong SUBSCRIPTOIN_ID = XPathLong.valueOf("32143317");

    @Mock
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    private SubscriptionAccountChangedProducer subscriptionAccountChangedProducer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subscriptionAccountChangedProducer = new SubscriptionAccountChangedProducer();
        ReflectionTestUtils.setField(subscriptionAccountChangedProducer, "subscriptionUpdateXpathExtractor", subscriptionUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(subscriptionUpdateXpathExtractor.getNewAccId(any(Node.class))).thenReturn(
                XPathLong.valueOf("123"));
        boolean isApplicable = subscriptionAccountChangedProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(subscriptionUpdateXpathExtractor.getOldSubscrId(any(Node.class))).thenReturn(SUBSCRIPTOIN_ID);
        when(subscriptionUpdateXpathExtractor.getNewAccId(any(Node.class))).thenReturn(ACCOUNT_ID);
        SubscriptionChangedAccountEvent event = (SubscriptionChangedAccountEvent) (subscriptionAccountChangedProducer.produceMessage(any(Node.class)).get(0));
        assertEquals(SUBSCRIPTOIN_ID.getValue(), event.getDomainId());
        assertEquals(ACCOUNT_ID.getValue(), event.getAccountId());
    }
}
