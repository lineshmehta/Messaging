package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;

/**
 * @author t798435
 */
@Component
public class MasterStructureInsertXpathExtractor extends XPathExtractor {

    private static final String MASTER_ID_MEMBER_EXPR = "//insert[@schema='MAST_STRUCTURE']//values//cell[@name='MAST_ID_MEMBER']";
    private static final String MASTER_ID_OWNER_EXPR = "//insert[@schema='MAST_STRUCTURE']//values//cell[@name='MAST_ID_OWNER']";

    /**
     * Gets the Master Id Member.
     * @param message message.
     * @return Master Id Member
     */
    public XPathLong getMasterIdMember(Node message) {
        return getLong(MASTER_ID_MEMBER_EXPR, message);
    }

    /**
     * Gets the Master Id Owner.
     * @param message message.
     * @return Master Id Owner
     */
    public XPathLong getMasterIdOwner(Node message) {
        return getLong(MASTER_ID_OWNER_EXPR, message);
    }
}
