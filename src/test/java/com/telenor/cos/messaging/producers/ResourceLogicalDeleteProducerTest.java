/**
 * 
 */
package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.resource.ResourceLogicalDeleteEvent;
import com.telenor.cos.messaging.handlers.ResourceLogicalDeleteHandler;
import com.telenor.cos.messaging.producers.xpath.ResourceUpdateXpathExtractor;
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
 * Test case for {@link com.telenor.cos.messaging.producers.ResourceLogicalDeleteProducer}
 *
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class ResourceLogicalDeleteProducerTest {

    @Mock
    private ResourceUpdateXpathExtractor resourceUpdateXpathExtractor;

    @Mock
    private ResourceLogicalDeleteHandler resourceLogicalDeleteHandler;

    private ResourceLogicalDeleteProducer resourceLogicalDeleteProducer;

    private static final XPathString INFO_IS_DELETED = XPathString.valueOf("Y");


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        resourceLogicalDeleteProducer = new ResourceLogicalDeleteProducer();
        ReflectionTestUtils.setField(resourceLogicalDeleteProducer, "resourceUpdateXpathExtractor",
                resourceUpdateXpathExtractor);
        ReflectionTestUtils.setField(resourceLogicalDeleteProducer, "resourceLogicalDeleteHandler", resourceLogicalDeleteHandler);

    }

    @Test
    public void isApplicable() throws Exception {
        when(resourceUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(INFO_IS_DELETED);
        boolean isApplicable = resourceLogicalDeleteProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }
    
    @Test
    public void isNotApplicable() throws Exception {
        when(resourceUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(null);
        boolean isApplicable = resourceLogicalDeleteProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(resourceUpdateXpathExtractor.getResourceId(any(Node.class))).thenReturn(XPathLong.valueOf(1L));
        Event event = (ResourceLogicalDeleteEvent) (resourceLogicalDeleteProducer.produceMessage(any(Node.class))).get(0);
        Long resourceId = event.getDomainId();
        assertNotNull(resourceId);
        assertEquals(Long.valueOf(1), resourceId);
    }
}
