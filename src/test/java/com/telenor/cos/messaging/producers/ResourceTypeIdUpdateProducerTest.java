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
import com.telenor.cos.messaging.event.resource.ResourceTypeIdUpdateEvent;
import com.telenor.cos.messaging.handlers.ResourceTypeIdUpdateHandler;
import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import com.telenor.cos.test.category.ServiceTest;
/**
 * Test case for {@link com.telenor.cos.messaging.producers.ResourceTypeIdUpdateProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class ResourceTypeIdUpdateProducerTest extends ResourceCommonProducerTest {

    private ResourceTypeIdUpdateProducer resourceTypeIdUpdateProducer;

    @Mock
    private ResourceTypeIdUpdateHandler resourceTypeIdUpdateHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        resourceTypeIdUpdateProducer = new ResourceTypeIdUpdateProducer();
        setMockCacheToProducer(resourceTypeIdUpdateProducer);
        ReflectionTestUtils.setField(resourceTypeIdUpdateProducer, "resourceTypeIdUpdateHandler", resourceTypeIdUpdateHandler);

    }

    @Test
    public void testIsApplicableToProducer() throws Exception {
        when(getResourceUpdateXpathExtractor().getNewResourceTypeId(any(Node.class))).thenReturn(XPathInteger.valueOf(KURT_RESOURCE_TYPE_ID));
        boolean isApplicableToProducer = resourceTypeIdUpdateProducer.isApplicable(any(Node.class));
        assertTrue(isApplicableToProducer);
    }

    @Test
    public void testProduceMessage() throws Exception {
        setResourceMock(MASTER_CUSTOMER_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        ResourceTypeIdUpdateEvent event = callCreateApplicableEventAndReturnEvent();
        assertResourceTypeIdUpdateEvent(event,MASTER_CUSTOMER_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
    }

    @Test
    public void testProduceMessageWithCun() throws Exception {
        setResourceMock(CUN_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        when(getMasterCustomerCache().get(Long.valueOf(RESOURCE_TYPE_ID_KEY))).thenReturn(MASTER_ID);
        ResourceTypeIdUpdateEvent event = callCreateApplicableEventAndReturnEvent();
        assertResourceTypeIdUpdateEvent(event,MASTER_CUSTOMER_RESOURCE_TYPE_ID,MASTER_ID.toString());
    }

    @Test
    public void testProduceMessageWithKurtId() throws Exception {
        setResourceMock(KURT_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        when(getKurtIdCache().get(Long.valueOf(RESOURCE_TYPE_ID_KEY))).thenReturn(KURT_ID);
        ResourceTypeIdUpdateEvent event = callCreateApplicableEventAndReturnEvent();
        assertResourceTypeIdUpdateEvent(event,MASTER_CUSTOMER_RESOURCE_TYPE_ID,KURT_ID.toString());
    }

    @Test(expected = CosMessagingException.class)
    public void shouldThrowExceptionWhenMasterIdIsNotFoundInCacheForCun() throws Exception {
        setResourceMock(CUN_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        when(getMasterCustomerCache().get(Long.valueOf(RESOURCE_TYPE_ID_KEY))).thenReturn(null);
        callCreateApplicableEventAndReturnEvent();
    }

    @Test(expected = CosMessagingException.class)
    public void shouldThrowExceptionWhenMasterIdNotFoundInCacheForKurtId() {
        setResourceMock(KURT_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        when(getKurtIdCache().get(Long.valueOf(RESOURCE_TYPE_ID_KEY))).thenReturn(null);
        callCreateApplicableEventAndReturnEvent();
    }

    private ResourceTypeIdUpdateEvent callCreateApplicableEventAndReturnEvent() {
        when(getResourceUpdateXpathExtractor().getNewResourceTypeId(any(Node.class))).thenReturn(XPathInteger.valueOf(KURT_RESOURCE_TYPE_ID));
        ResourceTypeIdUpdateEvent event = (ResourceTypeIdUpdateEvent) (resourceTypeIdUpdateProducer.produceMessage(any(Node.class))).get(0);
        return event;
    }
    private void assertResourceTypeIdUpdateEvent(ResourceTypeIdUpdateEvent event,Integer resourceTypeId, String resourceTypeIdKey) {
        assertEquals(MASTER_CUSTOMER_RESOURCE_TYPE_ID, event.getResourceTypeId());
        assertDomainIdAndCsUserId(event.getDomainId(),event.getCsUserIdsList().get(0));
        assertEquals("Unexpected ResourceType Id",resourceTypeIdKey,event.getResource().getResourceTypeIdKey());
        assertEquals("Unexpected ResourceType",resourceTypeId,event.getResource().getResourceTypeId());
    }
}
