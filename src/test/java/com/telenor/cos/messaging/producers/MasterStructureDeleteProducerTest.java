package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.MasterStructureUpdateXpathExtractor;
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

/**
 * @author t798435
 * 
 */
@Category(ServiceTest.class)
public class MasterStructureDeleteProducerTest {

    private MasterStructureDeleteProducer masterStructureDeleteProducer;

    @Mock
    private MasterStructureUpdateXpathExtractor masterStructureUpdateXpathExtractor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        masterStructureDeleteProducer = new MasterStructureDeleteProducer();
        ReflectionTestUtils.setField(masterStructureDeleteProducer, "masterStructureUpdateXpathExtractor", masterStructureUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(masterStructureUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        Boolean isApplicable = masterStructureDeleteProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testIsNotApplicable() throws Exception {
        when(masterStructureUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(null);
        Boolean isApplicable = masterStructureDeleteProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void shouldReturnMasterStructureDeleteEvent() throws Exception {
        when(masterStructureUpdateXpathExtractor.getOldMastIdMember(any(Node.class))).thenReturn(XPathLong.valueOf("31415926"));
        List<Event> eventsList = masterStructureDeleteProducer.produceMessage(any(Node.class));
        MasterStructureDeleteEvent event = (MasterStructureDeleteEvent) eventsList.get(0);

        assertEquals((Long)31415926L, event.getDomainId());
        assertEquals(Event.ACTION.DELETE, event.getAction());
    }

}
