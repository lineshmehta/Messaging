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
import com.telenor.cos.messaging.event.resource.ResourceStructureInheritUpdateEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Test case for {@link ResourceStructureInheritUpdateHandler}
 * @author Babaprakash D
 *
 */
public class ResourceStructureInheritUpdateHandlerTest extends ResourceCommonHandlerTest {

    private ResourceStructureInheritUpdateHandler resourceStructureInheritUpdateHandler;

    @Mock
    private ResourceCache resourceCache;

    private static final Integer RESOURCE_TYPE_ID = 456;
    private static final String RESOURCE_TYPE_ID_KEY = "456";
    private static final String INHERIT_CONTENT = "Y";
    private static final String INHERIT_STRUCTURE_OLD = "Y";
    private static final boolean INHERIT_STRUCTURE_NEW = false;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        resourceStructureInheritUpdateHandler = new ResourceStructureInheritUpdateHandler();
        ReflectionTestUtils.setField(resourceStructureInheritUpdateHandler, "resourceCache", resourceCache);
    }

    @Test
    public void testHandle() {
        CachableResource cachableResource = createCachableResource(RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY,INHERIT_CONTENT,INHERIT_STRUCTURE_OLD);
        when(resourceCache.get(any(Long.class))).thenReturn(cachableResource);
        Resource oldResource = createResource(RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY,INHERIT_CONTENT,INHERIT_STRUCTURE_OLD);
        List<String> csUserIdsList = new ArrayList<String>();
        ResourceStructureInheritUpdateEvent resourceStructureInheritUpdateEvent = new ResourceStructureInheritUpdateEvent(oldResource,csUserIdsList,INHERIT_STRUCTURE_NEW);
        resourceStructureInheritUpdateHandler.handle(resourceStructureInheritUpdateEvent);
        cachableResource.setResourceHasStructureInherit("N");
        verify(resourceCache).insert(RESOURCE_ID, cachableResource);
    }
}
