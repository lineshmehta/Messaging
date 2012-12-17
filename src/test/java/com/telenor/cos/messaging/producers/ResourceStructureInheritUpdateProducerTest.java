package com.telenor.cos.messaging.producers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.event.resource.ResourceStructureInheritUpdateEvent;
import com.telenor.cos.messaging.handlers.ResourceStructureInheritUpdateHandler;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

/**
 * Test case for {@link com.telenor.cos.messaging.producers.ResourceStructureInheritUpdateProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class ResourceStructureInheritUpdateProducerTest extends ResourceCommonProducerTest {

    private ResourceStructureInheritUpdateProducer resourceStructureInheritUpdateProducer;

    @Mock
    private ResourceStructureInheritUpdateHandler resourceStructureInheritUpdateHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        resourceStructureInheritUpdateProducer = new ResourceStructureInheritUpdateProducer();
        setMockCacheToProducer(resourceStructureInheritUpdateProducer);
        ReflectionTestUtils.setField(resourceStructureInheritUpdateProducer, "resourceStructureInheritUpdateHandler", resourceStructureInheritUpdateHandler);
    }

    @Test
    public void testIsApplicableToProducer() throws Exception {
        when(getResourceUpdateXpathExtractor().getNewResourceHasStructureInherit(any(Node.class))).thenReturn(XPathString.valueOf("N"));
        boolean isApplicableToProducer = resourceStructureInheritUpdateProducer.isApplicable(any(Node.class));
        assertTrue(isApplicableToProducer);
    }

    @Test
    public void testProduceMessage() throws Exception {
        setResourceMock(MASTER_CUSTOMER_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        ResourceStructureInheritUpdateEvent event = callCreateApplicableEventAndReturnEvent();
        assertResourceStructureInheritUpdateEvent(event,MASTER_CUSTOMER_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
    }

    @Test
    public void testProduceMessageWithCun() throws Exception {
        setResourceMock(CUN_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        when(getMasterCustomerCache().get(Long.valueOf(RESOURCE_TYPE_ID_KEY))).thenReturn(MASTER_ID);
        ResourceStructureInheritUpdateEvent event = callCreateApplicableEventAndReturnEvent();
        assertResourceStructureInheritUpdateEvent(event,MASTER_CUSTOMER_RESOURCE_TYPE_ID,MASTER_ID.toString());
    }

    @Test
    public void testProduceMessageWithKurtId() throws Exception {
        setResourceMock(KURT_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        when(getKurtIdCache().get(Long.valueOf(RESOURCE_TYPE_ID_KEY))).thenReturn(KURT_ID);
        ResourceStructureInheritUpdateEvent event = callCreateApplicableEventAndReturnEvent();
        assertResourceStructureInheritUpdateEvent(event,MASTER_CUSTOMER_RESOURCE_TYPE_ID,KURT_ID.toString());
    }

    @Test(expected = CosMessagingException.class)
    public void shouldThrowExceptionWhenMasterIdIsNotFoundInCacheForCun() throws Exception {
        setResourceMock(CUN_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        when(getMasterCustomerCache().get(Long.valueOf(RESOURCE_TYPE_ID_KEY))).thenReturn(null);
        callCreateApplicableEventAndReturnEvent();
    }

    @Test(expected = CosMessagingException.class)
    public void shouldThrowExceptionWhenMasterIdIsNotFoundInCacheForKurtId() {
        setResourceMock(KURT_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        when(getKurtIdCache().get(Long.valueOf(RESOURCE_TYPE_ID_KEY))).thenReturn(null);
        callCreateApplicableEventAndReturnEvent();
    }

    private ResourceStructureInheritUpdateEvent callCreateApplicableEventAndReturnEvent() {
        when(getResourceUpdateXpathExtractor().getNewResourceHasStructureInherit(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        return (ResourceStructureInheritUpdateEvent) (resourceStructureInheritUpdateProducer.produceMessage(any(Node.class))).get(0);
    }

    private void assertResourceStructureInheritUpdateEvent(ResourceStructureInheritUpdateEvent event,Integer resourceTypeId, String resourceTypeIdKey) {
        assertEquals(true, event.getResourceHasStructureInherit());
        assertDomainIdAndCsUserId(event.getDomainId(),event.getCsUserIdsList().get(0));
        assertEquals("Unexpected ResourceType Id",resourceTypeIdKey,event.getResource().getResourceTypeIdKey());
        assertEquals("Unexpected ResourceType",resourceTypeId,event.getResource().getResourceTypeId());
    }
}
