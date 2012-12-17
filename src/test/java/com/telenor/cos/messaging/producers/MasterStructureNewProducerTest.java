package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureNewEvent;
import com.telenor.cos.messaging.producers.xpath.MasterStructureInsertXpathExtractor;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author t798435
 * 
 */
@Category(ServiceTest.class)
public class MasterStructureNewProducerTest {

    private MasterStructureNewProducer masterStructureNewProducer;

    private static final XPathLong MASTER_ID_MEMBER = XPathLong.valueOf(666);
    private static final XPathLong MASTER_ID_OWNER = XPathLong.valueOf(777);

    @Mock
    private MasterStructureInsertXpathExtractor masterStructureInsertXpathExtractor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        masterStructureNewProducer = new MasterStructureNewProducer();
        ReflectionTestUtils
                .setField(masterStructureNewProducer, "masterStructureInsertXpathExtractor", masterStructureInsertXpathExtractor);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(masterStructureInsertXpathExtractor.getMasterIdMember(any(Node.class))).thenReturn(XPathLong.valueOf(55L));
        Boolean isApplicable = masterStructureNewProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testIsNotApplicable() throws Exception {
        when(masterStructureInsertXpathExtractor.getMasterIdMember(any(Node.class))).thenReturn(null);
        Boolean isApplicable = masterStructureNewProducer.isApplicable(any(Node.class));
        assertTrue(!isApplicable);
    }

    @Test
    public void testNewMasterStructureEvent() throws Exception {
        when(masterStructureInsertXpathExtractor.getMasterIdMember(any(Node.class))).thenReturn(MASTER_ID_MEMBER);
        when(masterStructureInsertXpathExtractor.getMasterIdOwner(any(Node.class))).thenReturn(MASTER_ID_OWNER);

        List<Event> eventsList = masterStructureNewProducer.produceMessage(any(Node.class));
        MasterStructureNewEvent masterStructureNewEvent=(MasterStructureNewEvent)eventsList.get(0);

        assertEquals("Unexpected MasterId (Master Id Member)", MASTER_ID_MEMBER.getValue(), masterStructureNewEvent.getDomainId());
        assertEquals("Unexpected Master Id Owner", MASTER_ID_OWNER.getValue(), masterStructureNewEvent.getMasterStructure().getMasterIdOwner());
        assertEquals("Master Id Member and Master Id should be the same, BUT ARE NOT!", MASTER_ID_MEMBER.getValue(), masterStructureNewEvent.getDomainId(), masterStructureNewEvent.getMasterStructure().getMasterId());
    }

}
