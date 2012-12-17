package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.userresource.UserResourceResourceIdUpdateEvent;
import com.telenor.cos.messaging.handlers.UserResourceResourceIdUpdateHandler;
import com.telenor.cos.messaging.jdbm.ResourceCache;
import com.telenor.cos.messaging.producers.xpath.UserResourceUpdateXpathExtractor;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link com.telenor.cos.messaging.producers.UserResourceNewProducer} and
 * {@link com.telenor.cos.messaging.producers.UserResourceDeleteProducer}
 *
 * @author Babaprakash D
 */
@Category(ServiceTest.class)
public class UserResourceIdUpdateProducerTest extends AbstractUserResourceProducerTest {

    private static final Long RESOURCE_ID_NEW = 457L;

    private UserResourceIdUpdateProducer userResourceResourceIdUpdateProducer;

    @Mock
    private UserResourceUpdateXpathExtractor userResourceUpdateXpathExtractor;

    @Mock
    private UserResourceResourceIdUpdateHandler userResourceResourceIdUpdateHandler;

    @Mock
    private ResourceCache resourceCache;
    private static final Long RESOURCE_ID_OLD = Long.valueOf(1);


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userResourceResourceIdUpdateProducer = new UserResourceIdUpdateProducer();
        ReflectionTestUtils.setField(userResourceResourceIdUpdateProducer, "userResourceUpdateXpathExtractor", userResourceUpdateXpathExtractor);
        ReflectionTestUtils.setField(userResourceResourceIdUpdateProducer, "resourceCache", resourceCache);
        ReflectionTestUtils.setField(userResourceResourceIdUpdateProducer, "userResourceResourceIdUpdateHandler", userResourceResourceIdUpdateHandler);

    }

    @Test
    public void testIsApplicable() throws Exception {
        when(userResourceUpdateXpathExtractor.getResourceId(any(Node.class))).thenReturn(XPathLong.valueOf("999"));
        Boolean isApplicable = userResourceResourceIdUpdateProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(userResourceUpdateXpathExtractor.getCsUserIdOld(any(Node.class))).thenReturn(XPathString.valueOf(CS_USER_ID));
        when(userResourceUpdateXpathExtractor.getResourceId(any(Node.class))).thenReturn(XPathLong.valueOf(RESOURCE_ID_NEW));
        when(userResourceUpdateXpathExtractor.getResourceIdOld(any(Node.class))).thenReturn(XPathLong.valueOf(RESOURCE_ID_OLD));
        when(resourceCache.get(RESOURCE_ID_NEW)).thenReturn(createCachableResource(RESOURCE_ID_NEW));
        when(resourceCache.get(RESOURCE_ID_OLD)).thenReturn(createCachableResource(RESOURCE_ID_OLD));
        UserResourceResourceIdUpdateEvent event = (UserResourceResourceIdUpdateEvent) (userResourceResourceIdUpdateProducer.produceMessage(any(Node.class))).get(0);
        assertEquals("Unexpected csuser Id", CS_USER_ID, event.getNewUserResource().getUserId());
        assertEquals("Unexpected csuser Id", CS_USER_ID, event.getOldUserResource().getUserId());
        assertEquals("Unexpected Old ResourceId", RESOURCE_ID_OLD, event.getOldUserResource().getResource().getResourceId());
        assertEquals("Unexpected Resource TypeId", RESOURCE_TYPE_ID, event.getNewUserResource().getResource().getResourceTypeId());
        assertEquals("Unexpected Resource TypeId Key", RESOURCE_TYPE_ID_KEY, event.getNewUserResource().getResource().getResourceTypeIdKey());
        assertEquals("Unexpected Inherit Content", false, event.getNewUserResource().getResource().getResourceHasContentInherit());
        assertEquals("Unexpected Inherit Structure", true, event.getNewUserResource().getResource().getResourceHasStructureInherit());
    }
}