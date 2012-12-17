package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.UserResource;
import com.telenor.cos.messaging.event.userresource.UserResourceDeleteEvent;
import com.telenor.cos.messaging.handlers.UserResourceDeleteHandler;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;
import com.telenor.cos.messaging.producers.xpath.UserResourceDeleteXpathExtractor;
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
 * Test case for {@link UserResourceNewProducer} and
 * {@link UserResourceDeleteProducer}
 * 
 * @author Babaprakash D
 */
@Category(ServiceTest.class)
public class UserResourceDeleteProducerTest {

    public static final String CS_USER_ID = "cosmaster";
    public static final Long RESOURCE_ID_NEW = 2L;
    public static final Integer RESOURCE_TYPE_ID = 456;
    public static final String RESOURCE_TYPE_ID_KEY = "Superaccount";
    public static final String INHERIT_CONTENT = "N";
    public static final String INHERIT_STRUCTURE = "Y";

    @Mock
    private UserResourceDeleteProducer userResourceDeleteProducer;

    @Mock
    private UserResourceDeleteXpathExtractor userResourceDeleteXpathExtractor;

    @Mock
    private ResourceCache resourceCache;

    @Mock
    private UserResourceDeleteHandler userResourceDeleteHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userResourceDeleteProducer = new UserResourceDeleteProducer();
        ReflectionTestUtils.setField(userResourceDeleteProducer, "userResourceDeleteXpathExtractor", userResourceDeleteXpathExtractor);
        ReflectionTestUtils.setField(userResourceDeleteProducer, "resourceCache", resourceCache);
        ReflectionTestUtils.setField(userResourceDeleteProducer, "userResourceDeleteHandler", userResourceDeleteHandler);
    }

    @Test
    public void isApplicableIsTrueForDelete() throws Exception {
        when(userResourceDeleteXpathExtractor.getCsUserId(any(Node.class))).thenReturn(XPathString.valueOf("33"));
        Boolean isApplicable = userResourceDeleteProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testProduceMessageDelete() throws Exception {
        when(userResourceDeleteXpathExtractor.getCsUserId(any(Node.class))).thenReturn(XPathString.valueOf(CS_USER_ID));
        when(userResourceDeleteXpathExtractor.getResourceId(any(Node.class))).thenReturn(XPathLong.valueOf(RESOURCE_ID_NEW));
        when(resourceCache.get(RESOURCE_ID_NEW)).thenReturn(createCachableResource(RESOURCE_ID_NEW));
        List<Event> eventList = userResourceDeleteProducer.produceMessage(any(Node.class));
        UserResourceDeleteEvent event = (UserResourceDeleteEvent)eventList.get(0);
        assertUserResource(event.getUserResource());
    }

    private CachableResource createCachableResource(Long resourceId) {
        CachableResource cachableResource = new CachableResource(resourceId);
        cachableResource.setResourceHasContentInherit(INHERIT_CONTENT);
        cachableResource.setResourceHasStructureInherit(INHERIT_STRUCTURE);
        cachableResource.setResourceTypeId(RESOURCE_TYPE_ID);
        cachableResource.setResourceTypeIdKey(RESOURCE_TYPE_ID_KEY);
        return cachableResource;
    }

    private void assertUserResource(UserResource userResource) {
        Resource resource = userResource.getResource();
        assertEquals("Unexpected csuser Id", CS_USER_ID, userResource.getUserId());
        assertEquals("Unexpected Resource TypeId", RESOURCE_TYPE_ID, resource.getResourceTypeId());
        assertEquals("Unexpected Resource TypeId Key", RESOURCE_TYPE_ID_KEY, resource.getResourceTypeIdKey());
        assertFalse(resource.getResourceHasContentInherit());
        assertTrue(resource.getResourceHasStructureInherit());
    }

}