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
import com.telenor.cos.messaging.event.customer.CustomerNewEvent;
import com.telenor.cos.messaging.handlers.CustomerNewHandler;
import com.telenor.cos.messaging.producers.xpath.CustomerInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
public class CustomerNewProducerTest {

    private static final XPathLong customerId = XPathLong.valueOf(123);
    private static final XPathString firstName = XPathString.valueOf("Baba");
    private static final XPathString middleName = XPathString.valueOf("Prakash");
    private static final XPathString lastName = XPathString.valueOf("Dabbara");
    private static final XPathLong masterCustId = XPathLong.valueOf(9999902);
    private static final XPathLong custUnitNumber = XPathLong.valueOf(9888777);
    private static final XPathLong custPostcodeIdMain = XPathLong.valueOf("450");
    private static final String formattedPostCodeIdMain = "0450";
    private static final XPathString postcodeNameMain = XPathString.valueOf("Kirkevein");
    private static final XPathString addressLineMain = XPathString.valueOf("UllevÃ¥l");
    private static final XPathString addressCOName = XPathString.valueOf("Telenor");
    private static final XPathString addressStreetName = XPathString.valueOf("Solli");
    private static final XPathString addressStreetNumber = XPathString.valueOf("13");

    private Producer customerNewProducer;

    @Mock
    private CustomerInsertXpathExtractor customerInsertXpathExtractor;

    @Mock
    private CustomerNewHandler customerNewHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        customerNewProducer = new CustomerNewProducer();
        ReflectionTestUtils.setField(customerNewProducer, "customerInsertXpathExtractor", customerInsertXpathExtractor);
        ReflectionTestUtils.setField(customerNewProducer, "customerNewHandler",customerNewHandler)
        ;
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(customerInsertXpathExtractor.getCustomerId(any(Node.class))).thenReturn(XPathLong.valueOf(55L));
        boolean isApplicable = customerNewProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testIsNotApplicable() throws Exception {
        when(customerInsertXpathExtractor.getCustomerId(any(Node.class))).thenReturn(null);
        boolean isApplicable = customerNewProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void testNewCustomerEvent() throws Exception {
        when(customerInsertXpathExtractor.getCustomerId(any(Node.class))).thenReturn(customerId);
        when(customerInsertXpathExtractor.getCustomerName(any(Node.class))).thenReturn(firstName);
        when(customerInsertXpathExtractor.getCustomerMidddleName(any(Node.class))).thenReturn(middleName);
        when(customerInsertXpathExtractor.getCustomerLastName(any(Node.class))).thenReturn(lastName);
        when(customerInsertXpathExtractor.getMasterCustomerId(any(Node.class))).thenReturn(masterCustId);
        when(customerInsertXpathExtractor.getCustomerUnitNumber(any(Node.class))).thenReturn(custUnitNumber);
        when(customerInsertXpathExtractor.getPostcodeIdMain(any(Node.class))).thenReturn(custPostcodeIdMain);
        when(customerInsertXpathExtractor.getPostcodeNameMain(any(Node.class))).thenReturn(postcodeNameMain);
        when(customerInsertXpathExtractor.getAddressLineMain(any(Node.class))).thenReturn(addressLineMain);
        when(customerInsertXpathExtractor.getAddressCOName(any(Node.class))).thenReturn(addressCOName);
        when(customerInsertXpathExtractor.getAddressStreetName(any(Node.class))).thenReturn(addressStreetName);
        when(customerInsertXpathExtractor.getAddressStreetNumber(any(Node.class))).thenReturn(addressStreetNumber);

        List<Event> eventsList = customerNewProducer.produceMessage(any(Node.class));
        CustomerNewEvent translatedEvent =(CustomerNewEvent) eventsList.get(0);
        assertEquals(customerId.getValue(), translatedEvent.getCustomerName().getCustomerId());
        assertEquals(firstName.getValue(), translatedEvent.getCustomerName().getFirstName());
        assertEquals(middleName.getValue() ,translatedEvent.getCustomerName().getMiddleName());
        assertEquals(lastName.getValue(), translatedEvent.getCustomerName().getLastName());
        assertEquals(masterCustId.getValue(), translatedEvent.getCustomerName().getMasterCustomerId());
        assertEquals(custUnitNumber.getValue(), translatedEvent.getCustomerName().getCustUnitNumber());

        assertEquals(customerId.getValue(), translatedEvent.getCustomerAddress().getCustomerId());
        assertEquals(formattedPostCodeIdMain, translatedEvent.getCustomerAddress().getPostcodeIdMain());
        assertEquals(postcodeNameMain.getValue().toString(), translatedEvent.getCustomerAddress().getPostcodeNameMain());
        assertEquals(addressLineMain.getValue(), translatedEvent.getCustomerAddress().getAddressLineMain());
        assertEquals(addressCOName.getValue(), translatedEvent.getCustomerAddress().getAddressCoName());
        assertEquals(addressStreetName.getValue(), translatedEvent.getCustomerAddress().getAddressStreetName());
        assertEquals(addressStreetNumber.getValue(), translatedEvent.getCustomerAddress().getAddressStreetNumber());
    }
}
