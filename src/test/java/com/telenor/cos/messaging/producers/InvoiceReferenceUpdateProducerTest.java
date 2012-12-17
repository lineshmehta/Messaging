package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.userref.InvoiceReferenceUpdateEvent;
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
public class InvoiceReferenceUpdateProducerTest {

    @Mock
    private UserReferenceUpdateXpathExtractor userReferenceUpdateXpathExtractor;

    private InvoiceReferenceUpdateProducer invoiceReferenceUpdateProducer;

    private static final String EINVOICE_REF = "Test";
    private static final String NUMBER_TYPE = "ES";
    private final static Long SUBSCRIPTION_ID = Long.valueOf("32870370");

    private TestHelper testHelper = new TestHelper();
    private Node invoiceRefDoc;
    private Node userRefDescrDoc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        invoiceReferenceUpdateProducer = new InvoiceReferenceUpdateProducer();
        ReflectionTestUtils.setField(invoiceReferenceUpdateProducer, "userReferenceUpdateXpathExtractor", userReferenceUpdateXpathExtractor);
        invoiceRefDoc = testHelper.fileToDom(UserReferenceTestHelper.INVOICEREF_UPDATE_XML);
        userRefDescrDoc = testHelper.fileToDom(UserReferenceTestHelper.USERREF_DESC_UPDATE_XML);
    }

    @Test
    public void testIsApplicableToProducerTrue() throws Exception {
        when(userReferenceUpdateXpathExtractor.getNewEinvoiceRef(any(Node.class))).thenReturn(XPathString.valueOf(EINVOICE_REF));
        when(userReferenceUpdateXpathExtractor.getOldNumberTypeId(any(Node.class))).thenReturn(XPathString.valueOf(NUMBER_TYPE));
        Boolean isApplicable = invoiceReferenceUpdateProducer.isApplicable(invoiceRefDoc);
        assertTrue(isApplicable);
    }

    @Test
    public void testIsApplicableToProducerFalse() throws Exception {
        when(userReferenceUpdateXpathExtractor.getNewEinvoiceRef(any(Node.class))).thenReturn(null);
        Boolean isApplicable = invoiceReferenceUpdateProducer.isApplicable(userRefDescrDoc);
        assertFalse(isApplicable);
    }

    @Test
    public void testCreateApplicableEvent() throws Exception {
        when(userReferenceUpdateXpathExtractor.getNewEinvoiceRef(any(Node.class))).thenReturn(XPathString.valueOf(EINVOICE_REF));
        when(userReferenceUpdateXpathExtractor.getOldSusbcrId(any(Node.class))).thenReturn(XPathLong.valueOf(SUBSCRIPTION_ID));
        InvoiceReferenceUpdateEvent invoiceReferenceUpdateEvent = (InvoiceReferenceUpdateEvent) (invoiceReferenceUpdateProducer.produceMessage(invoiceRefDoc)).get(0);
        assertEquals("Unextected Invoice Reference",EINVOICE_REF, invoiceReferenceUpdateEvent.getInvoiceRef());
        assertEquals("Unextected SubscriptionId",SUBSCRIPTION_ID, invoiceReferenceUpdateEvent.getDomainId());
    }
}
