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
import com.telenor.cos.messaging.event.resource.ResourceTypeIdKeyUpdateEvent;
import com.telenor.cos.messaging.handlers.ResourceTypeIdKeyUpdateHandler;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

/**
 * Test case for {@link com.telenor.cos.messaging.producers.ResourceTypeIdKeyUpdateProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class ResourceTypeIdKeyUpdateProducerTest extends ResourceCommonProducerTest {

    private ResourceTypeIdKeyUpdateProducer resourceTypeIdKeyUpdateProducer;

    @Mock
    private ResourceTypeIdKeyUpdateHandler resourceTypeIdKeyUpdateHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        resourceTypeIdKeyUpdateProducer = new ResourceTypeIdKeyUpdateProducer();
        setMockCacheToProducer(resourceTypeIdKeyUpdateProducer);
        ReflectionTestUtils.setField(resourceTypeIdKeyUpdateProducer, "resourceTypeIdKeyUpdateHandler", resourceTypeIdKeyUpdateHandler);
    }

    @Test
    public void testIsApplicableToProducer() throws Exception {
        when(getResourceUpdateXpathExtractor().getNewResourceTypeIdKey(any(Node.class))).thenReturn(XPathString.valueOf(RESOURCE_TYPE_ID_KEY));
        boolean isApplicableToProducer = resourceTypeIdKeyUpdateProducer.isApplicable(any(Node.class));
        assertTrue(isApplicableToProducer);
    }

    @Test
    public void testProduceMessage() throws Exception {
        setResourceMock(MASTER_CUSTOMER_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        ResourceTypeIdKeyUpdateEvent event = callCreateApplicableEventAndReturnEvent();
        assertResourceTypeIdKeyUpdateEvent(event,MASTER_CUSTOMER_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
    }

    @Test
    public void testProduceMessageWithCun() throws Exception {
        setResourceMock(CUN_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        when(getMasterCustomerCache().get(Long.valueOf(RESOURCE_TYPE_ID_KEY))).thenReturn(MASTER_ID);
        ResourceTypeIdKeyUpdateEvent event = callCreateApplicableEventAndReturnEvent();
        assertResourceTypeIdKeyUpdateEvent(event,MASTER_CUSTOMER_RESOURCE_TYPE_ID,MASTER_ID.toString());
    }

    @Test
    public void testProduceMessageWithKurtId() throws Exception {
        setResourceMock(KURT_RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        when(getKurtIdCache().get(Long.valueOf(RESOURCE_TYPE_ID_KEY))).thenReturn(KURT_ID);
        ResourceTypeIdKeyUpdateEvent event = callCreateApplicableEventAndReturnEvent();
        assertResourceTypeIdKeyUpdateEvent(event,MASTER_CUSTOMER_RESOURCE_TYPE_ID,KURT_ID.toString());
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

    private ResourceTypeIdKeyUpdateEvent callCreateApplicableEventAndReturnEvent() {
        when(getResourceUpdateXpathExtractor().getNewResourceTypeIdKey(any(Node.class))).thenReturn(XPathString.valueOf(RESOURCE_TYPE_ID_KEY));
        ResourceTypeIdKeyUpdateEvent event = (ResourceTypeIdKeyUpdateEvent) (resourceTypeIdKeyUpdateProducer.produceMessage(any(Node.class))).get(0);
        return event;
    }
    private void assertResourceTypeIdKeyUpdateEvent(ResourceTypeIdKeyUpdateEvent event,Integer resourceTypeId, String resourceTypeIdKey) {
        assertEquals(RESOURCE_TYPE_ID_KEY, event.getResourceTypeIdKey());
        assertDomainIdAndCsUserId(event.getDomainId(),event.getCsUserIdsList().get(0));
        assertEquals("Unexpected ResourceType Id",resourceTypeIdKey,event.getResource().getResourceTypeIdKey());
        assertEquals("Unexpected ResourceType",resourceTypeId,event.getResource().getResourceTypeId());
    }
}
