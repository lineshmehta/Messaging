package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.subscription.SubscriptionChangeTypeEvent;
import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.suite.ServiceTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTests.class)
public class SubscriptionChangeTypeProducerTest {

    private static final Long SUBSCR_ID = 2L;

    private static final String S212_PRODUCT_ID = "345";

    @Mock
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Mock
    private SubscriptionTypeCache subscriptionTypeCache;

    private SubscriptionChangeTypeProducer subscriptionChangeTypeProducer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subscriptionChangeTypeProducer = new SubscriptionChangeTypeProducer();
        ReflectionTestUtils.setField(subscriptionChangeTypeProducer, "subscriptionUpdateXpathExtractor",subscriptionUpdateXpathExtractor);
        ReflectionTestUtils.setField(subscriptionChangeTypeProducer, "subscriptionTypeCache", subscriptionTypeCache);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(subscriptionUpdateXpathExtractor.getNewS212ProductId(any(Node.class))).thenReturn(
                XPathString.valueOf("subscription"));
        boolean isApplicable = subscriptionChangeTypeProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }
    
    @Test
    public void testProduceMessage() throws Exception {
        createSubscriptionTypeMock("trololol");
        SubscriptionChangeTypeEvent event = callProducerAndAssertDomainId();
        assertEquals("Unexpected SubscriptionType","trololol",event.getSubcriptionType());
    }

    @Test
    public void testIfSubTypeNotFoundInCache() throws Exception {
        createSubscriptionTypeMock(null);
        SubscriptionChangeTypeEvent event = callProducerAndAssertDomainId();
        assertNull(event.getSubcriptionType());
    }

    private void createSubscriptionTypeMock(String type) {
        when(subscriptionUpdateXpathExtractor.getOldSubscrId(any(Node.class))).thenReturn(XPathLong.valueOf(SUBSCR_ID));
        when(subscriptionUpdateXpathExtractor.getNewS212ProductId(any(Node.class))).thenReturn(XPathString.valueOf(S212_PRODUCT_ID));
        when(subscriptionTypeCache.get(S212_PRODUCT_ID)).thenReturn(type);
    }

    private SubscriptionChangeTypeEvent callProducerAndAssertDomainId() {
        SubscriptionChangeTypeEvent event = (SubscriptionChangeTypeEvent) (subscriptionChangeTypeProducer.produceMessage(any(Node.class))).get(0);
        assertEvent(event);
        return event;
    }

    private void assertEvent(SubscriptionChangeTypeEvent event) {
        assertEquals(SUBSCR_ID, event.getDomainId());

    }
}
