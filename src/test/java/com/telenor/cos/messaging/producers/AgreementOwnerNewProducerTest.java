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

import com.telenor.cos.messaging.event.AgreementOwner;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.agreementowner.AgreementOwnerNewEvent;
import com.telenor.cos.messaging.producers.xpath.AgreementOwnerInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
public class AgreementOwnerNewProducerTest {

    private AgreementOwnerNewProducer agreementOwnerNewProducer;

    @Mock
    private AgreementOwnerInsertXpathExtractor agreementOwnerInsertXpathExtractor;
    

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        agreementOwnerNewProducer = new AgreementOwnerNewProducer();
        ReflectionTestUtils.setField(agreementOwnerNewProducer, "agreementOwnerInsertXpathExtractor", agreementOwnerInsertXpathExtractor);
    }

    @Test
    public void testIsApplicable(){
        when(agreementOwnerInsertXpathExtractor.getAgreementOwnerId(any(Node.class))).thenReturn(XPathLong.valueOf(33L));
        boolean isApplicable = agreementOwnerNewProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }
    
    @Test
    public void testIsNotApplicable(){
        when(agreementOwnerInsertXpathExtractor.getAgreementOwnerId(any(Node.class))).thenReturn(null);
        boolean isApplicable = agreementOwnerNewProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void testProduceEvent(){
        when(agreementOwnerInsertXpathExtractor.getAgreementId(any(Node.class))).thenReturn(XPathLong.valueOf(531314L));
        when(agreementOwnerInsertXpathExtractor.getMasterId(any(Node.class))).thenReturn(XPathLong.valueOf(100318909L));
        List<Event> eventList = agreementOwnerNewProducer.produceMessage(any(Node.class));
        AgreementOwnerNewEvent event = (AgreementOwnerNewEvent) eventList.get(0);
        assertEquals(TYPE.AGREEMENT_OWNER, event.getType());
        assertEquals(ACTION.CREATED, event.getAction());
        AgreementOwner agreementOwner = event.getAgreementOwner();
        assertEquals(Long.valueOf("100318909"), agreementOwner.getMasterId());
        assertEquals(Long.valueOf("531314"), agreementOwner.getAgreementId());

    }
}
