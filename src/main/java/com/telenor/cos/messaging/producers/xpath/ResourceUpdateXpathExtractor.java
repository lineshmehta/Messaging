package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * XPath Extractor for ResourceUpdate Events.
 * @author Babaprakash D
 *
 */
@Component
public class ResourceUpdateXpathExtractor extends XPathExtractor {
    
    private static final String RESOURCE_ID_EXPR = "//update[@schema='RESOURCE']//oldValues//cell[@name='RESOURCE_ID']";
    private static final String RESOURCE_TYPE_ID_OLD_EXPR = "//update[@schema='RESOURCE']//oldValues//cell[@name='RESOURCE_TYPE_ID']";
    private static final String RESOURCE_TYPE_ID_NEW_EXPR = "//update[@schema='RESOURCE']//values//cell[@name='RESOURCE_TYPE_ID']";
    private static final String RESOURCE_TYPE_ID_KEY_OLD_EXPR = "//update[@schema='RESOURCE']//oldValues//cell[@name='RESOURCE_TYPE_ID_KEY']";
    private static final String RESOURCE_TYPE_ID_KEY_NEW_EXPR = "//update[@schema='RESOURCE']//values//cell[@name='RESOURCE_TYPE_ID_KEY']";
    private static final String RESOURCE_HAS_CONTENT_INHERIT_OLD_EXPR = "//update[@schema='RESOURCE']//oldValues//cell[@name='RESOURCE_HAS_CONTENT_INHERIT']";
    private static final String RESOURCE_HAS_CONTENT_INHERIT_NEW_EXPR = "//update[@schema='RESOURCE']//values//cell[@name='RESOURCE_HAS_CONTENT_INHERIT']";
    private static final String RESOURCE_HAS_STRUCTURE_INHERIT_OLD_EXPR = "//update[@schema='RESOURCE']//oldValues//cell[@name='RESOURCE_HAS_STRUCTURE_INHERIT']";
    private static final String RESOURCE_HAS_STRUCTURE_INHERIT_NEW_EXPR = "//update[@schema='RESOURCE']//values//cell[@name='RESOURCE_HAS_STRUCTURE_INHERIT']";
    private static final String OLD_INFO_IS_DELETED_EXPR = "//update[@schema='RESOURCE']//oldValues//cell[@name='INFO_IS_DELETED']";
    private static final String NEW_INFO_IS_DELETED_EXPR = "//update[@schema='RESOURCE']//values//cell[@name='INFO_IS_DELETED']";

    /**
     * Gets ResourceId from message.
     * @param message from repServer.
     * @return resourceId.
     */
    public XPathLong getResourceId(Node message) {
        return getLong(RESOURCE_ID_EXPR, message);
    }

    /**
     * Gets ResourceTypeId from message.
     * @param message from repServer.
     * @return ResourceTypeId.
     */
    public XPathInteger getOldResourceTypeId(Node message) {
        return getInteger(RESOURCE_TYPE_ID_OLD_EXPR, message);
    }

    /**
     * Gets ResourceTypeId from message.
     * @param message from repServer.
     * @return ResourceTypeId.
     */
    public XPathInteger getNewResourceTypeId(Node message) {
        return getInteger(RESOURCE_TYPE_ID_NEW_EXPR, message);
    }

    /**
     * Gets ResourceTypeIdKey from message.
     * @param message from repServer.
     * @return ResourceTypeIdKey.
     */
    public XPathString getOldResourceTypeIdKey(Node message) {
        return getString(RESOURCE_TYPE_ID_KEY_OLD_EXPR, message);
    }

    /**
     * Gets ResourceTypeIdKey from message.
     * @param message from repServer.
     * @return ResourceTypeIdKey.
     */
    public XPathString getNewResourceTypeIdKey(Node message) {
        return getString(RESOURCE_TYPE_ID_KEY_NEW_EXPR, message);
    }

    /**
     * Gets ResourceHasContentInherit from message.
     * @param message from repServer.
     * @return ResourceHasContentInherit.
     */
    public XPathString getOldResourceHasContentInherit(Node message) {
        return getString(RESOURCE_HAS_CONTENT_INHERIT_OLD_EXPR, message);
    }

    /**
     * Gets ResourceHasContentInherit from message.
     * @param message from repServer.
     * @return ResourceHasContentInherit.
     */
    public XPathString getNewResourceHasContentInherit(Node message) {
        return getString(RESOURCE_HAS_CONTENT_INHERIT_NEW_EXPR, message);
    }

    /**
     * Gets ResourceHasStructureInherit from message.
     * @param message from repServer.
     * @return ResourceHasStructureInherit.
     */
    public XPathString getOldResourceHasStructureInherit(Node message) {
        return getString(RESOURCE_HAS_STRUCTURE_INHERIT_OLD_EXPR, message);
    }

    /**
     * Gets ResourceHasStructureInherit from message.
     * @param message from repServer.
     * @return ResourceHasStructureInherit.
     */
    public XPathString getNewResourceHasStructureInherit(Node message) {
        return getString(RESOURCE_HAS_STRUCTURE_INHERIT_NEW_EXPR, message);
    }

    /**
     * Gets InfoIsDeleted from message.
     * @param message from repServer.
     * @return InfoIsDeleted.
     */
    public XPathString getNewInfoIsDeleted(Node message) {
        return getString(NEW_INFO_IS_DELETED_EXPR, message);
    }

    /**
     * Gets InfoIsDeleted from message.
     * @param message from repServer.
     * @return InfoIsDeleted.
     */
    public XPathString getOldInfoIsDeleted(Node message) {
        return getString(OLD_INFO_IS_DELETED_EXPR, message);
    }
}
