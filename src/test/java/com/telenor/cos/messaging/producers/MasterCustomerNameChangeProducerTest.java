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

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.MasterCustomer;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNameChangeEvent;
import com.telenor.cos.messaging.producers.xpath.MasterCustomerUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
public class MasterCustomerNameChangeProducerTest {

    private Producer masterCustomerNameChangeProducer;
    
    @Mock
    private MasterCustomerUpdateXpathExtractor masterCustomerUpdateXpathExtractor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        masterCustomerNameChangeProducer = new MasterCustomerNameChangeProducer();
        ReflectionTestUtils.setField(masterCustomerNameChangeProducer, "masterCustomerUpdateXpathExtractor", masterCustomerUpdateXpathExtractor);
    }

    @Test
    public void shouldReturnTrueOnIsApplicable() throws Exception {
        when(masterCustomerUpdateXpathExtractor.getNewFirstName(any(Node.class))).thenReturn(XPathString.valueOf("Ole"));
        boolean isApplcable = masterCustomerNameChangeProducer.isApplicable(any(Node.class));
        assertTrue(isApplcable);
    }

    @Test
    public void shouldReturnFalseOnIsApplicable() throws Exception {
        when(masterCustomerUpdateXpathExtractor.getNewFirstName(any(Node.class))).thenReturn(null);
        boolean isApplcable = masterCustomerNameChangeProducer.isApplicable(any(Node.class));
        assertFalse(isApplcable);
    }

    @Test
    public void createApplicableEventWithFirstNameChange() throws Exception {
        when(masterCustomerUpdateXpathExtractor.getOldLastName(any(Node.class))).thenReturn(XPathString.valueOf("Halvorsen"));
        when(masterCustomerUpdateXpathExtractor.getNewFirstName(any(Node.class))).thenReturn(XPathString.valueOf("Knut"));
        when(masterCustomerUpdateXpathExtractor.getOldMiddleName(any(Node.class))).thenReturn(XPathString.valueOf("Ole"));
        translateAndAssertMasterCustomer();
    }

    @Test
    public void createApplicableEventWithMiddleNameChange() throws Exception {
        when(masterCustomerUpdateXpathExtractor.getOldLastName(any(Node.class))).thenReturn(XPathString.valueOf("Halvorsen"));
        when(masterCustomerUpdateXpathExtractor.getOldFirstName(any(Node.class))).thenReturn(XPathString.valueOf("Knut"));
        when(masterCustomerUpdateXpathExtractor.getNewMiddleName(any(Node.class))).thenReturn(XPathString.valueOf("Ole"));
        translateAndAssertMasterCustomer();
    }
    
    @Test
    public void createApplicableEventWithLastNameChange() throws Exception {
        when(masterCustomerUpdateXpathExtractor.getNewLastName(any(Node.class))).thenReturn(XPathString.valueOf("Halvorsen"));
        when(masterCustomerUpdateXpathExtractor.getOldFirstName(any(Node.class))).thenReturn(XPathString.valueOf("Knut"));
        when(masterCustomerUpdateXpathExtractor.getOldMiddleName(any(Node.class))).thenReturn(XPathString.valueOf("Ole"));
        translateAndAssertMasterCustomer();
    }

    private void translateAndAssertMasterCustomer() {
        List<Event> eventsList = masterCustomerNameChangeProducer.produceMessage(any(Node.class));
        MasterCustomerNameChangeEvent event = (MasterCustomerNameChangeEvent) eventsList.get(0);
        MasterCustomer masterCustomerName = event.getMasterCustomerName();
        assertMasterCustomerName(masterCustomerName);
    }
    private void assertMasterCustomerName(MasterCustomer masterCustomerName) {
        assertNotNull(masterCustomerName);
        assertEquals("Knut", masterCustomerName.getFirstName());
        assertEquals("Ole", masterCustomerName.getMiddleName());
        assertEquals("Halvorsen", masterCustomerName.getLastName());
    }

    @Test
    public void shouldReturnCustomerNameChangeEvent() throws Exception {
        when(masterCustomerUpdateXpathExtractor.getNewFirstName(any(Node.class))).thenReturn(XPathString.valueOf("Knut"));
        when(masterCustomerUpdateXpathExtractor.getNewMiddleName(any(Node.class))).thenReturn(XPathString.valueOf("Ole"));
        when(masterCustomerUpdateXpathExtractor.getNewLastName(any(Node.class))).thenReturn(XPathString.valueOf("Halvorsen"));
        List<Event> eventsList = masterCustomerNameChangeProducer.produceMessage(any(Node.class));
        MasterCustomerNameChangeEvent event = (MasterCustomerNameChangeEvent) eventsList.get(0);
        MasterCustomer masterCustomerName = event.getMasterCustomerName();
        assertNotNull(masterCustomerName);
        assertEquals("Knut", masterCustomerName.getFirstName());
        assertEquals("Ole", masterCustomerName.getMiddleName());
        assertEquals("Halvorsen", masterCustomerName.getLastName());
    }
}
