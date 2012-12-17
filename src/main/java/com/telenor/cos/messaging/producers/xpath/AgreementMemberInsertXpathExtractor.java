package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;

@Component
public class AgreementMemberInsertXpathExtractor extends XPathExtractor{

    private static final String AGREEMENT_MEMBER_ID_EXPR = "//insert[@schema='AGREEMENT_MEMBER_NEW']//values//cell[@name='AGREEMENT_MEMBER_ID']";
    private static final String AGREEMENT_ID_EXPR = "//insert[@schema='AGREEMENT_MEMBER_NEW']//values//cell[@name='AGREEMENT_ID']";
    private static final String CUSTOMER_UNIT_NUMBER_EXPR = "//insert[@schema='AGREEMENT_MEMBER_NEW']//values//cell[@name='CUST_UNIT_NUMBER']";
    private static final String MASTER_ID_EXPR = "//insert[@schema='AGREEMENT_MEMBER_NEW']//values//cell[@name='MASTER_ID']";


    /**
     * Return the agreementMemberId.
     *
     * @param message the message
     * @return agreementMemberId
     */
    public XPathLong getAgreementMemberId(Node message) {
        return getLong(AGREEMENT_MEMBER_ID_EXPR, message);
    }

    /**
     * Return the agreementId.
     *
     * @param message the message
     * @return agreementId
     */
    public XPathLong getAgreementId(Node message) {
        return getLong(AGREEMENT_ID_EXPR, message);
    }


    /**
     * Return the customerUnitNumber.
     *
     * @param message the message
     * @return the customerUnitNumber
     */
    public XPathLong getCustomerUnitNumber(Node message) {
        return getLong(CUSTOMER_UNIT_NUMBER_EXPR, message);
    }


    /**
     * Return the masterId.
     *
     * @param message the message
     * @return masterId
     */
    public XPathLong getMasterId(Node message) {
        return getLong(MASTER_ID_EXPR, message);
    }

}
