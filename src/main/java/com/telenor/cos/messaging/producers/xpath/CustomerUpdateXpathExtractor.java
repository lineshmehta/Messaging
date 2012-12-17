package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class CustomerUpdateXpathExtractor extends XPathExtractor {

    private static final String CUSTOMER_MASTER_ID_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='MASTER_ID']";
    private static final String CUSTOMER_FIRST_NAME_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='CUST_FIRST_NAME']";
    private static final String CUSTOMER_MIDDLE_NAME_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='CUST_MIDDLE_NAME']";
    private static final String CUSTOMER_LAST_NAME_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='CUST_LAST_NAME']";
    private static final String CUSTOMER_UNIT_NUMBER_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='CUST_UNIT_NUMBER']";
    private static final String INFO_IS_DELETED_NEW_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='INFO_IS_DELETED']";
    private static final String CUSTOMER_POSTCODE_ID_MAIN_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='POSTCODE_ID_MAIN']";
    private static final String CUSTOMER_POSTCODE_NAME_MAIN_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='POSTCODE_NAME_MAIN']";
    private static final String CUSTOMER_ADDRLINE_MAIN_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='ADDR_LINE_MAIN']";
    private static final String CUSTOMER_ADDR_CO_NAME_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='ADDR_CO_NAME']";
    private static final String CUSTOMER_ADDR_STREET_NAME_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='ADDR_STREET_NAME']";
    private static final String CUSTOMER_ADDR_STREET_NUMBER_EXPR = "//update[@schema='CUSTOMER']//values//cell[@name='ADDR_STREET_NUMBER']";

    private static final String CUSTOMER_ID_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='CUST_ID']";
    private static final String CUSTOMER_MASTER_ID_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='MASTER_ID']";
    private static final String CUSTOMER_FIRST_NAME_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='CUST_FIRST_NAME']";
    private static final String CUSTOMER_MIDDLE_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='CUST_MIDDLE_NAME']";
    private static final String CUSTOMER_LAST_NAME_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='CUST_LAST_NAME']";
    private static final String CUSTOMER_UNIT_NUMBER_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='CUST_UNIT_NUMBER']";
    private static final String INFO_IS_DELETED_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='INFO_IS_DELETED']";
    private static final String CUSTOMER_POSTCODE_ID_MAIN_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='POSTCODE_ID_MAIN']";
    private static final String CUSTOMER_POSTCODE_NAME_MAIN_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='POSTCODE_NAME_MAIN']";
    private static final String CUSTOMER_ADDRESS_LINE__OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='ADDR_LINE_MAIN']";
    private static final String CUSTOMER_ADDRESS_CO_NAME_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='ADDR_CO_NAME']";
    private static final String CUSTOMER_ADDRESS_STREET_NAME_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='ADDR_STREET_NAME']";
    private static final String CUSTOMER_ADDRESS_STREET_NUMBER_OLD_EXPR = "//update[@schema='CUSTOMER']//oldValues//cell[@name='ADDR_STREET_NUMBER']";

    /**
     * Gets CustomerFirstName.
     * 
     * @param message
     *            message.
     * @return CustomerFirstName
     */
    public XPathString getNewCustomerFirstName(Node message) {
        return getString(CUSTOMER_FIRST_NAME_EXPR, message);
    }

    /**
     * Gets CustomerMidddleName.
     * 
     * @param message
     *            message.
     * @return CustomerMidddleName
     */
    public XPathString getNewCustomerMidddleName(Node message) {
        return getString(CUSTOMER_MIDDLE_NAME_EXPR, message);
    }

    /**
     * Gets CustomerLastName.
     * 
     * @param message
     *            message.
     * @return CustomerLastName
     */
    public XPathString getNewCustomerLastName(Node message) {
        return getString(CUSTOMER_LAST_NAME_EXPR, message);
    }

    /**
     * Gets CustomerUnitNumber.
     * 
     * @param message
     *            message.
     * @return CustomerUnitNumber
     */
    public XPathLong getNewCustomerUnitNumber(Node message) {
        return getLong(CUSTOMER_UNIT_NUMBER_EXPR, message);
    }

    /**
     * Gets InfoIsDeleted.
     * 
     * @param message
     *            message.
     * @return InfoIsDeleted
     */
    public XPathString getNewInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_NEW_EXPR, message);
    }

    /**
     * Gets InfoIsDeleted.
     * 
     * @param message
     *            message.
     * @return InfoIsDeleted
     */
    public XPathString getOldInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_OLD_EXPR, message);
    }

    /**
     * Gets PostcodeIdMain.
     * 
     * @param message
     *            message.
     * @return PostcodeIdMain
     */
    public XPathLong getNewPostcodeIdMain(Node message) {
        return getLong(CUSTOMER_POSTCODE_ID_MAIN_EXPR, message);
    }

    /**
     * Gets PostcodeNameMain.
     * 
     * @param message
     *            message.
     * @return PostcodeNameMain
     */
    public XPathString getNewPostcodeNameMain(Node message) {
        return getString(CUSTOMER_POSTCODE_NAME_MAIN_EXPR, message);
    }

    /**
     * Gets AdressLineMain.
     * 
     * @param message
     *            message.
     * @return AdressLineMain
     */
    public XPathString getNewAdressLineMain(Node message) {
        return getString(CUSTOMER_ADDRLINE_MAIN_EXPR, message);
    }

    /**
     * Gets AdressCOName.
     * 
     * @param message
     *            message.
     * @return AdressCOName
     */
    public XPathString getNewAdressCOName(Node message) {
        return getString(CUSTOMER_ADDR_CO_NAME_EXPR, message);
    }

    /**
     * Gets AdressStreetName.
     * 
     * @param message
     *            message.
     * @return AdressStreetName
     */
    public XPathString getNewAdressStreetName(Node message) {
        return getString(CUSTOMER_ADDR_STREET_NAME_EXPR, message);
    }

    /**
     * Gets AdressStreetNumber.
     * 
     * @param message
     *            message.
     * @return AdressStreetNumber
     */
    public XPathString getNewAdressStreetNumber(Node message) {
        return getString(CUSTOMER_ADDR_STREET_NUMBER_EXPR, message);
    }

    /**
     * Gets CustomerId.
     * 
     * @param message
     *            message.
     * @return CustomerId
     */
    public XPathLong getOldCustomerId(Node message) {
        return getLong(CUSTOMER_ID_OLD_EXPR, message);
    }

    /**
     * Gets CustomerFirstName.
     * 
     * @param message
     *            message.
     * @return CustomerFirstName
     */
    public XPathString getOldCustomerFirstName(Node message) {
        return getString(CUSTOMER_FIRST_NAME_OLD_EXPR, message);
    }

    /**
     * Gets CustomerMidddleName.
     * 
     * @param message
     *            message.
     * @return CustomerMidddleName
     */
    public XPathString getOldCustomerMidddleName(Node message) {
        return getString(CUSTOMER_MIDDLE_OLD_EXPR, message);
    }

    /**
     * Gets CustomerLastName.
     * 
     * @param message
     *            message.
     * @return CustomerLastName
     */
    public XPathString getOldCustomerLastName(Node message) {
        return getString(CUSTOMER_LAST_NAME_OLD_EXPR, message);
    }

    /**
     * Gets CustomerUnitNumber.
     * 
     * @param message
     *            message.
     * @return CustomerUnitNumber
     */
    public XPathLong getOldCustomerUnitNumber(Node message) {
        return getLong(CUSTOMER_UNIT_NUMBER_OLD_EXPR, message);
    }

    /**
     * Gets PostcodeIdMain.
     * 
     * @param message
     *            message.
     * @return PostcodeIdMain
     */
    public XPathLong getOldPostcodeIdMain(Node message) {
        return getLong(CUSTOMER_POSTCODE_ID_MAIN_OLD_EXPR, message);
    }

    /**
     * Gets PostcodeNameMain.
     * 
     * @param message
     *            message.
     * @return PostcodeNameMain
     */
    public XPathString getOldPostcodeNameMain(Node message) {
        return getString(CUSTOMER_POSTCODE_NAME_MAIN_OLD_EXPR, message);
    }

    /**
     * Gets MasterCustomerId.
     * 
     * @param message
     *            message.
     * @return MasterCustomerId
     */
    public XPathLong getNewMasterCustomerId(Node message) {
        return getLong(CUSTOMER_MASTER_ID_EXPR, message);
    }

    /**
     * Gets MasterCustomerId.
     * 
     * @param message
     *            message.
     * @return MasterCustomerId
     */
    public XPathLong getOldMasterCustomerId(Node message) {
        return getLong(CUSTOMER_MASTER_ID_OLD_EXPR, message);
    }

    /**
     * Gets AddressLineMain.
     * 
     * @param message
     *            message.
     * @return AddressLineMain
     */
    public XPathString getOldAddressLineMain(Node message) {
        return getString(CUSTOMER_ADDRESS_LINE__OLD_EXPR, message);
    }

    /**
     * Gets AddressCOName.
     * 
     * @param message
     *            message.
     * @return AddressCOName
     */
    public XPathString getOldAddressCOName(Node message) {
        return getString(CUSTOMER_ADDRESS_CO_NAME_OLD_EXPR, message);
    }

    /**
     * Gets AddressStreetName.
     * 
     * @param message
     *            message.
     * @return AddressStreetName
     */
    public XPathString getOldAddressStreetName(Node message) {
        return getString(CUSTOMER_ADDRESS_STREET_NAME_OLD_EXPR, message);
    }

    /**
     * Gets AddressStreetNumber.
     * 
     * @param message
     *            message.
     * @return AddressStreetNumber
     */
    public XPathString getOldAddressStreetNumber(Node message) {
        return getString(CUSTOMER_ADDRESS_STREET_NUMBER_OLD_EXPR,
                message);
    }
}
