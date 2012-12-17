package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class AgreementMemberUpdateXpathExtractor extends XPathExtractor {

    private static final String AGREEMENT_MEMBER_ID_EXPR = "//update[@schema='AGREEMENT_MEMBER_NEW']//oldValues//cell[@name='AGREEMENT_MEMBER_ID']";
    private static final String AGREEMENT_ID_EXPR = "//update[@schema='AGREEMENT_MEMBER_NEW']//oldValues//cell[@name='AGREEMENT_ID']";
    private static final String INFO_IS_DELETED_EXPR = "//update[@schema='AGREEMENT_MEMBER_NEW']//values//cell[@name='INFO_IS_DELETED']";
    private static final String MASTER_ID_OLD_EXPR = "//update[@schema='AGREEMENT_MEMBER_NEW']//oldValues//cell[@name='MASTER_ID']";
    private static final String CUST_UNIT_NUMBER_OLD_EXPR = "//update[@schema='AGREEMENT_MEMBER_NEW']//oldValues//cell[@name='CUST_UNIT_NUMBER']";

    /**
     * Returns old AgreementMemberId
     * @param message the message
     * @return agreementMemberId
     */
    public XPathLong getOldAgreementMemberId(Node message) {
        return getLong(AGREEMENT_MEMBER_ID_EXPR, message);
    }

    /**
     * Returns old AgreementId
     * @param message the message
     * @return agreementId
     */
    public XPathLong getOldAgreementId(Node message) {
        return getLong(AGREEMENT_ID_EXPR, message);
    }
    
    /**
     * Returns old AgreementMember MasterId.
     * @param message the message
     * @return MasterId
     */
    public XPathLong getOldAgreementMemberMasterId(Node message) {
        return getLong(MASTER_ID_OLD_EXPR, message);
    }
    
    /**
     * Returns old AgreementMember CustUnitNumber
     * @param message the message
     * @return CustUnitNumber
     */
    public XPathLong getOldAgreementMemberCustUnitNumber(Node message) {
        return getLong(CUST_UNIT_NUMBER_OLD_EXPR, message);
    }

    /**
     * Returns new InfoIsDeleted
     * @param message the message
     * @return infoIsDeleted
     */
    public XPathString getNewInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_EXPR, message);
    }

}
