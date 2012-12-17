package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.userresource.UserResourceCsUserIdUpdateEvent;
import com.telenor.cos.messaging.handlers.UserResourceCsUserIdUpdateHandler;
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
 * Test case for {@link UserResourceNewProducer} and
 * {@link UserResourceDeleteProducer}
 *
 * @author Babaprakash D
 */
@Category(ServiceTest.class)
public class UserResourceCsUserIdUpdateProducerTest extends AbstractUserResourceProducerTest{

    @Mock
    private UserResourceUpdateXpathExtractor userResourceUpdateXpathExtractor;

    @Mock
    private UserResourceCsUserIdUpdateHandler userResourceCsUserIdUpdateHandler;

    @Mock
    private ResourceCache resourceCache;
    private UserResourceCsUserIdUpdateProducer userResourceCsUserIdUpdateProducer;
    private static final Long RESOURCE_ID_OLD = 1L;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userResourceCsUserIdUpdateProducer = new UserResourceCsUserIdUpdateProducer();
        ReflectionTestUtils.setField(userResourceCsUserIdUpdateProducer, "userResourceUpdateXpathExtractor", userResourceUpdateXpathExtractor);
        ReflectionTestUtils.setField(userResourceCsUserIdUpdateProducer, "resourceCache", resourceCache);
        ReflectionTestUtils.setField(userResourceCsUserIdUpdateProducer, "userResourceCsUserIdUpdateHandler", userResourceCsUserIdUpdateHandler);
    }

    @Test
    public void isApplicableIsTrueForUpdate() throws Exception {
        when(userResourceUpdateXpathExtractor.getCsUserId(any(Node.class))).thenReturn(
                XPathString.valueOf("csUserId"));
        Boolean isApplicable = userResourceCsUserIdUpdateProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testCsUserIdUpdateProducer() throws Exception {
        when(userResourceUpdateXpathExtractor.getCsUserIdOld(any(Node.class))).thenReturn(XPathString.valueOf(CS_USER_ID));
        when(userResourceUpdateXpathExtractor.getCsUserId(any(Node.class))).thenReturn(XPathString.valueOf(CSUSERID_NEW));
        when(userResourceUpdateXpathExtractor.getResourceIdOld(any(Node.class))).thenReturn(XPathLong.valueOf(RESOURCE_ID_OLD));
        when(resourceCache.get(RESOURCE_ID_OLD)).thenReturn(createCachableResource(RESOURCE_ID_OLD));
        UserResourceCsUserIdUpdateEvent event = (UserResourceCsUserIdUpdateEvent) (userResourceCsUserIdUpdateProducer.produceMessage(any(Node.class))).get(0);
        assertEquals("Unexpected New csuser Id", CSUSERID_NEW, event.getUserResource().getUserId());
        assertEquals("Unexpected Old CsUserId", CS_USER_ID, event.getOldCsUserId());
        assertEquals("Unexpected Old ResourceId", RESOURCE_ID_OLD, event.getUserResource().getResource().getResourceId());
        assertEquals("Unexpected Resource TypeId", RESOURCE_TYPE_ID, event.getUserResource().getResource().getResourceTypeId());
        assertEquals("Unexpected Resource TypeId Key", RESOURCE_TYPE_ID_KEY, event.getUserResource().getResource().getResourceTypeIdKey());
        assertEquals("Unexpected Inherit Content", false, event.getUserResource().getResource().getResourceHasContentInherit());
        assertEquals("Unexpected Inherit Structure", true, event.getUserResource().getResource().getResourceHasStructureInherit());
    }
}