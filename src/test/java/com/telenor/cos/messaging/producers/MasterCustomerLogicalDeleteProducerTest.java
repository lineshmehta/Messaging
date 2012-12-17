package com.telenor.cos.messaging.producers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.MasterCustomerUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
public class MasterCustomerLogicalDeleteProducerTest {

    private MasterCustomerLogicalDeleteProducer masterCustomerLogicalDeleteProducer;

    @Mock
    private MasterCustomerUpdateXpathExtractor masterCustomerUpdateXpathExtractor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        masterCustomerLogicalDeleteProducer = new MasterCustomerLogicalDeleteProducer();
        ReflectionTestUtils.setField(masterCustomerLogicalDeleteProducer, "masterCustomerUpdateXpathExtractor", masterCustomerUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(masterCustomerUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        Boolean isApplicable = masterCustomerLogicalDeleteProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testIsNotApplicable() throws Exception {
        when(masterCustomerUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(null);
        Boolean isApplicable = masterCustomerLogicalDeleteProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void shouldReturnMasterCustomerLogicalDeleteEvent() throws Exception {
        when(masterCustomerUpdateXpathExtractor.getOldMasterCustomerId(any(Node.class))).thenReturn(XPathLong.valueOf("31415926"));
        List<Event> eventsList = masterCustomerLogicalDeleteProducer.produceMessage(any(Node.class));
        MasterCustomerLogicalDeleteEvent event = (MasterCustomerLogicalDeleteEvent) eventsList.get(0);

        assertEquals((Long) 31415926L, event.getDomainId());
        assertEquals(Event.ACTION.LOGICAL_DELETE, event.getAction());
    }

}
