package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.jdbm.KurtIdCache;
import com.telenor.cos.messaging.jdbm.MasterCustomerCache;
import com.telenor.cos.messaging.jdbm.UserResourceCache;
import com.telenor.cos.messaging.producers.xpath.ResourceUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ResourceCommonProducerTest {

    public static final Long RESOURCE_ID = Long.valueOf(1);
    public static final String CS_USER_ID = "cosmaster";
    public static final Integer CUN_RESOURCE_TYPE_ID = Integer.valueOf(6);
    public static final Integer KURT_RESOURCE_TYPE_ID = Integer.valueOf(7);
    public static final Integer MASTER_CUSTOMER_RESOURCE_TYPE_ID = Integer.valueOf(4);
    public static final String RESOURCE_TYPE_ID_KEY = "456";
    public static final Long MASTER_ID = Long.valueOf(234);
    public static final Long KURT_ID = Long.valueOf(678); 

    @Mock
    private ResourceUpdateXpathExtractor resourceUpdateXpathExtractor;

    @Mock
    private UserResourceCache userResourceCache;

    @Mock
    private MasterCustomerCache masterCustomerCache;

    @Mock 
    private KurtIdCache kurtIdCache;

    public void setResourceMock(Integer resourceTypeId,String resourceTypeIdKey) {
        when(resourceUpdateXpathExtractor.getResourceId(any(Node.class))).thenReturn(XPathLong.valueOf(RESOURCE_ID));
        when(resourceUpdateXpathExtractor.getOldResourceTypeId(any(Node.class))).thenReturn(XPathInteger.valueOf(resourceTypeId));
        when(resourceUpdateXpathExtractor.getOldResourceTypeIdKey(any(Node.class))).thenReturn(XPathString.valueOf(resourceTypeIdKey));
        when(resourceUpdateXpathExtractor.getOldResourceHasContentInherit(any(Node.class))).thenReturn(XPathString.valueOf("N"));
        when(resourceUpdateXpathExtractor.getOldResourceHasStructureInherit(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        List<String> csUserIdsList = new ArrayList<String>();
        csUserIdsList.add(CS_USER_ID);
        when(userResourceCache.get(RESOURCE_ID)).thenReturn(csUserIdsList);
    }
    
    public void setMockCacheToProducer(AbstractResourceProducer resourceProducer) {
        ReflectionTestUtils.setField(resourceProducer, "resourceUpdateXpathExtractor", getResourceUpdateXpathExtractor());
        ReflectionTestUtils.setField(resourceProducer, "userResourceCache", getUserResourceCache());
        ReflectionTestUtils.setField(resourceProducer, "masterCustomerCache", getMasterCustomerCache());
        ReflectionTestUtils.setField(resourceProducer, "kurtIdCache", getKurtIdCache());
    }

    /**
     * @return the resourceUpdateXpathExtractor
     */
    public ResourceUpdateXpathExtractor getResourceUpdateXpathExtractor() {
        return resourceUpdateXpathExtractor;
    }

    public UserResourceCache getUserResourceCache() {
        return userResourceCache;
    }

    public MasterCustomerCache getMasterCustomerCache() {
        return masterCustomerCache;
    }

    public KurtIdCache getKurtIdCache() {
        return kurtIdCache;
    }

    public void assertDomainIdAndCsUserId(Long domainId,String csUserId) {
        assertNotNull(domainId);
        assertEquals(RESOURCE_ID, domainId);
        assertEquals(CS_USER_ID, csUserId);
    }
}
