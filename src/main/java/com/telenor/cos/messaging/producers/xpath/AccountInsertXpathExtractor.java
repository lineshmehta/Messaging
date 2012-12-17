package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class AccountInsertXpathExtractor extends XPathExtractor {

    private static final String ACCOUNT_ID_EXPR = "//insert[@schema='ACCOUNT']//values//cell[@name='ACC_ID']";
    private static final String ACCOUNT_NAME_EXPR = "//insert[@schema='ACCOUNT']//values//cell[@name='ACC_NAME']";
    private static final String ACCOUNT_TYPE_ID_EXPR = "//insert[@schema='ACCOUNT']//values//cell[@name='ACC_TYPE_ID']";
    private static final String ACCOUNT_STATUS_ID_EXPR = "//insert[@schema='ACCOUNT']//values//cell[@name='ACC_STATUS_ID2']";
    private static final String ACCOUNT_PAYMENT_STATUS_EXPR = "//insert[@schema='ACCOUNT']//values//cell[@name='ACC_STATUS_ID']";
    private static final String ACCOUNT_CUST_ID_PAYER_EXPR = "//insert[@schema='ACCOUNT']//values//cell[@name='CUST_ID_PAYER']";
    private static final String ACCOUNT_INVOICE_FORMAT_EXPR = "//insert[@schema='ACCOUNT']//values//cell[@name='ACC_INV_MEDIUM']";
    private static final String ACCOUNT_CUST_ID_RESP_EXPR = "//insert[@schema='ACCOUNT']//values//cell[@name='CUST_ID_RESP']";

    /**
     * Gets AccountId from message.
     * @param message message
     * @return accountId.
     */
    public XPathLong getAccountId(Node message) {
        return getLong(ACCOUNT_ID_EXPR, message);
    }

    /**
     * Gets AccountName.
     * @param message message
     * @return accountName.
     */
    public XPathString getAccountName(Node message) {
        return getString(ACCOUNT_NAME_EXPR, message);
    }

    /**
     * Gets AccountTypeId.
     * @param message message.
     * @return accountTypeId.
     */
    public XPathString getAccountTypeId(Node message) {
        return getString(ACCOUNT_TYPE_ID_EXPR, message);
    }

    /**
     * Gets AccountStatusId
     * @param message message.
     * @return accountStatusId.
     */
    public XPathString getAccountStatusId(Node message) {
        return getString(ACCOUNT_STATUS_ID_EXPR, message);
    }

    /**
     * Gets AccountPaymentStatus.
     * @param message message.
     * @return accountPaymentStatus.
     */
    public XPathString getAccountPaymentStatus(Node message) {
        return getString(ACCOUNT_PAYMENT_STATUS_EXPR, message);
    }

    /**
     * Gets AccountCustomerPayerId.
     * @param message message.
     * @return payerCustId.
     */
    public XPathLong getAccountCustIdPayer(Node message) {
        return getLong(ACCOUNT_CUST_ID_PAYER_EXPR, message);
    }

    /**
     * Gets AccountInvoiceFormat.
     * @param message message.
     * @return accountInvoiceFormat.
     */
    public XPathString getAccountInvoiceFormat(Node message) {
        return getString(ACCOUNT_INVOICE_FORMAT_EXPR, message);
    }

    /**
     * Gets AccountCustomerRespId
     * @param message message
     * @return custIdResp.
     */
    public XPathLong getAccountCustIdResp(Node message) {
        return getLong(ACCOUNT_CUST_ID_RESP_EXPR, message);
    }
}
