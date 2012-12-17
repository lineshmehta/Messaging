package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.MobileOfficeUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class MobileOfficeUpdateProducerTest {

    @Mock
    private MobileOfficeUpdateXpathExtractor mobileOfficeUpdateXpathExtractor;

    private MobileOfficeUpdateProducer mobileOfficeUpdateProducer;

    private final static String OLD_DIRECTORY_NUMBER = "111222555444";
    private final static String NEW_EXTENSION_NUMBER = "45678955";
    private final static String OLD_EXTENSION_NUMBER = "45678944";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mobileOfficeUpdateProducer = new MobileOfficeUpdateProducer();
        ReflectionTestUtils.setField(mobileOfficeUpdateProducer, "mobileOfficeUpdateXpathExtractor", mobileOfficeUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicableToProducerTrue() throws Exception {
        when(mobileOfficeUpdateXpathExtractor.getExtensionNumber(any(Node.class))).thenReturn(XPathString.valueOf(NEW_EXTENSION_NUMBER));
        when(mobileOfficeUpdateXpathExtractor.getExtensionNumberOld(any(Node.class))).thenReturn(XPathString.valueOf(OLD_EXTENSION_NUMBER));
        Boolean isApplicable = mobileOfficeUpdateProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testIsApplicableToProducerFalse() throws Exception {
        when(mobileOfficeUpdateXpathExtractor.getExtensionNumber(any(Node.class))).thenReturn(null);
        when(mobileOfficeUpdateXpathExtractor.getExtensionNumberOld(any(Node.class))).thenReturn(XPathString.valueOf(OLD_EXTENSION_NUMBER));
        Boolean isApplicable = mobileOfficeUpdateProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }
    
    
    @Test
    public void testCreateApplicableEvent() throws Exception {
        when(mobileOfficeUpdateXpathExtractor.getDirectoryNumberOld(any(Node.class))).thenReturn(XPathString.valueOf(OLD_DIRECTORY_NUMBER));
        when(mobileOfficeUpdateXpathExtractor.getExtensionNumberOld(any(Node.class))).thenReturn(XPathString.valueOf(OLD_EXTENSION_NUMBER));
        when(mobileOfficeUpdateXpathExtractor.getExtensionNumber(any(Node.class))).thenReturn(XPathString.valueOf(NEW_EXTENSION_NUMBER));
        MobileOfficeUpdateEvent mobileOfficeDeleteEvent = (MobileOfficeUpdateEvent) (mobileOfficeUpdateProducer.produceMessage(any(Node.class))).get(0);
        
        assertEquals("Unexpected directory number id", OLD_DIRECTORY_NUMBER, mobileOfficeDeleteEvent.getDirectoryNumber());
        assertEquals("Unexpected extension number", OLD_EXTENSION_NUMBER, mobileOfficeDeleteEvent.getOldExtentionNumber());
        assertEquals("Unexpected extension number", NEW_EXTENSION_NUMBER, mobileOfficeDeleteEvent.getExtensionNumber());
    }

}
