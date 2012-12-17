package com.telenor.cos.messaging.producers.xpath;


import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class MasterCustomerUpdateXpathExtractor extends XPathExtractor {

    private static final String MASTER_ID_NEW_EXPR = "//update[@schema='MASTER_CUSTOMER']//values//cell[@name='MASTER_ID']";
    private static final String INFO_IS_DELETED_NEW_EXPR = "//update[@schema='MASTER_CUSTOMER']//values//cell[@name='INFO_IS_DELETED']";
    private static final String MASTER_ID_OLD_EXPR = "//update[@schema='MASTER_CUSTOMER']//oldValues//cell[@name='MASTER_ID']";
    private static final String INFO_IS_DELETED_OLD_EXPR = "//update[@schema='MASTER_CUSTOMER']//oldValues//cell[@name='INFO_IS_DELETED']";
    private static final String FIRST_NAME_NEW_EXPR = "//update[@schema='MASTER_CUSTOMER']//values//cell[@name='CUST_FIRST_NAME']";
    private static final String MIDDLE_NAME_NEW_EXPR = "//update[@schema='MASTER_CUSTOMER']//values//cell[@name='CUST_MIDDLE_NAME']";
    private static final String LAST_NAME_NEW_EXPR = "//update[@schema='MASTER_CUSTOMER']//values//cell[@name='CUST_LAST_NAME']";
    private static final String FIRST_NAME_OLD_EXPR = "//update[@schema='MASTER_CUSTOMER']//oldValues//cell[@name='CUST_FIRST_NAME']";
    private static final String MIDDLE_NAME_OLD_EXPR = "//update[@schema='MASTER_CUSTOMER']//oldValues//cell[@name='CUST_MIDDLE_NAME']";
    private static final String LAST_NAME_OLD_EXPR = "//update[@schema='MASTER_CUSTOMER']//oldValues//cell[@name='CUST_LAST_NAME']";
    
    /**
     * Gets MasterCustomerId.
     * @param message message.
     * @return MasterCustomerId.
     */
    public XPathLong getNewMasterCustomerId(Node message) {
        return getLong(MASTER_ID_NEW_EXPR, message);
    }

    /**
     * Gets InfoIsDeleted.
     * @param message message.
     * @return InfoIsDeleted.
     */
    public XPathString getNewInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_NEW_EXPR, message);
    }

    /**
     * Gets MasterCustomerId.
     * @param message message.
     * @return MasterCustomerId.
     */
    public XPathLong getOldMasterCustomerId(Node message) {
        return getLong(MASTER_ID_OLD_EXPR, message);
    }

    /**
     * Gets InfoIsDeleted.
     * @param message message.
     * @return InfoIsDeleted.
     */
    public XPathString getOldInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_OLD_EXPR, message);
    }

    /**
     * Gets the New Value of FirstName.
     * @param message message.
     * @return FirstName.
     */
    public XPathString getNewFirstName(Node message) {
        return getString(FIRST_NAME_NEW_EXPR, message);
    }

    /**
     * Gets the OLD Value of FirstName.
     * @param message message.
     * @return FirstName.
     */
    public XPathString getOldFirstName(Node message) {
        return getString(FIRST_NAME_OLD_EXPR, message);
    }

    /**
     * Gets the New Value of LastName.
     * @param message message.
     * @return LastName.
     */
    public XPathString getNewLastName(Node message) {
        return getString(LAST_NAME_NEW_EXPR, message);
    }

    /**
     * Gets the OLD Value of LastName.
     * @param message message.
     * @return LastName.
     */
    public XPathString getOldLastName(Node message) {
        return getString(LAST_NAME_OLD_EXPR, message);
    }

    /**
     * Gets the New Value of MiddleName.
     * @param message message.
     * @return MiddleName.
     */
    public XPathString getNewMiddleName(Node message) {
        return getString(MIDDLE_NAME_NEW_EXPR, message);
    }
    
    /**
     * Gets the OLD Value of MiddleName.
     * @param message message.
     * @return MiddleName.
     */
    public XPathString getOldMiddleName(Node message) {
        return getString(MIDDLE_NAME_OLD_EXPR, message);
    }
}
