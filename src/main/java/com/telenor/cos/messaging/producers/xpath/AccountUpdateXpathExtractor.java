package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * XPath Extractor for AccountUpdateEvents.
 * @author Babaprakash D
 *
 */
@Component
public class AccountUpdateXpathExtractor extends XPathExtractor {

    private static final String ACCOUNT_UPDATE_FILTER_EXPR = "//update[@schema='ACCOUNT']";
    private static final String ACCOUNT_ID_EXPR = "//update[@schema='ACCOUNT']//oldValues//cell[@name='ACC_ID']";
    private static final String ACCOUNT_NAME_OLD_EXPR = "//update[@schema='ACCOUNT']//oldValues//cell[@name='ACC_NAME']";
    private static final String ACCOUNT_NAME_NEW_EXPR = "//update[@schema='ACCOUNT']//values//cell[@name='ACC_NAME']";
    private static final String ACCOUNT_TYPE_ID_OLD_EXPR = "//update[@schema='ACCOUNT']//oldValues//cell[@name='ACC_TYPE_ID']";
    private static final String ACCOUNT_TYPE_ID_NEW_EXPR = "//update[@schema='ACCOUNT']//values//cell[@name='ACC_TYPE_ID']";
    private static final String ACCOUNT_STATUS_ID2_OLD_EXPR = "//update[@schema='ACCOUNT']//oldValues//cell[@name='ACC_STATUS_ID2']";
    private static final String ACCOUNT_STATUS_ID2_NEW_EXPR = "//update[@schema='ACCOUNT']//values//cell[@name='ACC_STATUS_ID2']";
    private static final String ACCOUNT_STATUS_ID_OLD_EXPR = "//update[@schema='ACCOUNT']//oldValues//cell[@name='ACC_STATUS_ID']";
    private static final String ACCOUNT_STATUS_ID_NEW_EXPR = "//update[@schema='ACCOUNT']//values//cell[@name='ACC_STATUS_ID']";
    private static final String ACCOUNT_INVOICE_MEDIUM_OLD_EXPR = "//update[@schema='ACCOUNT']//oldValues//cell[@name='ACC_INV_MEDIUM']";
    private static final String ACCOUNT_INVOICE_MEDIUM_NEW_EXPR = "//update[@schema='ACCOUNT']//values//cell[@name='ACC_INV_MEDIUM']";
    private static final String CUST_ID_PAYER_OLD_EXPR = "//update[@schema='ACCOUNT']//oldValues//cell[@name='CUST_ID_PAYER']";
    private static final String CUST_ID_PAYER_NEW_EXPR = "//update[@schema='ACCOUNT']//values//cell[@name='CUST_ID_PAYER']";
    private static final String CUST_ID_RESP_OLD_EXPR = "//update[@schema='ACCOUNT']//oldValues//cell[@name='CUST_ID_RESP']";
    private static final String CUST_ID_RESP_NEW_EXPR = "//update[@schema='ACCOUNT']//values//cell[@name='CUST_ID_RESP']";
    private static final String ACCOUNT_INFO_IS_DELETED_UPDATE_EXPR = "//update[@schema='ACCOUNT']//values//cell[@name='INFO_IS_DELETED']";
    private static final String ACCOUNT_INFO_IS_DELETED_UPDATE_OLD_EXPR = "//update[@schema='ACCOUNT']//oldValues//cell[@name='INFO_IS_DELETED']";

    /**
     * @param message message.
     * @return message.
     */
    public XPathString getUpdateFilter(Node message) {
        return getString(ACCOUNT_UPDATE_FILTER_EXPR, message);
    }

    /**
     * Gets AccountId.
     * @param message message.
     * @return accountId.
     */
    public XPathLong getAccountId(Node message) {
        return getLong(ACCOUNT_ID_EXPR, message);
    }

