package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class SubscriptionUpdateXpathExtractor extends XPathExtractor {

    private static final String SUBSCR_ID_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_ID']";
    private static final String CONTRACT_ID_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='CONTRACT_ID']";
    private static final String PRODUCT_ID_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='PRODUCT_ID']";
    private static final String S212_PRODUCT_ID_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='S212_PRODUCT_ID']";
    private static final String DIR_NUMBER_ID_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='DIRECTORY_NUMBER_ID']";
    private static final String CUST_ID_USER_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='CUST_ID_USER']";
    private static final String SUBSCR_VALID_FROM_DATE_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_VALID_FROM_DATE']";
    private static final String SUBSCR_VALID_TO_DATE_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_VALID_TO_DATE']";
    private static final String SUBSCR_STATUS_ID_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_STATUS_ID']";
    private static final String SUBSCR_HAS_SECRET_NUMBER_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_HAS_SECRET_NUMBER']";
    private static final String ACC_ID_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='ACC_ID']";
    private static final String INFO_IS_DELETED_NEW_EXPR = "//update[@schema='SUBSCRIPTION']//values//cell[@name='INFO_IS_DELETED']";

    private static final String SUBSCR_ID_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='SUBSCR_ID']";
    private static final String CONTRACT_ID_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='CONTRACT_ID']";
    private static final String PRODUCT_ID_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='PRODUCT_ID']";
    private static final String DIR_NUMBER_ID_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='DIRECTORY_NUMBER_ID']";
    private static final String CUST_ID_USER_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='CUST_ID_USER']";
    private static final String SUBSCR_VALID_FROM_DATE_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='SUBSCR_VALID_FROM_DATE']";
    private static final String SUBSCR_VALID_TO_DATE_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='SUBSCR_VALID_TO_DATE']";
    private static final String SUBSCR_STATUS_ID_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='SUBSCR_STATUS_ID']";
    private static final String SUBSCR_HAS_SECRET_NUMBER_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='SUBSCR_HAS_SECRET_NUMBER']";
    private static final String ACC_ID_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='ACC_ID']";
    private static final String INFO_IS_DELETED_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='INFO_IS_DELETED']";
    private static final String SUBSCR_TYPE_ID_OLD_EXPR = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='SUBSCR_TYPE_ID']";

    /**
     * Gets NewSubscrId.
     * @param message message.
     * @return NewSubscrId.
     */
    public XPathLong getNewSubscrId(Node message) {
        return getLong(SUBSCR_ID_NEW_EXPR, message);
    }

    /**
     * Gets ContractId.
     * @param message message.
     * @return ContractId.
     */
    public XPathString getNewContractId(Node message) {
        return getString(CONTRACT_ID_NEW_EXPR, message);
    }

    /**
     * Gets S212ProductId.
     * @param message message.
     * @return S212ProductId.
     */
    public XPathString getNewS212ProductId(Node message){
        return getString(S212_PRODUCT_ID_NEW_EXPR, message);
    }

    /**
     * Gets ProductId.
     * @param message message.
     * @return ProductId.
     */
    public XPathString getNewProductId(Node message) {
        return getString(PRODUCT_ID_NEW_EXPR, message);
    }

    /**
     * Gets DirNumberId.
     * @param message message.
     * @return DirNumberId.
     */
    public XPathString getNewDirNumberId(Node message) {
        return getString(DIR_NUMBER_ID_NEW_EXPR, message);
    }

    /**
     * Gets CustIdUser.
     * @param message message.
     * @return CustIdUser.
     */
    public XPathLong getNewCustIdUser(Node message) {
        return getLong(CUST_ID_USER_NEW_EXPR, message);
    }

    /**
     * Gets SubscrValidFromDate.
     * @param message message.
     * @return SubscrValidFromDate.
     */
    public XPathDate getNewSubscrValidFromDate(Node message) {
        return getDate(SUBSCR_VALID_FROM_DATE_NEW_EXPR, message);
    }

    /**
     * Gets AccId.
     * @param message message.
     * @return AccId.
     */
    public XPathLong getNewAccId(Node message) {
        return getLong(ACC_ID_NEW_EXPR, message);
    }

    /**
     * Gets ContractId.
     * @param message message.
     * @return ContractId.
     */
    public XPathString getOldContractId(Node message) {
        return getString(CONTRACT_ID_OLD_EXPR, message);
    }

    /**
     * Gets ProductId.
     * @param message message.
     * @return ProductId.
     */
    public XPathString getOldProductId(Node message) {
        return getString(PRODUCT_ID_OLD_EXPR, message);
    }

    /**
     * Gets DirNumberId.
     * @param message message.
     * @return DirNumberId.
     */
    public XPathString getOldDirNumberId(Node message) {
        return getString(DIR_NUMBER_ID_OLD_EXPR, message);
    }

    /**
     * Gets CustIdUser.
     * @param message message.
     * @return CustIdUser.
     */
    public XPathLong getOldCustIdUser(Node message) {
        return getLong(CUST_ID_USER_OLD_EXPR, message);
    }

    /**
     * Gets AccId.
     * @param message message.
     * @return AccId.
     */
    public XPathLong getOldAccId(Node message) {
        return getLong(ACC_ID_OLD_EXPR, message);
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
     * Gets InfoIsDeleted.
     * @param message message.
     * @return InfoIsDeleted.
     */
    public XPathString getOldInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_OLD_EXPR, message);
    }

    /**
     * Gets SubscrValidFromDate.
     * @param message message.
     * @return SubscrValidFromDate.
     */
    public XPathDate getOldSubscrValidFromDate(Node message) {
        return getDate(SUBSCR_VALID_FROM_DATE_OLD_EXPR, message);
    }

    /**
     * Gets new SubscrValidtoDate
     * @param message message
     * @return SubscrValidtoDate
     */
    public XPathDate getNewSubscrValidToDate(Node message){
        return getDate(SUBSCR_VALID_TO_DATE_NEW_EXPR, message);
    }

    /**
     * Gets SubscrValidToDate.
     * @param message message.
     * @return SubscrValidToDate.
     */
    public XPathDate getOldSubscrValidToDate(Node message) {
        return getDate(SUBSCR_VALID_TO_DATE_OLD_EXPR, message);
    }

    /**
     * Gets SubscrStatusId.
     * @param message message.
     * @return SubscrStatusId.
     */
    public XPathString getNewSubscrStatusId(Node message) {
        return getString(SUBSCR_STATUS_ID_NEW_EXPR, message);
    }

    /**
     * Gets SubscrId.
     * @param message message.
     * @return SubscrId.
     */
    public XPathLong getOldSubscrId(Node message) {
        return getLong(SUBSCR_ID_OLD_EXPR, message);
    }

    /**
     * Gets SubscrStatusId.
     * @param message message.
     * @return SubscrStatusId.
     */
    public XPathString getOldSubscrStatusId(Node message) {
        return getString(SUBSCR_STATUS_ID_OLD_EXPR, message);
    }

    /**
     * Get new SubscriptionSecretNumber.
     * @param message message
     * @return SubscriptionSecretNumber
     */
    public XPathString getNewSubscrHasSecretNumber(Node message) {
        return getString(SUBSCR_HAS_SECRET_NUMBER_NEW_EXPR, message);
    }

    /**
     * Get old SubscriptionSecretNumber.
     * @param message message
     * @return SubscriptionSecretNumber
     */
    public XPathString getOldSubscrHasSecretNumber(Node message) {
        return getString(SUBSCR_HAS_SECRET_NUMBER_OLD_EXPR, message);
    }

    /**
     * Get old SubscriptionTypeId.
     * @param message message
     * @return SubscriptionTypeId
     */
    public XPathString getOldSubscrTypeId(Node message){
        return getString(SUBSCR_TYPE_ID_OLD_EXPR, message);
    }
}
