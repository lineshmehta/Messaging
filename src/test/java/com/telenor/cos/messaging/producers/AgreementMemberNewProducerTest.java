package com.telenor.cos.messaging.producers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.CosMessagingInvalidDataException;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.agreementmember.AgreementMemberNewEvent;
import com.telenor.cos.messaging.producers.xpath.AgreementMemberInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
public class AgreementMemberNewProducerTest extends AgreementMemberCommonProducerTest{

    private AgreementMemberNewProducer agreementMemberNewProducer;

    @Mock
    private AgreementMemberInsertXpathExtractor mockAgreementMemberInsertXpathExtractor;
    
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        agreementMemberNewProducer = new AgreementMemberNewProducer();
        ReflectionTestUtils.setField(agreementMemberNewProducer, "agreementMemberInsertXpathExtractor", mockAgreementMemberInsertXpathExtractor);
        ReflectionTestUtils.setField(agreementMemberNewProducer, "masterCustomerCache", getMockMasterCustomerCache());
    }

    @Test
    public void testIsApplicabel() throws Exception {
        when(mockAgreementMemberInsertXpathExtractor.getAgreementMemberId(any(Node.class))).thenReturn(XPathLong.valueOf(44L));
        assertTrue("Expected to be applicable", agreementMemberNewProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testIsNotApplicabel() throws Exception {
        when(mockAgreementMemberInsertXpathExtractor.getAgreementId(any(Node.class))).thenReturn(null);
        assertFalse("Expected to be not applicable", agreementMemberNewProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testTranslate() {
        setUpXpathExtractorMocks(MASTER_ID, null);
        List<Event> agreementMembers = agreementMemberNewProducer.produceMessage(any(Node.class));
        assertAgreementMember(agreementMembers.get(0));
    }

    @Test
    public void testMasterIdFoundInCache() {
        setUpXpathExtractorMocks(null, null);
        setUpMasterCustomerCacheMocks(MASTER_ID);
        List<Event> agreementMembers = agreementMemberNewProducer.produceMessage(any(Node.class));
        assertAgreementMember(agreementMembers.get(0));
    }

    @Test (expected = CosMessagingException.class)
    public void testMasterIdNotFoundInCache(){
        setUpXpathExtractorMocks(null, CUSTOMER_UNIT_NUMBER);
        setUpMasterCustomerCacheMocks(null);
        agreementMemberNewProducer.produceMessage(any(Node.class));
    }

    @Test(expected = CosMessagingInvalidDataException.class)
    public void testBothMasterIdAndCustomerUnitNumberAreNull(){
        setUpXpathExtractorMocks(null, null);
        agreementMemberNewProducer.produceMessage(any(Node.class));
    }

    private void setUpXpathExtractorMocks(XPathLong returnedMasterId, XPathLong returnedCustomerUnitNumber) {
        when(mockAgreementMemberInsertXpathExtractor.getAgreementMemberId(any(Node.class))).thenReturn(AGREEMENT_MEMBER_ID);
        when(mockAgreementMemberInsertXpathExtractor.getAgreementId(any(Node.class))).thenReturn(AGREEMENT_ID);
        when(mockAgreementMemberInsertXpathExtractor.getMasterId(any(Node.class))).thenReturn(returnedMasterId);
        when(mockAgreementMemberInsertXpathExtractor.getCustomerUnitNumber(any(Node.class))).thenReturn(returnedCustomerUnitNumber);
    }
    private void setUpMasterCustomerCacheMocks(XPathLong returnedMasterId) {
        when(mockAgreementMemberInsertXpathExtractor.getCustomerUnitNumber(any(Node.class))).thenReturn(CUSTOMER_UNIT_NUMBER);
        when(getMockMasterCustomerCache().get(CUSTOMER_UNIT_NUMBER.getValue())).thenReturn(XPathLong.getValue(returnedMasterId));
    }
    private void assertAgreementMember(Event agreementMemberEvent) {
        AgreementMemberNewEvent agreementMemberNewEvent = (AgreementMemberNewEvent) agreementMemberEvent;
        assertEquals("Unexpected agreement member id", AGREEMENT_MEMBER_ID.getValue(), agreementMemberNewEvent.getDomainId());
        assertEquals("Unexpected agreement id", AGREEMENT_ID.getValue(), agreementMemberNewEvent.getAgreementMember().getAgreementId());
        assertEquals("Unexpected master id", MASTER_ID.getValue(), agreementMemberNewEvent.getAgreementMember().getMasterId());
    }


}
