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
import com.telenor.cos.messaging.event.resource.ResourceContentInheritUpdateEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Test case for {@link ResourceContentInheritUpdateHandler}
 * @author Babaprakash D
 *
 */
public class ResourceContentInheritUpdateHandlerTest extends ResourceCommonHandlerTest {

    private ResourceContentInheritUpdateHandler resourceContentInheritUpdateHandler;

    @Mock
    private ResourceCache resourceCache;

    private static final Integer RESOURCE_TYPE_ID = 456;
    private static final String RESOURCE_TYPE_ID_KEY = "456";
    private static final String INHERIT_STRUCTURE = "Y";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        resourceContentInheritUpdateHandler = new ResourceContentInheritUpdateHandler();
        ReflectionTestUtils.setField(resourceContentInheritUpdateHandler, "resourceCache", resourceCache);
    }

    @Test
    public void testHandle() {
        CachableResource cachableResource = createCachableResource(RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY,"N",INHERIT_STRUCTURE);
        when(resourceCache.get(any(Long.class))).thenReturn(cachableResource);
        Resource oldResource = createResource(RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY,"N",INHERIT_STRUCTURE);
        List<String> csUserIdsList = new ArrayList<String>();
        ResourceContentInheritUpdateEvent resourceContentInheritUpdateEvent = new ResourceContentInheritUpdateEvent(oldResource,csUserIdsList,true);
        resourceContentInheritUpdateHandler.handle(resourceContentInheritUpdateEvent);
        cachableResource.setResourceHasContentInherit("Y");
        verify(resourceCache).insert(RESOURCE_ID, cachableResource);
    }
}
