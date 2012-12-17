package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * Xpath Extractor for New Resource.
 * @author Babaprakash D
 *
 */
@Component
public class ResourceInsertXpathExtractor extends XPathExtractor {

    private static final String RESOURCE_ID_EXPR = "//insert[@schema='RESOURCE']//values//cell[@name='RESOURCE_ID']";
    private static final String RESOURCE_TYPE_ID_EXPR = "//insert[@schema='RESOURCE']//values//cell[@name='RESOURCE_TYPE_ID']";
    private static final String RESOURCE_TYPE_ID_KEY_EXPR = "//insert[@schema='RESOURCE']//values//cell[@name='RESOURCE_TYPE_ID_KEY']";
    private static final String RESOURCE_HAS_CONTENT_INHERIT_EXPR = "//insert[@schema='RESOURCE']//values//cell[@name='RESOURCE_HAS_CONTENT_INHERIT']";
    private static final String RESOURCE_HAS_STRUCTURE_INHERIT_EXPR = "//insert[@schema='RESOURCE']//values//cell[@name='RESOURCE_HAS_STRUCTURE_INHERIT']";

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
    public XPathInteger getResourceTypeId(Node message) {
        return getInteger(RESOURCE_TYPE_ID_EXPR, message);
    }

    /**
     * Gets ResourceTypeIdKey from message.
     * @param message from repServer.
     * @return ResourceTypeIdKey.
     */
    public XPathString getResourceTypeIdKey(Node message) {
        return getString(RESOURCE_TYPE_ID_KEY_EXPR, message);
    }

    /**
     * Gets ResourceHasContentInherit from message.
     * @param message from repServer.
     * @return ResourceHasContentInherit.
     */
    public XPathString getResourceHasContentInherit(Node message) {
        return getString(RESOURCE_HAS_CONTENT_INHERIT_EXPR, message);
    }

    /**
     * Gets ResourceHasStructureInherit from message.
     * @param message from repServer.
     * @return ResourceHasStructureInherit.
     */
    public XPathString getResourceHasStructureInherit(Node message) {
        return getString(RESOURCE_HAS_STRUCTURE_INHERIT_EXPR, message);
    }
}
