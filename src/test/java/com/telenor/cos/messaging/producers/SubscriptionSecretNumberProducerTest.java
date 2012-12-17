package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.subscription.SubscriptionSecretNumberEvent;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class SubscriptionSecretNumberProducerTest {

    private SubscriptionSecretNumberProducer producer;

    @Mock
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        producer = new SubscriptionSecretNumberProducer();
        ReflectionTestUtils.setField(producer, "subscriptionUpdateXpathExtractor",
                subscriptionUpdateXpathExtractor);
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(subscriptionUpdateXpathExtractor.getOldSubscrId(any(Node.class))).thenReturn(XPathLong.valueOf(1234567));
        when(subscriptionUpdateXpathExtractor.getNewSubscrHasSecretNumber(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        SubscriptionSecretNumberEvent event = (SubscriptionSecretNumberEvent) (producer.produceMessage(any(Node.class))).get(0);
        assertEquals((Long) 1234567L, event.getDomainId());
        assertEquals(ACTION.SECRET_NUMBER, event.getAction());
        assertTrue(event.isSecretNumber());
    }
}
