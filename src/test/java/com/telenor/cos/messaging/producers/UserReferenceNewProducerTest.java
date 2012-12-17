package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.userref.UserReferenceNewEvent;
import com.telenor.cos.messaging.producers.xpath.UserReferenceInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class UserReferenceNewProducerTest {

    private UserReferenceNewProducer userReferenceNewProducer;

    @Mock
    private UserReferenceInsertXpathExtractor userReferenceInsertXpathExtractorMock;

    private final static Long SUBSCRIPTION_ID = Long.valueOf("1");
    private static final Long SUBSCR_ID = Long.valueOf(1);
    private final static String USER_REF_DESCR = "TEST REF 1";
    private final static String EINVOICE_REF = "TEST EINVOICE REF";
    private final static String NUMBER_TYPE = "ES";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userReferenceNewProducer = new UserReferenceNewProducer();
        ReflectionTestUtils.setField(userReferenceNewProducer, "userReferenceInsertXpathExtractor",  userReferenceInsertXpathExtractorMock);
    }

    @Test
    public void isApplicableTrue() throws Exception {
        when(userReferenceInsertXpathExtractorMock.getSubscriptionId(any(Node.class))).thenReturn(XPathLong.valueOf(SUBSCRIPTION_ID));
        when(userReferenceInsertXpathExtractorMock.getNumberType(any(Node.class))).thenReturn(XPathString.valueOf(NUMBER_TYPE));
        assertTrue(userReferenceNewProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void isApplicableFalse() throws Exception {
        when(userReferenceInsertXpathExtractorMock.getSubscriptionId(any(Node.class))).thenReturn(null);
        when(userReferenceInsertXpathExtractorMock.getNumberType(any(Node.class))).thenReturn(null);
        assertFalse(userReferenceNewProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(userReferenceInsertXpathExtractorMock.getSubscriptionId(any(Node.class))).thenReturn(XPathLong.valueOf(SUBSCR_ID));
        when(userReferenceInsertXpathExtractorMock.getEInvoiceRef(any(Node.class))).thenReturn(XPathString.valueOf(EINVOICE_REF));
        when(userReferenceInsertXpathExtractorMock.getUserRefDescription(any(Node.class))).thenReturn(XPathString.valueOf(USER_REF_DESCR));
        when(userReferenceInsertXpathExtractorMock.getNumberType(any(Node.class))).thenReturn(XPathString.valueOf(NUMBER_TYPE));
        List<Event> translatedEventsList = userReferenceNewProducer.produceMessage(any(Node.class));
        UserReferenceNewEvent userReferenceNewEvent = (UserReferenceNewEvent)translatedEventsList.get(0);
        assertEquals("Unexpected Einvoice Ref",EINVOICE_REF,userReferenceNewEvent.getUserReference().getInvoiceRef());
        assertEquals("Unexpected UserRef Descr", USER_REF_DESCR, userReferenceNewEvent.getUserReference().getUserRefDescr());
        assertEquals("Unexpected NumberType",NUMBER_TYPE,userReferenceNewEvent.getUserReference().getNumberType());
    }
}
