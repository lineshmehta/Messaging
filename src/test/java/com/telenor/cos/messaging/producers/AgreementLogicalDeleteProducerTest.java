package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Agreement;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.agreement.AgreementLogicalDeletedEvent;
import com.telenor.cos.messaging.producers.xpath.AgreementUpdateXpathExtractor;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link AgreementLogicalDeleteProducer}
 */
@Category(ServiceTest.class)
public class AgreementLogicalDeleteProducerTest {

    public static final String AGREEMENT_ID = "4274399";
    private Producer agreementLogicalDeleteProducer;

    @Mock
    private AgreementUpdateXpathExtractor agreementUpdateXpathExtractor;

    @Before
    public void setUp() throws Exception {
        agreementLogicalDeleteProducer = new AgreementLogicalDeleteProducer();
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(agreementLogicalDeleteProducer, "agreementUpdateXpathExtractor", agreementUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(agreementUpdateXpathExtractor.getInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        assertTrue(agreementLogicalDeleteProducer.isApplicable(any(Node.class)));
    }
    
    @Test
    public void testIsNotApplicable() throws Exception {
        when(agreementUpdateXpathExtractor.getInfoIsDeleted(any(Node.class))).thenReturn(null);
        assertFalse(agreementLogicalDeleteProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testLogicalDeleteMessage() throws Exception {
        when(agreementUpdateXpathExtractor.getAgreementId(any(Node.class))).thenReturn(XPathLong.valueOf(AGREEMENT_ID));
        AgreementLogicalDeletedEvent event = (AgreementLogicalDeletedEvent) agreementLogicalDeleteProducer.produceMessage(
                any(Node.class)).get(0);
        assertNotNull(event);
        assertEquals(ACTION.LOGICAL_DELETE, event.getAction());
        assertEquals(TYPE.AGREEMENT, event.getType());

        Agreement agreement = event.getAgreement();
        assertNotNull(agreement);
        assertEquals(Long.valueOf(AGREEMENT_ID), agreement.getAgreementId());
    }
}
