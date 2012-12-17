package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * @author t808074
 * 
 */
@Component
public class CustomerInsertXpathExtractor extends XPathExtractor {

    private static final String CUSTOMER_ID_EXPR = "//insert[@schema='CUSTOMER']//values//cell[@name='CUST_ID']";
    private static final String CUSTOMER_MASTER_CUSTOMER_ID_EXPR = "//insert[@schema='CUSTOMER']//values//cell[@name='MASTER_ID']";
    private static final String CUSTOMER_NAME_EXPR = "//insert[@schema='CUSTOMER']//values//cell[@name='CUST_FIRST_NAME']";
    private static final String CUSTOMER_MIDDLE_NAME_EXPR = "//insert[@schema='CUSTOMER']//values//cell[@name='CUST_MIDDLE_NAME']";
    private static final String CUSTOMER_LAST_NAME_EXPR = "//insert[@schema='CUSTOMER']//values//cell[@name='CUST_LAST_NAME']";
    private static final String CUSTOMER_UNIT_NUMBER_EXPR = "//insert[@schema='CUSTOMER']//values//cell[@name='CUST_UNIT_NUMBER']";
    private static final String CUSTOMER_POSTCODE_ID_MAIN_EXPR = "//insert[@schema='CUSTOMER']//values//cell[@name='POSTCODE_ID_MAIN']";
    private static final String CUSTOMER_POSTCODE_NAME_MAIN_EXPR = "//insert[@schema='CUSTOMER']//values//cell[@name='POSTCODE_NAME_MAIN']";
    private static final String CUSTOMER_ADDRLINE_MAIN_EXPR = "//insert[@schema='CUSTOMER']//values//cell[@name='ADDR_LINE_MAIN']";
    private static final String CUSTOMER_ADDR_CO_NAME_EXPR = "//insert[@schema='CUSTOMER']//values//cell[@name='ADDR_CO_NAME']";
    private static final String CUSTOMER_ADDR_STREET_NAME = "//insert[@schema='CUSTOMER']//values//cell[@name='ADDR_STREET_NAME']";
    private static final String CUSTOMER_ADDR_STREET_NUMBER = "//insert[@schema='CUSTOMER']//values//cell[@name='ADDR_STREET_NUMBER']";

    /**
     * Gets CustomerId.
     * 
     * @param message
     *            message.
     * @return CustomerId.
     */
    public XPathLong getCustomerId(Node message) {
        return getLong(CUSTOMER_ID_EXPR, message);
    }

    /**
     * Gets MasterCustomerId.
     * 
     * @param message
     *            message.
     * @return MasterCustomerId.
     */
    public XPathLong getMasterCustomerId(Node message) {
        return getLong(CUSTOMER_MASTER_CUSTOMER_ID_EXPR, message);
    }

    /**
     * Gets CustomerFirstName.
     * 
     * @param message
     *            message.
     * @return CustomerFirstName.
     */
    public XPathString getCustomerName(Node message) {
        return getString(CUSTOMER_NAME_EXPR, message);
    }

    /**
     * Gets CustomerMiddleName.
     * 
     * @param message
     *            message.
     * @return CustomerMiddleName.
     */
    public XPathString getCustomerMidddleName(Node message) {
        return getString(CUSTOMER_MIDDLE_NAME_EXPR, message);
    }

    /**
     * Gets CustomerLastName.
     * 
     * @param message
     *            message.
     * @return CustomerLastName.
     */
    public XPathString getCustomerLastName(Node message) {
        return getString(CUSTOMER_LAST_NAME_EXPR, message);
    }

    /**
     * Gets CustomerUnitNumber.
     * 
     * @param message
     *            message.
     * @return CustomerUnitNumber.
     */
    public XPathLong getCustomerUnitNumber(Node message) {
        return getLong(CUSTOMER_UNIT_NUMBER_EXPR, message);
    }

    /**
     * Gets PostcodeIdMain.
     * 
     * @param message
     *            message.
     * @return PostcodeIdMain.
     */
    public XPathLong getPostcodeIdMain(Node message) {
        return getLong(CUSTOMER_POSTCODE_ID_MAIN_EXPR, message);
    }

    /**
     * Gets PostcodeNameMain.
     * 
     * @param message
     *            message.
     * @return PostcodeNameMain.
     */
    public XPathString getPostcodeNameMain(Node message) {
        return getString(CUSTOMER_POSTCODE_NAME_MAIN_EXPR, message);
    }

    /**
     * Gets AddressLineMain.
     * 
     * @param message
     *            message.
     * @return AddressLineMain.
     */
    public XPathString getAddressLineMain(Node message) {
        return getString(CUSTOMER_ADDRLINE_MAIN_EXPR, message);
    }

    /**
     * Gets AddressCOName.
     * 
     * @param message
     *            message.
     * @return AddressCOName.
     */
    public XPathString getAddressCOName(Node message) {
        return getString(CUSTOMER_ADDR_CO_NAME_EXPR, message);
    }

    /**
     * Gets AddressStreetName.
     * 
     * @param message
     *            message.
     * @return AddressStreetName.
     */
    public XPathString getAddressStreetName(Node message) {
        return getString(CUSTOMER_ADDR_STREET_NAME, message);
    }

    /**
     * Gets AddressStreetNumber.
     * 
     * @param message
     *            message.
     * @return AddressStreetNumber.
     */
    public XPathString getAddressStreetNumber(Node message) {
        return getString(CUSTOMER_ADDR_STREET_NUMBER, message);
    }
}
