package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.agreement.AgreementNewEvent;
import com.telenor.cos.messaging.jdbm.MasterCustomerCache;
import com.telenor.cos.messaging.producers.xpath.AgreementInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link AgreementNewProducer}
 */
@Category(ServiceTest.class)
public class AgreementNewProducerTest {

    private AgreementNewProducer agreementNewProducer;

    @Mock
    private MasterCustomerCache mockMasterCustomerCache;

    @Mock
    private AgreementInsertXpathExtractor agreementInsertXpathExtractorMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        agreementNewProducer = new AgreementNewProducer();
        ReflectionTestUtils.setField(agreementNewProducer, "agreementInsertXpathExtractor", agreementInsertXpathExtractorMock);
        ReflectionTestUtils.setField(agreementNewProducer, "masterCustomerCache", mockMasterCustomerCache);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(agreementInsertXpathExtractorMock.getAgreementId(any(Node.class))).thenReturn(XPathLong.valueOf(33L));
        Boolean isApplicable = agreementNewProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }
    
    @Test
    public void testIsNotApplicable() throws Exception {
        when(agreementInsertXpathExtractorMock.getAgreementId(any(Node.class))).thenReturn(null);
        Boolean isApplicable = agreementNewProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void getNewAgreementEventWithMC() throws Exception{
        when(agreementInsertXpathExtractorMock.getAgreementId(any(Node.class))).thenReturn(XPathLong.valueOf(55));
        when(agreementInsertXpathExtractorMock.getCustUnitNumber(any(Node.class))).thenReturn(XPathLong.valueOf(2));
        when(mockMasterCustomerCache.get(any(Long.class))).thenReturn(22L);
        List<Event> eventList = agreementNewProducer.produceMessage(any(Node.class));
        AgreementNewEvent event = (AgreementNewEvent) eventList.get(0);
        assertEquals(Long.valueOf("55"), event.getDomainId());
        assertEquals(Long.valueOf("22"), event.getAgreement().getMasterCustomerId());
    }

    @Test
    public void getNewAgreementEvent() throws Exception{
        when(agreementInsertXpathExtractorMock.getAgreementId(any(Node.class))).thenReturn(XPathLong.valueOf(55));
        when(agreementInsertXpathExtractorMock.getCustUnitNumber(any(Node.class))).thenReturn(null);
        List<Event> eventList = agreementNewProducer.produceMessage(any(Node.class));
        AgreementNewEvent event = (AgreementNewEvent) eventList.get(0);
        assertEquals(Long.valueOf("55"), event.getDomainId());
        assertNull(event.getAgreement().getMasterCustomerId());
    }

}
