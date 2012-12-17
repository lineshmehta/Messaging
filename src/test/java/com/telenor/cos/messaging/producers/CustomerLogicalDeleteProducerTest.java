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
import com.telenor.cos.messaging.event.customer.CustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.handlers.CustomerLogicalDeleteHandler;
import com.telenor.cos.messaging.producers.xpath.CustomerUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
public class CustomerLogicalDeleteProducerTest {

    private Producer customerLogicalDeleteProducer;

    @Mock
    private CustomerUpdateXpathExtractor customerUpdateXpathExtractor;

    @Mock
    private CustomerLogicalDeleteHandler customerLogicalDeleteHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        customerLogicalDeleteProducer = new CustomerLogicalDeleteProducer();
        ReflectionTestUtils.setField(customerLogicalDeleteProducer, "customerUpdateXpathExtractor", customerUpdateXpathExtractor);
        ReflectionTestUtils.setField(customerLogicalDeleteProducer, "customerLogicalDeleteHandler", customerLogicalDeleteHandler);
    }

    @Test
    public void isApplicableIstrueForChange() throws Exception {
        when(customerUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        Boolean b = customerLogicalDeleteProducer.isApplicable(any(Node.class));
        assertTrue(b);
    }

    @Test
    public void isApplicableIsFalseForChange() throws Exception {
        when(customerUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(null);
        Boolean b = customerLogicalDeleteProducer.isApplicable(any(Node.class));
        assertFalse(b);
    }

    @Test
    public void shouldReturnSusbcriptionLogicalDeleteEvent() throws Exception {
        when(customerUpdateXpathExtractor.getOldCustomerId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        List<Event> eventsList = customerLogicalDeleteProducer.produceMessage(any(Node.class));
        CustomerLogicalDeleteEvent event = (CustomerLogicalDeleteEvent) eventsList.get(0);
        assertEquals((Long) 32143317L, event.getDomainId());
        assertEquals(Event.ACTION.LOGICAL_DELETE, event.getAction());
    }
}
