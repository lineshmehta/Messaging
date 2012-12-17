package com.telenor.cos.messaging.handlers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceTypeIdKeyUpdateEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Test case for {@link ResourceTypeIdKeyUpdateHandler}
 * @author Babaprakash D
 *
 */
public class ResourceTypeIdKeyUpdateHandlerTest extends ResourceCommonHandlerTest {

    private ResourceTypeIdKeyUpdateHandler resourceTypeIdKeyUpdateHandler;

    @Mock
    private ResourceCache resourceCache;

    private static final Integer RESOURCE_TYPE_ID = 456;
    private static final String RESOURCE_TYPE_ID_KEY_OLD = "456";
    private static final String RESOURCE_TYPE_ID_KEY_NEW = "457";
    private static final String INHERIT_CONTENT = "Y";
    private static final String INHERIT_STRUCTURE = "N";

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        resourceTypeIdKeyUpdateHandler = new ResourceTypeIdKeyUpdateHandler();
        ReflectionTestUtils.setField(resourceTypeIdKeyUpdateHandler, "resourceCache", resourceCache);
    }

    @Test
    public void testHandle() {
        CachableResource cachableResource = createCachableResource(RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY_OLD,INHERIT_CONTENT,INHERIT_STRUCTURE);
        when(resourceCache.get(any(Long.class))).thenReturn(cachableResource);
        Resource oldResource = createResource(RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY_OLD,INHERIT_CONTENT,INHERIT_STRUCTURE);
        List<String> csUserIdsList = new ArrayList<String>();
        ResourceTypeIdKeyUpdateEvent resourceTypeIdKeyUpdateEvent = new ResourceTypeIdKeyUpdateEvent(oldResource,csUserIdsList,RESOURCE_TYPE_ID_KEY_NEW);
        resourceTypeIdKeyUpdateHandler.handle(resourceTypeIdKeyUpdateEvent);
        cachableResource.setResourceTypeIdKey(RESOURCE_TYPE_ID_KEY_NEW);
        verify(resourceCache).insert(RESOURCE_ID, cachableResource);
    }
}
