package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * @author Per Jørgen Walstrøm
 */
@Component
public class UserResourceUpdateXpathExtractor extends XPathExtractor {

    private static final String CS_USER_ID_EXPR = "//update[@schema='USER_RESOURCE']//values//cell[@name='CS_USER_ID']";
    private static final String CS_USER_ID_OLD_EXPR = "//update[@schema='USER_RESOURCE']//oldValues//cell[@name='CS_USER_ID']";
    private static final String RESOURCE_ID_EXPR = "//update[@schema='USER_RESOURCE']//values//cell[@name='RESOURCE_ID']";
    private static final String RESOURCE_ID_OLD_EXPR = "//update[@schema='USER_RESOURCE']//oldValues//cell[@name='RESOURCE_ID']";

    /**
     * @param message from repServer.
     * @return csUserId.
     */
    public XPathString getCsUserId(Node message) {
        return getString(CS_USER_ID_EXPR,message);
    }

    /**
     * @param message from repServer.
     * @return csUserId.
     */
    public XPathString getCsUserIdOld(Node message) {
        return getString(CS_USER_ID_OLD_EXPR,message);
    }

    /**
     * @param message from repServer.
     * @return ResourceId.
     */
    public XPathLong getResourceId(Node message) {
        return getLong(RESOURCE_ID_EXPR,message);
    }

    /**
     * @param message from repServer.
     * @return oldResourceId
     */
    public XPathLong getResourceIdOld(Node message) {
        return getLong(RESOURCE_ID_OLD_EXPR, message);
    }
}
