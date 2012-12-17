package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentNewEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionEquipmentNewXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class SubscriptionEquipmentNewProducerTest {

    public static final XPathLong SUBSCRIPTION_ID = XPathLong.valueOf(1234567);
    public static final XPathString IMSI = XPathString.valueOf("999999");

    @Autowired
    private SubscriptionEquipmentNewProducer subscriptionEquipmentNewProducer;

    @Mock
    private SubscriptionEquipmentNewXpathExtractor subscriptionEquipmentNewXpathExtractorMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subscriptionEquipmentNewProducer = new SubscriptionEquipmentNewProducer();
        ReflectionTestUtils.setField(subscriptionEquipmentNewProducer, "subscriptionEquipmentNewXpathExtractor", subscriptionEquipmentNewXpathExtractorMock);
    }

    @Test
    public void testIsApplicable(){
        when(subscriptionEquipmentNewXpathExtractorMock.getImsiNumberId(any(Node.class))).thenReturn(IMSI);
        assertTrue(subscriptionEquipmentNewProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testIsNotApplicable(){
        when(subscriptionEquipmentNewXpathExtractorMock.getImsiNumberId(any(Node.class))).thenReturn(null);
        assertFalse(subscriptionEquipmentNewProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage(){
        when(subscriptionEquipmentNewXpathExtractorMock.getSubscriptionId(any(Node.class))).thenReturn(SUBSCRIPTION_ID);
        when(subscriptionEquipmentNewXpathExtractorMock.getImsiNumberId(any(Node.class))).thenReturn(IMSI);
        SubscriptionEquipmentNewEvent event = (SubscriptionEquipmentNewEvent)subscriptionEquipmentNewProducer.produceMessage(any(Node.class)).get(0);
        assertEquals(IMSI.getValue(), event.getSubscriptionEquipment().getImsi());
        assertEquals(SUBSCRIPTION_ID.getValue(), event.getDomainId());
    }
}
