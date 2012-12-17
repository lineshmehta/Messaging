package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.usermapping.TnuIdUserMappingNewEvent;
import com.telenor.cos.messaging.producers.xpath.TnuIdUserMappingInsertXPathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * 
 * Test case for {@link TnuIdUserMappingNewProducer} 
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class TnuIdUserMappingNewProducerTest {

    private static final XPathString TNU_USER_ID = XPathString.valueOf("_1000030");
    private static final XPathInteger APP_ID = XPathInteger.valueOf(62);
    private static final  XPathString CS_USER_ID = XPathString.valueOf("tn_9234");

    private TnuIdUserMappingNewProducer userMappingNewProducer;

    @Mock
    private TnuIdUserMappingInsertXPathExtractor tnuIdUserMappingInsertXPathExtractor;
    

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        userMappingNewProducer = new TnuIdUserMappingNewProducer();
        ReflectionTestUtils.setField(userMappingNewProducer, "tnuIdUserMappingInsertXPathExtractor", tnuIdUserMappingInsertXPathExtractor);
    }
    
    @Test
    public void testIsApplicable() throws Exception {
        when(tnuIdUserMappingInsertXPathExtractor.getTelenorUserId(any(Node.class))).thenReturn(XPathString.valueOf("1001"));
        Boolean isApplicable = userMappingNewProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable); 
    }
    
    @Test
    public void testIdNotApplicable() throws Exception {
        when(tnuIdUserMappingInsertXPathExtractor.getTelenorUserId(any(Node.class))).thenReturn(null);
        Boolean isApplicable = userMappingNewProducer.isApplicable(any(Node.class));
        assertTrue(!isApplicable);
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(tnuIdUserMappingInsertXPathExtractor.getApplicationId(any(Node.class))).thenReturn(APP_ID);
        when(tnuIdUserMappingInsertXPathExtractor.getTelenorUserId(any(Node.class))).thenReturn(TNU_USER_ID);
        when(tnuIdUserMappingInsertXPathExtractor.getCosSecurityUserId(any(Node.class))).thenReturn(CS_USER_ID);
        List<Event> eventList = userMappingNewProducer.produceMessage(any(Node.class));
        TnuIdUserMappingNewEvent event = (TnuIdUserMappingNewEvent)eventList.get(0);
        assertEquals("Unexpected Application Id", APP_ID.getValue(), event.getUserMapping().getApplicationId());
        assertEquals("Unexpected telenor user Id", TNU_USER_ID.getValue(), event.getUserMapping().getTelenorUserId());
        assertEquals("Unexpected Application Id", CS_USER_ID.getValue(), event.getUserMapping().getCosSecurityUserId());
    }
    
}
