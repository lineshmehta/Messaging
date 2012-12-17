package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeNewEvent;
import com.telenor.cos.messaging.producers.xpath.MobileOfficeInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class MobileOfficeNewProducerTest {

    private MobileOfficeNewProducer mobileOfferNewProducer;

    private static final XPathString EXTENSION_NUMBER = XPathString.valueOf("456789123");
    private static final XPathString DIRECTORY_NUMBER = XPathString.valueOf("111111111");

    @Mock
    private MobileOfficeInsertXpathExtractor mobileOfferInsertXpathExtractor;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mobileOfferNewProducer = new MobileOfficeNewProducer();
        ReflectionTestUtils.setField(mobileOfferNewProducer, "mobileOfferInsertXpathExtractor", mobileOfferInsertXpathExtractor);
    }
    
    @Test
    public void testIsApplicable() {
        when(mobileOfferInsertXpathExtractor.getDirectoryNumber(any(Node.class))).thenReturn(XPathString.valueOf("123456789"));
        when(mobileOfferInsertXpathExtractor.getExtensionNumber(any(Node.class))).thenReturn(XPathString.valueOf("45"));
        boolean isApplicable = mobileOfferNewProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }
    
    @Test
    public void testIsNotApplicable() {
        when(mobileOfferInsertXpathExtractor.getDirectoryNumber(any(Node.class))).thenReturn(XPathString.valueOf("123456789"));
        when(mobileOfferInsertXpathExtractor.getExtensionNumber(any(Node.class))).thenReturn(null);
        boolean isApplicable = mobileOfferNewProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void shouldProduceEventListOfMObileOffer(){
        when(mobileOfferInsertXpathExtractor.getDirectoryNumber(any(Node.class))).thenReturn(DIRECTORY_NUMBER);
        when(mobileOfferInsertXpathExtractor.getExtensionNumber(any(Node.class))).thenReturn(EXTENSION_NUMBER);
        List<Event> eventList = mobileOfferNewProducer.produceMessage(any(Node.class));
        assertNotNull(eventList);
        MobileOfficeNewEvent event = (MobileOfficeNewEvent) eventList.get(0);
        assertNotNull(event);
        assertEquals(DIRECTORY_NUMBER.getValue(), event.getDirectoryNumber());
        assertEquals(EXTENSION_NUMBER.getValue(), event.getExtensionNumber());
    }
}
