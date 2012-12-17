package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class TnuIdUserMappingDeleteXPathExtractor extends XPathExtractor {

    private static final String TELENOR_USER_ID_EXPR = "//delete[@schema='tnuid_user_mapping']//oldValues//cell[@name='tnu_id']";
    private static final String CS_USER_ID_EXPR = "//delete[@schema='tnuid_user_mapping']//oldValues//cell[@name='cs_user_id']";
    private static final String APPLICATION_ID_EXPR = "//delete[@schema='tnuid_user_mapping']//oldValues//cell[@name='application_id']";

    /**
     * Gets TelenorUserId.
     * 
     * @param message
     *            message.
     * @return TelenorUserId.
     */
    public XPathString getTelenorUserIdOld(Node message) {
        return getString(TELENOR_USER_ID_EXPR, message);
    }

    /**
     * Gets ApplicationId.
     * 
     * @param message
     *            message.
     * @return ApplicationId.
     */
    public XPathInteger getApplicationIdOld(Node message) {
        return getInteger(APPLICATION_ID_EXPR, message);
    }

    /**
     * Gets CosSecurityUserId.
     * 
     * @param message
     *            message.
     * @return CosSecurityUserId.
     */
    public XPathString getCosSecurityUserId(Node message) {
        return getString(CS_USER_ID_EXPR, message);
    }
}
