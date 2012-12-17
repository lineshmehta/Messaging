package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.agreementmember.AgreementMemberLogicalDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.AgreementMemberUpdateXpathExtractor;
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
public class AgreementMemberLogicalDeleteProducerTest extends AgreementMemberCommonProducerTest{

    private AgreementMemberLogicalDeleteProducer agreementMemberLogicalDeleteProducer;

    @Mock
    private AgreementMemberUpdateXpathExtractor agreementMemberUpdateXPathExtractor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        agreementMemberLogicalDeleteProducer = new AgreementMemberLogicalDeleteProducer();
        ReflectionTestUtils.setField(agreementMemberLogicalDeleteProducer, "agreementMemberUpdateXPathExtractor", agreementMemberUpdateXPathExtractor);
        ReflectionTestUtils.setField(agreementMemberLogicalDeleteProducer, "masterCustomerCache", getMockMasterCustomerCache());
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(agreementMemberUpdateXPathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        boolean isApplicable = agreementMemberLogicalDeleteProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }
    
    @Test
    public void testIsNotApplicable() throws Exception {
        when(agreementMemberUpdateXPathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(null);
        boolean isApplicable = agreementMemberLogicalDeleteProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void testCreateEventWhenMasterIdNotNull() {
        when(agreementMemberUpdateXPathExtractor.getOldAgreementMemberMasterId(any(Node.class))).thenReturn(MASTER_ID);
        when(agreementMemberUpdateXPathExtractor.getOldAgreementMemberCustUnitNumber(any(Node.class))).thenReturn(CUSTOMER_UNIT_NUMBER);
        setUpXpathExtractorMocks();
        List<Event> eventList = agreementMemberLogicalDeleteProducer.produceMessage(any(Node.class));
        assertAgreementMember(eventList.get(0));
    }

    @Test
    public void createEventWhenMasterIdIsNull() {
        when(agreementMemberUpdateXPathExtractor.getOldAgreementMemberMasterId(any(Node.class))).thenReturn(null);
        setUpXpathExtractorMocks();
        setUpMasterCustomerCacheMocks(MASTER_ID);
        List<Event> eventList = agreementMemberLogicalDeleteProducer.produceMessage(any(Node.class));
        assertAgreementMember(eventList.get(0));
    }

    @Test(expected = CosMessagingException.class)
    public void createEventWhenBothMasterIdAndCustUnitNumberAreNull() {
        when(agreementMemberUpdateXPathExtractor.getOldAgreementMemberMasterId(any(Node.class))).thenReturn(null);
        when(agreementMemberUpdateXPathExtractor.getOldAgreementMemberCustUnitNumber(any(Node.class))).thenReturn(null);
        setUpXpathExtractorMocks();
        agreementMemberLogicalDeleteProducer.produceMessage(any(Node.class));
    }

    private void setUpXpathExtractorMocks() {
        when(agreementMemberUpdateXPathExtractor.getOldAgreementMemberId(any(Node.class))).thenReturn(AGREEMENT_MEMBER_ID);
        when(agreementMemberUpdateXPathExtractor.getOldAgreementId(any(Node.class))).thenReturn(AGREEMENT_ID);

    }
    private void setUpMasterCustomerCacheMocks(XPathLong returnedMasterId) {
        when(agreementMemberUpdateXPathExtractor.getOldAgreementMemberCustUnitNumber(any(Node.class))).thenReturn(CUSTOMER_UNIT_NUMBER);
        when(getMockMasterCustomerCache().get(CUSTOMER_UNIT_NUMBER.getValue())).thenReturn(XPathLong.getValue(returnedMasterId));
    }
    private void assertAgreementMember(Event agreementMemberEvent) {
        AgreementMemberLogicalDeleteEvent event = (AgreementMemberLogicalDeleteEvent)agreementMemberEvent;
        assertEquals("Unexpected agreement member id", AGREEMENT_MEMBER_ID.getValue(), event.getDomainId());
        assertEquals("Unexpected agreement id", AGREEMENT_ID.getValue(), event.getAgreementMember().getAgreementId());
        assertEquals("Unexpected master id", MASTER_ID.getValue(), event.getAgreementMember().getMasterId());
    }
}
