package com.telenor.cos.messaging.routers.aggregation.translators;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.event.Subscription;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.producers.xpath.UserReferenceInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link com.telenor.cos.messaging.routers.aggregation.translators.NewUserReferenceAggregationTranslator}
 * @author Babaprakash D
 *
 */
public class NewUserReferenceAggregationTranslatorTest {

    @Mock
    private UserReferenceInsertXpathExtractor userReferenceInsertXpathExtractor;

    private NewUserReferenceAggregationTranslator newUserReferenceAggregateTranslator;

    private static final Long SUBSCR_ID = Long.valueOf(1);
    private final static String USER_REF_DESCR = "TEST REF 1";
    private final static String EINVOICE_REF = "TEST EINVOICE REF";
    private final static String NUMBER_TYPE_TM = "TM";
    private final static String NUMBER_TYPE_ES = "ES";
    private Subscription subscription;
    private NewSubscriptionEvent newSubscriptionEvent;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        newUserReferenceAggregateTranslator = new NewUserReferenceAggregationTranslator();
        ReflectionTestUtils.setField(newUserReferenceAggregateTranslator, "userReferenceInsertXpathExtractor", userReferenceInsertXpathExtractor);
        subscription = new Subscription();
        subscription.setOwnerSubscriptionId(SUBSCR_ID);
        when(userReferenceInsertXpathExtractor.getEInvoiceRef(any(Node.class))).thenReturn(XPathString.valueOf(EINVOICE_REF));
        when(userReferenceInsertXpathExtractor.getUserRefDescription(any(Node.class))).thenReturn(XPathString.valueOf(USER_REF_DESCR));
        newSubscriptionEvent = new NewSubscriptionEvent(SUBSCR_ID, subscription);
    }

    @Test
    public void testTranslateWithNumberTypeTM() throws Exception {
        when(userReferenceInsertXpathExtractor.getNumberType(any(Node.class))).thenReturn(XPathString.valueOf(NUMBER_TYPE_TM));
        NewSubscriptionEvent eventAfterTranaslation =(NewSubscriptionEvent)newUserReferenceAggregateTranslator.translate(newSubscriptionEvent, any(Node.class));
        assertNull(eventAfterTranaslation.getData().getInvoiceReference());
        assertEquals("Unexpected UserRef Description",USER_REF_DESCR,eventAfterTranaslation.getData().getUserReferenceDescription());
    }

    @Test
    public void testTranslateWithNumberTypeES() throws Exception {
        when(userReferenceInsertXpathExtractor.getNumberType(any(Node.class))).thenReturn(XPathString.valueOf(NUMBER_TYPE_ES));
        NewSubscriptionEvent eventAfterTranaslation =(NewSubscriptionEvent)newUserReferenceAggregateTranslator.translate(newSubscriptionEvent, any(Node.class));
        assertEquals("Unexpected Einvoice Ref",EINVOICE_REF,eventAfterTranaslation.getData().getInvoiceReference());
        assertEquals("Unexpected UserRef",USER_REF_DESCR,eventAfterTranaslation.getData().getUserReference());
    }

    @Test
    public void testTranslateNewSubscriptionEventIsNull() throws Exception {
        try {
            newUserReferenceAggregateTranslator.translate(null, any(Node.class));
        } catch(CosMessagingException e) {
            assertTrue(e.getMessage().contains("NewSubscriptionEvent is empty"));
        }
    }
    
    @Test
    public void testTranslateNewSubscriptionEventGetDataIsNull() throws Exception {

        NewSubscriptionEvent subscriptionEventDataIsNull = new NewSubscriptionEvent(SUBSCR_ID, null);
        try {
            newUserReferenceAggregateTranslator.translate(subscriptionEventDataIsNull, any(Node.class));
        } catch(CosMessagingException e) {
            assertTrue(e.getMessage().contains("NewSubscriptionEvent.getData() returns empty (NULL) dataset."));
        }



    }
}
