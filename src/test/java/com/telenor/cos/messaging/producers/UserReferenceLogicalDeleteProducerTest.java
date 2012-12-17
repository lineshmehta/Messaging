package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.userref.UserReferenceLogicalDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.UserReferenceUpdateXpathExtractor;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class UserReferenceLogicalDeleteProducerTest {

    @Mock
    private UserReferenceUpdateXpathExtractor userReferenceUpdateXpathExtractor;

    private UserReferenceLogicalDeleteProducer userReferenceLogicalDeleteProducer;

    private final static Long SUBSCRIPTION_ID = Long.valueOf("32870370");
    private final static String NUMBER_TYPE = "ES";
    private final static XPathString INFO_IS_DELETED = XPathString.valueOf("Y");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userReferenceLogicalDeleteProducer = new UserReferenceLogicalDeleteProducer();
        ReflectionTestUtils.setField(userReferenceLogicalDeleteProducer, "userReferenceUpdateXpathExtractor", userReferenceUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicableToProducerTrue() throws Exception {
        when(userReferenceUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(INFO_IS_DELETED);
        Boolean isApplicable = userReferenceLogicalDeleteProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testIsApplicableToProducerFalse() throws Exception {
        when(userReferenceUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(null);
        Boolean isApplicable = userReferenceLogicalDeleteProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void testCreateApplicableEvent() throws Exception {
        when(userReferenceUpdateXpathExtractor.getOldSusbcrId(any(Node.class))).thenReturn(XPathLong.valueOf(SUBSCRIPTION_ID));
        when(userReferenceUpdateXpathExtractor.getOldNumberTypeId(any(Node.class))).thenReturn(XPathString.valueOf(NUMBER_TYPE));
        UserReferenceLogicalDeleteEvent userReferenceLogicalDeleteEvent = (UserReferenceLogicalDeleteEvent) (userReferenceLogicalDeleteProducer.produceMessage(any(Node.class))).get(0);
        assertEquals("Unextected SubscriptionId",SUBSCRIPTION_ID, userReferenceLogicalDeleteEvent.getDomainId());
        assertEquals("Unextected NumberType",NUMBER_TYPE, userReferenceLogicalDeleteEvent.getNumberType());
    }
}
