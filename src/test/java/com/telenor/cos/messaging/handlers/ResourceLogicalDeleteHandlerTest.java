package com.telenor.cos.messaging.handlers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.telenor.cos.messaging.event.resource.ResourceLogicalDeleteEvent;
import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

/**
 * Test case for {@link ResourceLogicalDeleteHandler}
 * @author Babaprakash D
 *
 */
public class ResourceLogicalDeleteHandlerTest extends ResourceCommonHandlerTest {

    private ResourceLogicalDeleteHandler resourceLogicalDeleteHandler;

    @Mock
    private ResourceCache resourceCache;

    private static final Integer RESOURCE_TYPE_ID = 456;
    private static final String RESOURCE_TYPE_ID_KEY = "Superaccount";
    private static final String INHERIT_CONTENT_OLD = "N";
    private static final String INHERIT_STRUCTURE = "Y";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        resourceLogicalDeleteHandler = new ResourceLogicalDeleteHandler();
        ReflectionTestUtils.setField(resourceLogicalDeleteHandler, "resourceCache", resourceCache); 
    }

    @Test
    public void testHandle() throws Exception {
        CachableResource cachableResource = createCachableResource(RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY,INHERIT_CONTENT_OLD,INHERIT_STRUCTURE);
        when(resourceCache.get(any(Long.class))).thenReturn(cachableResource);
        ResourceLogicalDeleteEvent resourceLogicalDeleteEvent = new ResourceLogicalDeleteEvent(RESOURCE_ID);
        resourceLogicalDeleteHandler.handle(resourceLogicalDeleteEvent);
        verify(resourceCache).remove(RESOURCE_ID);
    }
}
