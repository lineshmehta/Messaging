package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.usermapping.TnuidUserMappingDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.TnuIdUserMappingDeleteXPathExtractor;
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
public class TnuIdUserMappingDeleteProducerTest {

    private TnuIdUserMappingDeleteProducer tnuIdUserMappingDeleteProducer;
    private static final XPathString TNU_USER_ID = XPathString.valueOf("_1000030");

    @Mock
    private TnuIdUserMappingDeleteXPathExtractor tnuIdUserMappingDeleteXPathExtractor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tnuIdUserMappingDeleteProducer = new TnuIdUserMappingDeleteProducer();
        ReflectionTestUtils.setField(tnuIdUserMappingDeleteProducer, "tnuIdUserMappingDeleteXPathExtractor",
                tnuIdUserMappingDeleteXPathExtractor);
    }

    @Test
    public void shouldReturnIsApplicable() throws Exception {
        when(tnuIdUserMappingDeleteXPathExtractor.getTelenorUserIdOld(any(Node.class))).thenReturn(
                XPathString.valueOf("telenor"));
        boolean isApplicable = tnuIdUserMappingDeleteProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void shouldReturnIsNotApplicable() throws Exception {
        when(tnuIdUserMappingDeleteXPathExtractor.getTelenorUserIdOld(any(Node.class))).thenReturn(null);
        boolean isApplicable = tnuIdUserMappingDeleteProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void shouldReturnDeleteEvent() throws Exception{
        when(tnuIdUserMappingDeleteXPathExtractor.getTelenorUserIdOld(any(Node.class))).thenReturn(TNU_USER_ID);
        List<Event> eventList = tnuIdUserMappingDeleteProducer.produceMessage(any(Node.class));
        TnuidUserMappingDeleteEvent event = (TnuidUserMappingDeleteEvent) eventList.get(0);
        assertEquals(TNU_USER_ID.getValue(), event.getTelenorUserId());
        assertEquals(ACTION.DELETE, event.getAction());
        assertEquals(TYPE.USERMAPPING, event.getType());
    }
}
