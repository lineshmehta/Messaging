package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class SubscriptionInsertXpathExtractor extends XPathExtractor{

    private static final String SUBSCR_ID_EXPR = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_ID']";
    private static final String ACC_ID_EXPR = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='ACC_ID']";
    private static final String CUST_ID_USER_EXPR = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='CUST_ID_USER']";
    private static final String SUBSCR_VALID_FROM_DATE_EXPR = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_VALID_FROM_DATE']";
    private static final String SUBSCR_VALID_TO_DATE_EXPR = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_VALID_TO_DATE']";
    private static final String SUBSCR_HAS_SECRET_NUMBER_EXPR = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_HAS_SECRET_NUMBER']";
    private static final String DIRECTORY_NUMBER_ID_EXPR = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='DIRECTORY_NUMBER_ID']";
    private static final String CONTRACT_ID_INSERT_EXPR = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='CONTRACT_ID']";
    private static final String S212_PRODUCT_ID_EXPR = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='S212_PRODUCT_ID']";
    private static final String STATUS_ID_INSERT_EXPR = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_STATUS_ID']";

    /**
     * Gets SubscrId.
     * @param message message.
     * @return SubscrId.
     */
    public XPathLong getSubscrId(Node message) {
        return getLong(SUBSCR_ID_EXPR, message);
    }

    /**
     * Gets AccId.
     * @param message message.
     * @return AccId.
     */
    public XPathLong getAccId(Node message) {
        return getLong(ACC_ID_EXPR, message);
    }

    /**
     * Gets CustId.
     * @param message message.
     * @return CustId.
     */
    public XPathLong getCustId(Node message) {
        return getLong(CUST_ID_USER_EXPR, message);
    }

    /**
     * Gets SubscrValidFromDate.
     * @param message message.
     * @return SubscrValidFromDate.
     */
    public XPathDate getSubscrValidFromDate(Node message) {
        return getDate(SUBSCR_VALID_FROM_DATE_EXPR, message);
    }

    /**
     * Gets SubscrValidToDate.
     * @param message message.
     * @return SubscrValidToDate
     */
    public XPathDate getSubscrValidToDate(Node message) {
        return getDate(SUBSCR_VALID_TO_DATE_EXPR, message);
    }

    /**
     * Gets DirectoryNumberId.
     * @param message message.
     * @return DirectoryNumberId
     */
    public XPathLong getDirectoryNumberId(Node message) {
        return getLong(DIRECTORY_NUMBER_ID_EXPR, message);
    }

    /**
     * Gets ContractId.
     * @param message message.
     * @return ContractId.
     */
    public XPathLong getContractId(Node message) {
        return getLong(CONTRACT_ID_INSERT_EXPR, message);
    }

    /**
     * Gets S212ProductId.
     * @param message message.
     * @return S212ProductId
     */
    public XPathString getS212ProductId(Node message) {
        return getString(S212_PRODUCT_ID_EXPR, message);
    }

    /**
     * Gets subscrHasSecretNumber
     * @param message message
     * @return subscrHasSecretNumber
     */
    public XPathString getSubscrHasSecretNumber(Node message){
        return getString(SUBSCR_HAS_SECRET_NUMBER_EXPR, message);
    }


    /**
     * Gets the StatusId
     * @param message message
     * @return StatusId
     */
    public XPathString getStatusId(Node message) {
        return getString(STATUS_ID_INSERT_EXPR, message);
    }

}
