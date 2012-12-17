package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * Implementation class for MasterCustomerInsertXPathExtractor.
 * 
 * @author Babaprakash
 * 
 */
@Component
public class MasterCustomerInsertXpathExtractor extends XPathExtractor {

    private static final String MASTER_ID_EXPR = "//insert[@schema='MASTER_CUSTOMER']//values//cell[@name='MASTER_ID']";
    private static final String FIRST_NAME_EXPR = "//insert[@schema='MASTER_CUSTOMER']//values//cell[@name='CUST_FIRST_NAME']";
    private static final String MIDDLE_NAME_EXPR = "//insert[@schema='MASTER_CUSTOMER']//values//cell[@name='CUST_MIDDLE_NAME']";
    private static final String LAST_NAME_EXPR = "//insert[@schema='MASTER_CUSTOMER']//values//cell[@name='CUST_LAST_NAME']";
    private static final String ORGANIZATION_NUMBER_EXPR = "//insert[@schema='MASTER_CUSTOMER']//values//cell[@name='CUST_UNIT_NUMBER']";
    private static final String KURT_ID_INSERT_EXPR = "//insert[@schema='MASTER_CUSTOMER']//values//cell[@name='KURT_ID']";

    /**
     * Gets MasterCustomerId.
     * 
     * @param message
     *            message.
     * @return MasterCustomerId.
     */
    public XPathLong getMasterCustomerId(Node message) {
        return getLong(MASTER_ID_EXPR, message);
    }

    /**
     * Gets FirstName.
     * 
     * @param message
     *            message.
     * @return FirstName.
     */
    public XPathString getFirstName(Node message) {
        return getString(FIRST_NAME_EXPR, message);
    }

    /**
     * Gets MiddleName.
     * 
     * @param message
     *            message.
     * @return MiddleName.
     */
    public XPathString getMiddleName(Node message) {
        return getString(MIDDLE_NAME_EXPR, message);
    }

    /**
     * Gets LastName.
     * 
     * @param message
     *            message.
     * @return LastName.
     */
    public XPathString getLastName(Node message) {
        return getString(LAST_NAME_EXPR, message);
    }

    /**
     * Gets OrganizationNumber.
     * 
     * @param message
     *            message.
     * @return OrganizationNumber.
     */
    public XPathLong getOrganizationNumber(Node message) {
        return getLong(ORGANIZATION_NUMBER_EXPR, message);
    }

    /**
     * Gets KurtId.
     * 
     * @param message
     *            message.
     * @return KurtId.
     */
    public XPathLong getKurtId(Node message) {
        return getLong(KURT_ID_INSERT_EXPR, message);
    }
}
