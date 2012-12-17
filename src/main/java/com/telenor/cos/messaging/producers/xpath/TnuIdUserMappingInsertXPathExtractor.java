package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * XPath Extractor Implementation for TnuIdUserMapping.
 * @author Babaprakash D
 *
 */
@Component
public class TnuIdUserMappingInsertXPathExtractor extends XPathExtractor {

    private static final String TELENOR_USER_ID_EXPR = "//insert[@schema='tnuid_user_mapping']//values//cell[@name='tnu_id']";
    private static final String CS_USER_ID_EXPR = "//insert[@schema='tnuid_user_mapping']//values//cell[@name='cs_user_id']";
    private static final String APPLICATION_ID_EXPR = "//insert[@schema='tnuid_user_mapping']//values//cell[@name='application_id']";

    /**
     * Gets TelenorUserId.
     * @param message message.
     * @return TelenorUserId.
     */
    public XPathString getTelenorUserId(Node message) {
        return getString(TELENOR_USER_ID_EXPR, message);
    }

    /**
     * Gets ApplicationId.
     * @param message message.
     * @return ApplicationId.
     */
    public XPathInteger getApplicationId(Node message) {
        return getInteger(APPLICATION_ID_EXPR, message);
    }

    /**
     * Gets CosSecurityUserId.
     * @param message message.
     * @return CosSecurityUserId.
     */
    public XPathString getCosSecurityUserId(Node message) {
        return getString(CS_USER_ID_EXPR, message);
    }
}
