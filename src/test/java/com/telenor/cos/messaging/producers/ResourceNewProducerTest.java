package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceNewEvent;
import com.telenor.cos.messaging.handlers.ResourceNewHandler;
import com.telenor.cos.messaging.jdbm.KurtIdCache;
import com.telenor.cos.messaging.jdbm.MasterCustomerCache;
import com.telenor.cos.messaging.producers.xpath.ResourceInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link com.telenor.cos.messaging.producers.ResourceNewProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class ResourceNewProducerTest {

    private static final Long RESOURCE_ID = 1L;
    private static final Integer RESOURCE_TYPE_ID = 2;
    private static final Integer MC_RESOURCE_TYPE_ID = 4;
    private static final Integer CUSTOMER_UNIT_RESOURCE_TYPE_ID = 6;
    private static final Integer KURT_RESOURCE_TYPE_ID = 7;
    private static final String CUSTOMER_UNIT_NUMBER = "666";
    private static final String KURT_ID = "999";
    private static final String RESOURCE_TYPE_KEY = "777";
    private static final Long MASTER_ID = 42L;

    private ResourceNewProducer resourceNewProducer;

    @Mock
    private ResourceInsertXpathExtractor resourceInsertXpathExtractor;

    @Mock
    private MasterCustomerCache mockMasterCustomerCache;

    @Mock
    private KurtIdCache mockKurtIdCache;

    @Mock
    private ResourceNewHandler resourceNewHandler;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        resourceNewProducer = new ResourceNewProducer();
        ReflectionTestUtils.setField(resourceNewProducer, "resourceInsertXpathExtractor", resourceInsertXpathExtractor);
        ReflectionTestUtils.setField(resourceNewProducer, "masterCustomerCache", mockMasterCustomerCache);
        ReflectionTestUtils.setField(resourceNewProducer, "kurtIdCache", mockKurtIdCache);
        ReflectionTestUtils.setField(resourceNewProducer, "resourceNewHandler", resourceNewHandler);
    }

    @Test
    public void testProduceMessage() throws Exception {
        setUpXPathExtractorMock(RESOURCE_TYPE_ID, RESOURCE_TYPE_KEY);
        List<Event> resourceNewEventsList = resourceNewProducer.produceMessage(any(Node.class));
        assertEvent(resourceNewEventsList);
    }

    @Test
    public void testProduceMessageWithResourceTypeCustomerUnit() throws Exception {
        setUpXPathExtractorMock(CUSTOMER_UNIT_RESOURCE_TYPE_ID, CUSTOMER_UNIT_NUMBER);
        setUpMasterCustomerCacheMock(CUSTOMER_UNIT_NUMBER, MASTER_ID);
        List<Event> resourceNewEventsList = resourceNewProducer.produceMessage(any(Node.class));
        assertEvent(resourceNewEventsList, MC_RESOURCE_TYPE_ID, MASTER_ID.toString());
        verify(mockMasterCustomerCache).get(Long.valueOf(CUSTOMER_UNIT_NUMBER));
    }

    @Test
    public void testProduceMessageWithResourceTypeKurtId() throws Exception {
        setUpXPathExtractorMock(KURT_RESOURCE_TYPE_ID, KURT_ID);
        setUpKurtIdCacheMock(KURT_ID, MASTER_ID);
        List<Event> resourceNewEventsList = resourceNewProducer.produceMessage(any(Node.class));
        assertEvent(resourceNewEventsList, MC_RESOURCE_TYPE_ID, MASTER_ID.toString());
        verify(mockKurtIdCache).get(Long.valueOf(KURT_ID));
    }

    @Test(expected = CosMessagingException.class)
    public void shouldThrowExceptionWhenMasterIdIsNotAvailableForCunInCache() throws Exception {
        setUpXPathExtractorMock(CUSTOMER_UNIT_RESOURCE_TYPE_ID, CUSTOMER_UNIT_NUMBER);
        setUpMasterCustomerCacheMock(CUSTOMER_UNIT_NUMBER, null);
        List<Event> resourceNewEventsList = resourceNewProducer.produceMessage(any(Node.class));
        assertEvent(resourceNewEventsList, MC_RESOURCE_TYPE_ID, null);
    }

    @Test(expected = CosMessagingException.class)
    public void shouldThrowExceptionWhenMasterIdIsNotAvailableForKurtIdInCache() throws Exception {
        setUpXPathExtractorMock(KURT_RESOURCE_TYPE_ID, KURT_ID);
        setUpKurtIdCacheMock(KURT_ID, null);
        List<Event> resourceNewEventsList = resourceNewProducer.produceMessage(any(Node.class));
        assertEvent(resourceNewEventsList, MC_RESOURCE_TYPE_ID, null);
    }

    private void assertEvent(List<Event> resourceNewEventsList, Integer expectedResourceTypeId, String expectedResourceTypeKey) {
        ResourceNewEvent resourceNewEvent = (ResourceNewEvent) resourceNewEventsList.get(0);
        assertEquals("Unexpected domain id", Long.valueOf(1), resourceNewEvent.getDomainId());
        Resource resource = resourceNewEvent.getResource();
        assertEquals("Unexpected resource id", RESOURCE_ID, resource.getResourceId());
        assertEquals("Unexpected resource type id", expectedResourceTypeId, resource.getResourceTypeId());
        assertEquals("Unexpected resource type key", expectedResourceTypeKey, resource.getResourceTypeIdKey());
        assertFalse("Expected not to have content inherit", resource.getResourceHasContentInherit());
        assertTrue("Expected to have structure inherit", resource.getResourceHasStructureInherit());
    }

    private void setUpXPathExtractorMock(Integer returnedResourceTypeId, String returnedResourceTypeIdKey) {
        when(resourceInsertXpathExtractor.getResourceId(any(Node.class))).thenReturn(XPathLong.valueOf(RESOURCE_ID));
        when(resourceInsertXpathExtractor.getResourceTypeId(any(Node.class))).thenReturn(XPathInteger.valueOf(returnedResourceTypeId));
        when(resourceInsertXpathExtractor.getResourceTypeIdKey(any(Node.class))).thenReturn(XPathString.valueOf(returnedResourceTypeIdKey));
        when(resourceInsertXpathExtractor.getResourceHasContentInherit(any(Node.class))).thenReturn(XPathString.valueOf("N"));
        when(resourceInsertXpathExtractor.getResourceHasStructureInherit(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
    }

    private void setUpMasterCustomerCacheMock(String expectedCustomerUnitNo, Long returnedMasterId) {
        when(mockMasterCustomerCache.get(Long.valueOf(expectedCustomerUnitNo))).thenReturn(returnedMasterId);
    }

    private void setUpKurtIdCacheMock(String kurtId, Long returnedMasterId) {
        when(mockKurtIdCache.get(Long.valueOf(kurtId))).thenReturn(returnedMasterId);
    }

    private void assertEvent(List<Event> resourceNewEventsList) {
        assertEvent(resourceNewEventsList, RESOURCE_TYPE_ID, RESOURCE_TYPE_KEY);
    }
}
