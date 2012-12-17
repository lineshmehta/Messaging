package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionEquipmentUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class SubscriptionEquipmentDeleteProducerTest {

    public static final XPathLong SUBSCRIPTION_ID = XPathLong.valueOf(1234567);
    public static final XPathString IMSI = XPathString.valueOf("999999");

    @Autowired
    private SubscriptionEquipmentDeleteProducer subscriptionEquipmentDeleteProducer;

    @Mock
    private SubscriptionEquipmentUpdateXpathExtractor subscriptionEquipmentUpdateXpathExtractorMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subscriptionEquipmentDeleteProducer = new SubscriptionEquipmentDeleteProducer();
        ReflectionTestUtils.setField(subscriptionEquipmentDeleteProducer, "subscriptionEquipmentUpdateXpathExtractor", subscriptionEquipmentUpdateXpathExtractorMock);
    }

    @Test
      public void testIsNotApplicable(){
        when(subscriptionEquipmentUpdateXpathExtractorMock.getInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("N"));
        when(subscriptionEquipmentUpdateXpathExtractorMock.getValidToDate(any(Node.class))).thenReturn(new XPathDate(DateUtils.addDays(new Date(), 1)));
        assertFalse(subscriptionEquipmentDeleteProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testIsNotApplicableOpenEndDate(){
        when(subscriptionEquipmentUpdateXpathExtractorMock.getInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("N"));
        when(subscriptionEquipmentUpdateXpathExtractorMock.getValidToDate(any(Node.class))).thenReturn(null);
        assertFalse(subscriptionEquipmentDeleteProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testIsApplicableDeleted(){
        when(subscriptionEquipmentUpdateXpathExtractorMock.getInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        when(subscriptionEquipmentUpdateXpathExtractorMock.getValidToDate(any(Node.class))).thenReturn(new XPathDate(new DateTime().plusDays(1).toDate()));
        assertTrue(subscriptionEquipmentDeleteProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testIsApplicableExpired(){
        when(subscriptionEquipmentUpdateXpathExtractorMock.getInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("N"));
        when(subscriptionEquipmentUpdateXpathExtractorMock.getValidToDate(any(Node.class))).thenReturn(new XPathDate(new DateTime().minusDays(1).toDate()));
        assertTrue(subscriptionEquipmentDeleteProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage(){
        when(subscriptionEquipmentUpdateXpathExtractorMock.getSubscriptionIdOld(any(Node.class))).thenReturn(SUBSCRIPTION_ID);
        when(subscriptionEquipmentUpdateXpathExtractorMock.getImsiNumberIdOld(any(Node.class))).thenReturn(IMSI);

        SubscriptionEquipmentDeleteEvent event = (SubscriptionEquipmentDeleteEvent)subscriptionEquipmentDeleteProducer.produceMessage(any(Node.class)).get(0);
        assertEquals(IMSI.getValue(), event.getSubscriptionEquipment().getImsi());
        assertEquals(SUBSCRIPTION_ID.getValue(), event.getDomainId());
    }
}
