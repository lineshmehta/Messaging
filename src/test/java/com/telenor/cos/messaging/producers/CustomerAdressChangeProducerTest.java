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

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.customer.CustomerAddressChangeEvent;
import com.telenor.cos.messaging.handlers.CustomerAddressChangeHandler;
import com.telenor.cos.messaging.producers.xpath.CustomerUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
public class CustomerAdressChangeProducerTest {

    private Producer customerAddressChangeProducer;

    @Mock
    private CustomerUpdateXpathExtractor customerUpdateXpathExtractor;

    @Mock
    private CustomerAddressChangeHandler customerAddressChangeHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        customerAddressChangeProducer = new CustomerAddressChangeProducer();
        ReflectionTestUtils.setField(customerAddressChangeProducer, "customerUpdateXpathExtractor", customerUpdateXpathExtractor);
        ReflectionTestUtils.setField(customerAddressChangeProducer, "customerAddressChangeHandler", customerAddressChangeHandler);
    }

    @Test
    public void shouldBeApplicable() throws Exception{
        when(customerUpdateXpathExtractor.getNewPostcodeIdMain(any(Node.class))).thenReturn(XPathLong.valueOf(43L));
        boolean isApplicable = customerAddressChangeProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void shouldBeApplicable2() throws Exception{
        when(customerUpdateXpathExtractor.getNewAdressStreetNumber(any(Node.class))).thenReturn(XPathString.valueOf("3445"));
        boolean isApplicable = customerAddressChangeProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void shouldNotBeApplicable() throws Exception{
        when(customerUpdateXpathExtractor.getNewPostcodeIdMain(any(Node.class))).thenReturn(null);
        boolean isApplicable = customerAddressChangeProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void shouldCreateAdressChangeEvent() throws Exception {
        when(customerUpdateXpathExtractor.getOldCustomerId(any(Node.class))).thenReturn(XPathLong.valueOf(1L));
        when(customerUpdateXpathExtractor.getNewPostcodeIdMain(any(Node.class))).thenReturn(XPathLong.valueOf(4L));
        when(customerUpdateXpathExtractor.getNewPostcodeNameMain(any(Node.class))).thenReturn(XPathString.valueOf("post"));
        when(customerUpdateXpathExtractor.getNewAdressLineMain(any(Node.class))).thenReturn(XPathString.valueOf("line"));
        when(customerUpdateXpathExtractor.getNewAdressCOName(any(Node.class))).thenReturn(XPathString.valueOf("name"));
        when(customerUpdateXpathExtractor.getNewAdressStreetName(any(Node.class))).thenReturn(XPathString.valueOf("street"));
        when(customerUpdateXpathExtractor.getNewAdressStreetNumber(any(Node.class))).thenReturn(XPathString.valueOf("78"));

        List<Event> eventsList = customerAddressChangeProducer.produceMessage(any(Node.class));
        CustomerAddressChangeEvent event = (CustomerAddressChangeEvent) eventsList.get(0);
        assertNotNull(event);
        assertEquals(Long.valueOf("1"), event.getDomainId());
        CustomerAddress customerAdress = event.getCustomerAdress();
        assertNotNull(customerAdress);
        assertEquals("0004", customerAdress.getPostcodeIdMain());
        assertEquals("post", customerAdress.getPostcodeNameMain());
        assertEquals("line", customerAdress.getAddressLineMain());
        assertEquals("name", customerAdress.getAddressCoName());
        assertEquals("street", customerAdress.getAddressStreetName());
        assertEquals("78", customerAdress.getAddressStreetNumber());
    }
}
