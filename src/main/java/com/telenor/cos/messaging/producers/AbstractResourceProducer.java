package com.telenor.cos.messaging.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.jdbm.KurtIdCache;
import com.telenor.cos.messaging.jdbm.MasterCustomerCache;
import com.telenor.cos.messaging.producers.xpath.ResourceUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;

@Component
public abstract class AbstractResourceProducer extends AbstractProducer {

    private static final Integer RESOURCE_TYPE_CUSOMER_UNIT_NUMBER = Integer.valueOf(6);
    private static final Integer RESOURCE_TYPE_KURT_ID = Integer.valueOf(7);
    private static final Integer RESOURCE_TYPE_MASTER_CUSTOMER = Integer.valueOf(4);

    @Autowired
    private ResourceUpdateXpathExtractor resourceUpdateXpathExtractor;

    @Autowired
    private MasterCustomerCache masterCustomerCache;

    @Autowired
    private KurtIdCache kurtIdCache;

    /**
     * Creates Resource with Old Values.
     * @param message from repServer.
     * @return Resource.
     */
    protected Resource createResourceWithOldValues(Node message) {
        Long resourceId = XPathLong.getValue(resourceUpdateXpathExtractor.getResourceId(message));
        boolean oldResourceHasContentInherit = "Y".equals(resourceUpdateXpathExtractor.getOldResourceHasContentInherit(message).getValue());
        boolean oldResourceHasStructureInherit = "Y".equals(resourceUpdateXpathExtractor.getOldResourceHasStructureInherit(message).getValue());
        Integer oldResourceTypeId = resourceUpdateXpathExtractor.getOldResourceTypeId(message).getValue();
        String oldResourceTypeIdKey = resourceUpdateXpathExtractor.getOldResourceTypeIdKey(message).getValue();
        Resource resourceWithOldValues = new Resource(resourceId);
        resourceWithOldValues.setResourceHasContentInherit(oldResourceHasContentInherit);
        resourceWithOldValues.setResourceHasStructureInherit(oldResourceHasStructureInherit);
        resourceWithOldValues.setResourceTypeId(getResourceTypeId(oldResourceTypeId));
        resourceWithOldValues.setResourceTypeIdKey(getResourceTypeIdKeyFromMessageOrCache(oldResourceTypeId,oldResourceTypeIdKey));
        return resourceWithOldValues;
    }

    public ResourceUpdateXpathExtractor getResourceUpdateXpathExtractor() {
        return resourceUpdateXpathExtractor;
    }

    /**
     * Gets the resourceTypeId.
     * @param resourceTypeId resourceTypeId.
     * @return resourceTypeId 4 if ResourceTypeId is 6 or 7.
     */
    protected Integer getResourceTypeId(Integer resourceTypeId) {
        if(RESOURCE_TYPE_CUSOMER_UNIT_NUMBER.equals(resourceTypeId) || RESOURCE_TYPE_KURT_ID.equals(resourceTypeId)) {
            return RESOURCE_TYPE_MASTER_CUSTOMER;
        } else {
            return resourceTypeId;
        }
    }

    /**
     * Gets ResourceTypeIdKey.
     * @param resourceTypeId resourceTypeId
     * @param resourceTypeIdKey resourceTypeIdKey to search in Cache.
     * @return resourceTypeIdKey.
     */
    protected String getResourceTypeIdKeyFromMessageOrCache(Integer resourceTypeId, String resourceTypeIdKey) {
        if (RESOURCE_TYPE_CUSOMER_UNIT_NUMBER.equals(resourceTypeId)) {
            return getMasterIdFromMasterCustomerCache(resourceTypeIdKey);
        } else if (RESOURCE_TYPE_KURT_ID.equals(resourceTypeId)) {
            return getMasterIdFromKurtIdCache(resourceTypeIdKey);
        } else {
            return resourceTypeIdKey;
        }
    }

    private String getMasterIdFromMasterCustomerCache(String resourceTypeIdKey) {
        Long masterId = masterCustomerCache.get(Long.valueOf(resourceTypeIdKey));
        throwExceptionIfMasterIdIsNull(resourceTypeIdKey,"MasterCustomerCache", masterId);
        return masterId.toString();
    }

    private String getMasterIdFromKurtIdCache(String resourceTypeIdKey) {
        Long masterId = kurtIdCache.get(Long.valueOf(resourceTypeIdKey));
        throwExceptionIfMasterIdIsNull(resourceTypeIdKey,"KurtIdCache",masterId);
        return masterId.toString();
    }

    private void throwExceptionIfMasterIdIsNull(String resourceTypeIdKey,String nameOftheCache, Long masterId) {
        if (masterId == null) {
            throw new CosMessagingException("No MasterId was found in the " + nameOftheCache + " for the ResourceTypeIdKey [" + resourceTypeIdKey + "]", null);
        }
    }
}