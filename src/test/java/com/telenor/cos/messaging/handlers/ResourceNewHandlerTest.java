package com.telenor.cos.messaging.handlers;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceNewEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Test case for {@link ResourceNewHandler}
 * @author Babaprakash D
 *
 */
public class ResourceNewHandlerTest extends ResourceCommonHandlerTest {

    private ResourceNewHandler resourceNewHandler;

    @Mock
    private ResourceCache resourceCache;

    private static final Integer RESOURCE_TYPE_ID = 456;
    private static final String RESOURCE_TYPE_ID_KEY = "456";
    private static final String INHERIT_CONTENT = "N";
    private static final String INHERIT_STRUCTURE = "Y";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        resourceNewHandler = new ResourceNewHandler();
        ReflectionTestUtils.setField(resourceNewHandler, "resourceCache", resourceCache); 
    }

    @Test
    public void testHandle() throws Exception {
        Resource resource = createResource(RESOURCE_TYPE_ID, RESOURCE_TYPE_ID_KEY, INHERIT_CONTENT, INHERIT_STRUCTURE);
        ResourceNewEvent resourceNewEvent = new ResourceNewEvent(resource);
        resourceNewHandler.handle(resourceNewEvent);
        CachableResource cachableResource = createCachableResource(RESOURCE_TYPE_ID, RESOURCE_TYPE_ID_KEY, INHERIT_CONTENT, INHERIT_STRUCTURE);
        verify(resourceCache).insert(RESOURCE_ID,cachableResource);
    }
}
