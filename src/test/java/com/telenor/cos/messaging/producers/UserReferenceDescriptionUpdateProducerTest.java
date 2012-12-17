package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.userref.UserReferenceDescriptionUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.UserReferenceUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.messaging.util.TestHelper;
import com.telenor.cos.messaging.util.UserReferenceTestHelper;
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
public class UserReferenceDescriptionUpdateProducerTest {

    @Mock
    private UserReferenceUpdateXpathExtractor userReferenceUpdateXpathExtractor;

    private UserReferenceDescriptionUpdateProducer userReferenceDescriptionUpdateProducer;

    private final static Long SUBSCRIPTION_ID = Long.valueOf("32870370");
    private final static String USER_REF_DESCR = "TEST REF 2";
    private final static String NUMBER_TYPE = "ES";

    private TestHelper testHelper = new TestHelper();
    private Node invoiceRefDoc;
    private Node userRefDescrDoc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userReferenceDescriptionUpdateProducer = new UserReferenceDescriptionUpdateProducer();
        ReflectionTestUtils.setField(userReferenceDescriptionUpdateProducer, "userReferenceUpdateXpathExtractor", userReferenceUpdateXpathExtractor);
        invoiceRefDoc = testHelper.fileToDom(UserReferenceTestHelper.INVOICEREF_UPDATE_XML);
        userRefDescrDoc = testHelper.fileToDom(UserReferenceTestHelper.USERREF_DESC_UPDATE_XML);
    }

    @Test
    public void testIsApplicableToProducerTrue() throws Exception {
        when(userReferenceUpdateXpathExtractor.getNewUserRefDescr(any(Node.class))).thenReturn(XPathString.valueOf(USER_REF_DESCR));
        Boolean isApplicable = userReferenceDescriptionUpdateProducer.isApplicable(userRefDescrDoc);
        assertTrue(isApplicable);
    }

    @Test
    public void testIsApplicableToProducerFalse() throws Exception {
        when(userReferenceUpdateXpathExtractor.getNewUserRefDescr(any(Node.class))).thenReturn(null);
        Boolean isApplicable = userReferenceDescriptionUpdateProducer.isApplicable(invoiceRefDoc);
        assertFalse(isApplicable);
    }

    @Test
    public void testCreateApplicableEvent() throws Exception {
        when(userReferenceUpdateXpathExtractor.getNewUserRefDescr(any(Node.class))).thenReturn(XPathString.valueOf(USER_REF_DESCR));
        when(userReferenceUpdateXpathExtractor.getOldSusbcrId(any(Node.class))).thenReturn(XPathLong.valueOf(SUBSCRIPTION_ID));
        when(userReferenceUpdateXpathExtractor.getOldNumberTypeId(any(Node.class))).thenReturn(XPathString.valueOf(NUMBER_TYPE));
        UserReferenceDescriptionUpdateEvent userReferenceDescrUpdateEvent = (UserReferenceDescriptionUpdateEvent) (userReferenceDescriptionUpdateProducer.produceMessage(userRefDescrDoc)).get(0);
        assertEquals("Unextected User Reference Descr",USER_REF_DESCR, userReferenceDescrUpdateEvent.getUserRefDescr());
        assertEquals("Unextected SubscriptionId",SUBSCRIPTION_ID, userReferenceDescrUpdateEvent.getDomainId());
    }
}
