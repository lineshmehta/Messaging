package com.telenor.cos.messaging.producers.xpath;


import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * @author t798435
 */
@Component
public class MasterStructureUpdateXpathExtractor extends XPathExtractor {

    private static final String MAST_ID_MEMBER_OLD_EXPR = "//update[@schema='MAST_STRUCTURE']//oldValues//cell[@name='MAST_ID_MEMBER']";
    private static final String MAST_ID_OWNER_NEW_EXPR = "//update[@schema='MAST_STRUCTURE']//values//cell[@name='MAST_ID_OWNER']";
    private static final String MAST_ID_OWNER_OLD_EXPR = "//update[@schema='MAST_STRUCTURE']//oldValues//cell[@name='MAST_ID_OWNER']";
    private static final String INFO_IS_DELETED_NEW_EXPR = "//update[@schema='MAST_STRUCTURE']//values//cell[@name='INFO_IS_DELETED']";
    private static final String INFO_IS_DELETED_OLD_EXPR = "//update[@schema='MAST_STRUCTURE']//oldValues//cell[@name='INFO_IS_DELETED']";

    /**
     * Gets the old Master Id Member.
     * @param message message.
     * @return OldMastIdMember which maps to MasterCustomerId.
     */
    public XPathLong getOldMastIdMember(Node message) {
        return getLong(MAST_ID_MEMBER_OLD_EXPR, message);
    }
    
    /**
     * Gets the new Master Id Owner
     * @param message message.
     * @return NewMastIdOwner
     */
    public XPathLong getNewMastIdOwner(Node message) {
        return getLong(MAST_ID_OWNER_NEW_EXPR, message);
    }

    /**
     * Gets the old Master Id Owner.
     * @param message message.
     * @return OldMastIdOwner
     */
    public XPathLong getOldMastIdOwner(Node message) {
        return getLong(MAST_ID_OWNER_OLD_EXPR, message);
    }

    /**
     * Gets the new InfoIsDeleted.
     * @param message message.
     * @return InfoIsDeleted.
     */
    public XPathString getNewInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_NEW_EXPR, message);
    }

    /**
     * Gets the old InfoIsDeleted.
     * @param message message.
     * @return InfoIsDeleted.
     */
    public XPathString getOldInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_OLD_EXPR, message);
    }
}
