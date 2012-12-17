package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class AgreementUpdateXpathExtractor extends XPathExtractor {

    private static final String AGREEMENT_ID_EXPR = "//update[@schema='AGREEMENT_NEW']//oldValues//cell[@name='AGREEMENT_ID']";
    private static final String INFO_IS_DELETED_EXPR = "//update[@schema='AGREEMENT_NEW']//values//cell[@name='INFO_IS_DELETED']";
    
    /**
     * Gets agreementId from message.
     * 
     * @param message message
     * @return the agreement id
     */
    public XPathLong getAgreementId(Node message) {
        return getLong(AGREEMENT_ID_EXPR, message);
    }

    /**
     * Gets agreementId from message.
     * 
     * @param message message
     * @return the agreement id
     */
    public XPathString getInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_EXPR, message);
    }
    
}
