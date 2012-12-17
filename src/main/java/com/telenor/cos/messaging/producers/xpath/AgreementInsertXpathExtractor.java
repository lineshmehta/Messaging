package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;

@Component
public class AgreementInsertXpathExtractor extends XPathExtractor {

    private static final String AGREEMENT_ID_EXPR = "//insert[@schema='AGREEMENT_NEW']//values//cell[@name='AGREEMENT_ID']";
    private static final String CUST_UNIT_NUMBER_EXPR = "//insert[@schema='AGREEMENT_NEW']//values//cell[@name='CUST_UNIT_NUMBER']";

    /**
     * Gets agreementId from message.
     * 
     * @param message
     *            message
     * @return the agreement id
     */
    public XPathLong getAgreementId(Node message) {
        return getLong(AGREEMENT_ID_EXPR, message);
    }

    /**
     * Gets the custUnitNumber from message
     * 
     * @param message
     *            the message
     * @return the custUnitNumber
     */
    public XPathLong getCustUnitNumber(Node message) {
        return getLong(CUST_UNIT_NUMBER_EXPR, message);
    }

}
