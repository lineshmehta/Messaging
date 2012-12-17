package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.MasterStructure;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.MasterStructureUpdateXpathExtractor;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author t798435
 * 
 */
@Category(ServiceTest.class)
public class MasterStructureUpdateProducerTest {

    private MasterStructureUpdateProducer masterStructureUpdateProducer;

    @Mock
    private MasterStructureUpdateXpathExtractor masterStructureUpdateXpathExtractor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        masterStructureUpdateProducer = new MasterStructureUpdateProducer();
        ReflectionTestUtils.setField(masterStructureUpdateProducer, "masterStructureUpdateXpathExtractor",
                masterStructureUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(masterStructureUpdateXpathExtractor.getNewMastIdOwner(any(Node.class))).thenReturn(XPathLong.valueOf(33L));
        Boolean isApplicable = masterStructureUpdateProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testIsNotApplicable() throws Exception {
        when(masterStructureUpdateXpathExtractor.getNewMastIdOwner(any(Node.class))).thenReturn(null);
        Boolean isApplicable = masterStructureUpdateProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void shouldReturnMasterStructureUpdateEvent() throws Exception{
        when(masterStructureUpdateXpathExtractor.getNewMastIdOwner(any(Node.class))).thenReturn(XPathLong.valueOf("888"));
        List<Event> eventsList = masterStructureUpdateProducer.produceMessage(any(Node.class));
        MasterStructureUpdateEvent event = (MasterStructureUpdateEvent) eventsList.get(0);
        MasterStructure masterStructure = event.getMasterStructure();
        assertNotNull(masterStructure);
        assertEquals(Long.valueOf(888L), masterStructure.getMasterIdOwner());
    }
}
