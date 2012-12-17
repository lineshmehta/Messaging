package com.telenor.cos.messaging.producers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.customer.CustomerNameChangeEvent;
import com.telenor.cos.messaging.handlers.CustomerNameChangeHandler;
import com.telenor.cos.messaging.producers.xpath.CustomerUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
public class CustomerNameChangeProducerTest {

    private Producer customerNameChangeProducer;

    @Mock
    private CustomerUpdateXpathExtractor customerUpdateXpathExtractor;

    @Mock
    private CustomerNameChangeHandler customerNameChangeHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        customerNameChangeProducer = new CustomerNameChangeProducer();
        ReflectionTestUtils.setField(customerNameChangeProducer, "customerUpdateXpathExtractor", customerUpdateXpathExtractor);
        ReflectionTestUtils.setField(customerNameChangeProducer, "customerNameChangeHandler",customerNameChangeHandler);
    }

    @Test
    public void shouldReturnTrueOnIsApplicable() throws Exception {
        when(customerUpdateXpathExtractor.getNewCustomerFirstName(any(Node.class))).thenReturn(XPathString.valueOf("Ole"));
        boolean isApplcable = customerNameChangeProducer.isApplicable(any(Node.class));
        assertTrue(isApplcable);
    }

    @Test
    public void shouldReturnFalseOnIsApplicable() throws Exception {
        when(customerUpdateXpathExtractor.getNewCustomerFirstName(any(Node.class))).thenReturn(null);
        boolean isApplcable = customerNameChangeProducer.isApplicable(any(Node.class));
        assertFalse(isApplcable);
    }

    @Test
    public void shouldReturnCustomerNameChangeEvent() throws Exception{
        when(customerUpdateXpathExtractor.getOldCustomerId(any(Node.class))).thenReturn(XPathLong.valueOf(10L));
        when(customerUpdateXpathExtractor.getNewCustomerFirstName(any(Node.class))).thenReturn(XPathString.valueOf("Knut"));
        when(customerUpdateXpathExtractor.getNewCustomerMidddleName(any(Node.class))).thenReturn(XPathString.valueOf("Ole"));
        when(customerUpdateXpathExtractor.getNewCustomerLastName(any(Node.class))).thenReturn(XPathString.valueOf("Halvorsen"));
        when(customerUpdateXpathExtractor.getNewMasterCustomerId(any(Node.class))).thenReturn(XPathLong.valueOf(9889));
        when(customerUpdateXpathExtractor.getNewCustomerUnitNumber(any(Node.class))).thenReturn(XPathLong.valueOf(9889));
        List<Event> eventsList = customerNameChangeProducer.produceMessage(any(Node.class));
        CustomerNameChangeEvent event = (CustomerNameChangeEvent) eventsList.get(0);
        assertEquals(Long.valueOf(10), event.getDomainId());
        CustomerName customer = event.getCustomerName();
        assertNotNull(customer);
        assertEquals("Knut", customer.getFirstName());
        assertEquals("Ole", customer.getMiddleName());
        assertEquals("Halvorsen", customer.getLastName());
        assertEquals(Long.valueOf(9889), customer.getMasterCustomerId());
        assertEquals(Long.valueOf(9889), customer.getCustUnitNumber());
    }
}