    /**
     * Gets InfoIsDelete field status.
     * @param message message.
     * @return infoIsDeleted.
     */
    public XPathString getNewInfoIsDeleted(Node message) {
        return getString(ACCOUNT_INFO_IS_DELETED_UPDATE_EXPR, message);
    }

    /**
     * Gets InfoIsDelete field status.
     * @param message message.
     * @return infoIsDeleted.
     */
    public XPathString getOldInfoIsDeleted(Node message) {
        return getString(ACCOUNT_INFO_IS_DELETED_UPDATE_OLD_EXPR, message);
    }

    /**
     * Gets AccountName.
     * @param message message.
     * @return accountName.
     */
    public XPathString getOldAccountName(Node message) {
        return getString(ACCOUNT_NAME_OLD_EXPR, message);
    }

    /**
     * Gets AccountName.
     * @param message message.
     * @return accountName.
     */
    public XPathString getNewAccountName(Node message) {
        return getString(ACCOUNT_NAME_NEW_EXPR, message);
    }

    /**
     * Gets AccountTypeId.
     * @param message message.
     * @return AccountTypeId.
     */
    public XPathString getOldAccountTypeId(Node message) {
        return getString(ACCOUNT_TYPE_ID_OLD_EXPR, message);
    }

    /**
     * Gets AccountTypeId.
     * @param message message.
     * @return AccountTypeId.
     */
    public XPathString getNewAccountTypeId(Node message) {
        return getString(ACCOUNT_TYPE_ID_NEW_EXPR, message);
    }

    /**
     * Gets accountStatusId2.
     * @param message message.
     * @return accountStatusId2.
     */
    public XPathString getOldAccountStatusId2(Node message) {
        return getString(ACCOUNT_STATUS_ID2_OLD_EXPR, message);
    }

    /**
     * Gets accountStatusId2.
     * @param message message.
     * @return accountStatusId2.
     */
    public XPathString getNewAccountStatusId2(Node message) {
        return getString(ACCOUNT_STATUS_ID2_NEW_EXPR, message);
    }

    /**
     * Gets accountStatusId.
     * @param message message.
     * @return accountStatusId.
     */
    public XPathString getOldAccountStatusId(Node message) {
        return getString(ACCOUNT_STATUS_ID_OLD_EXPR, message);
    }

    /**
     * Gets accountStatusId.
     * @param message message.
     * @return accountStatusId.
     */
    public XPathString getNewAccountStatusId(Node message) {
        return getString(ACCOUNT_STATUS_ID_NEW_EXPR, message);
    }

    /**
     * Gets AccountInvMedium.
     * @param message message.
     * @return AccountInvMedium.
     */
    public XPathString getOldAccountInvMedium(Node message) {
        return getString(ACCOUNT_INVOICE_MEDIUM_OLD_EXPR, message);
    }

    /**
     * Gets AccountInvMedium.
     * @param message message.
     * @return AccountInvMedium.
     */
    public XPathString getNewAccountInvMedium(Node message) {
        return getString(ACCOUNT_INVOICE_MEDIUM_NEW_EXPR, message);
    }

    /**
     * Gets CustIdPayer.
     * @param message message.
     * @return CustIdPayer.
     */
    public XPathString getOldCustIdPayer(Node message) {
        return getString(CUST_ID_PAYER_OLD_EXPR, message);
    }

    /**
     * Gets CustIdPayer.
     * @param message message.
     * @return CustIdPayer.
     */
    public XPathString getNewCustIdPayer(Node message) {
        return getString(CUST_ID_PAYER_NEW_EXPR, message);
    }

    /**
     * Gets CustIdResp.
     * @param message message.
     * @return CustIdResp.
     */
    public XPathString getOldCustIdResp(Node message) {
        return getString(CUST_ID_RESP_OLD_EXPR, message);
    }

    /**
     * Gets CustIdResp.
     * @param message message.
     * @return CustIdResp.
     */
    public XPathString getNewCustIdResp(Node message) {
        return getString(CUST_ID_RESP_NEW_EXPR, message);
    }
}
