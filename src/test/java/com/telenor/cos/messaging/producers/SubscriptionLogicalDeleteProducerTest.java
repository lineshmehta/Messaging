package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.subscription.SubscriptionLogicalDeleteEvent;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class SubscriptionLogicalDeleteProducerTest {

    private SubscriptionLogicalDeleteProducer producer;

    @Mock
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    private static final XPathString INFO_IS_DELETED = XPathString.valueOf("Y");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        producer = new SubscriptionLogicalDeleteProducer();
        ReflectionTestUtils.setField(producer, "subscriptionUpdateXpathExtractor", subscriptionUpdateXpathExtractor);
    }

    @Test
    public void isApplicable() throws Exception {
        when(subscriptionUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(INFO_IS_DELETED);
        boolean isApplicable = producer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void isNotApplicable() throws Exception {
        when(subscriptionUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(null);
        boolean isApplicable = producer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(subscriptionUpdateXpathExtractor.getOldSubscrId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        SubscriptionLogicalDeleteEvent event = (SubscriptionLogicalDeleteEvent) (producer
                .produceMessage(any(Node.class))).get(0);
        assertEquals((Long) 32143317L, event.getDomainId());
        assertEquals(ACTION.LOGICAL_DELETE, event.getAction());
    }

}
