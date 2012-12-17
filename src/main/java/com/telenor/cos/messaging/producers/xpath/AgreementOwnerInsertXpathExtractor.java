package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;

@Component
public class AgreementOwnerInsertXpathExtractor extends XPathExtractor {

    private static final String AGREEMENT_OWNER_ID_EXPR = "//insert[@schema='AGREEMENT_OWNER']//values//cell[@name='AGREEMENT_OWNER_ID']";
    private static final String MASTER_ID_EXPR = "//insert[@schema='AGREEMENT_OWNER']//values//cell[@name='MASTER_ID']";
    private static final String AGREEMENT_ID_EXPR = "//insert[@schema='AGREEMENT_OWNER']//values//cell[@name='AGREEMENT_ID']";

    /**
     * Gets agreementOwnerId from the message
     * 
     * @param message
     *            the message
     * @return XPathLong agreementOwnerId
     */
    public XPathLong getAgreementOwnerId(Node message) {
        return getLong(AGREEMENT_OWNER_ID_EXPR, message);
    }

    /**
     * Gets the masterId from the message
     * 
     * @param message
     *            the message
     * @return XPathLong masterId
     */
    public XPathLong getMasterId(Node message) {
        return getLong(MASTER_ID_EXPR, message);
    }

    /**
     * Gets the agreementId from the message
     * 
     * @param message
     *            the message
     * @return XPathLong the agreementId
     */
    public XPathLong getAgreementId(Node message) {
        return getLong(AGREEMENT_ID_EXPR, message);
    }

}
