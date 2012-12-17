package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionEquipmentUpdateXpathExtractor;
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
public class SubscriptionEquipmentUpdateProducerTest {

    public static final XPathLong SUBSCRIPTION_ID = XPathLong.valueOf(1234567);
    public static final XPathString IMSI = XPathString.valueOf("999999");

    @Autowired
    private SubscriptionEquipmentUpdateProducer subscriptionEquipmentUpdateProducer;

    @Mock
    private SubscriptionEquipmentUpdateXpathExtractor subscriptionEquipmentUpdateXpathExtractorMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subscriptionEquipmentUpdateProducer = new SubscriptionEquipmentUpdateProducer();
        ReflectionTestUtils.setField(subscriptionEquipmentUpdateProducer, "subscriptionEquipmentUpdateXpathExtractor", subscriptionEquipmentUpdateXpathExtractorMock);
    }

    @Test
     public void testIsApplicable(){
        when(subscriptionEquipmentUpdateXpathExtractorMock.getImsiNumberIdNew(any(Node.class))).thenReturn(IMSI);
        assertTrue(subscriptionEquipmentUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testIsNotApplicable(){
        when(subscriptionEquipmentUpdateXpathExtractorMock.getImsiNumberIdNew(any(Node.class))).thenReturn(null);
        assertFalse(subscriptionEquipmentUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage(){
        when(subscriptionEquipmentUpdateXpathExtractorMock.getSubscriptionIdOld(any(Node.class))).thenReturn(SUBSCRIPTION_ID);
        when(subscriptionEquipmentUpdateXpathExtractorMock.getImsiNumberIdNew(any(Node.class))).thenReturn(IMSI);

        SubscriptionEquipmentUpdateEvent event = (SubscriptionEquipmentUpdateEvent)subscriptionEquipmentUpdateProducer.produceMessage(any(Node.class)).get(0);
        assertEquals(IMSI.getValue(), event.getSubscriptionEquipment().getImsi());
        assertEquals(SUBSCRIPTION_ID.getValue(), event.getDomainId());
    }
}
