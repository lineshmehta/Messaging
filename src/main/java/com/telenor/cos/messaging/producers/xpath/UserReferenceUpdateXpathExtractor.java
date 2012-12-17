package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * XPath Extractor for UserReference Update Event.
 * 
 */
@Component
public class UserReferenceUpdateXpathExtractor extends XPathExtractor {

    private static final String USER_REFERENCE_UPDATE_FILTER_EXPR = "//update[@schema='USER_REFERENCE']";
    private static final String SUBCRIPT_ID_OLD_EXPR = "//update[@schema='USER_REFERENCE']//oldValues//cell[@name='SUBSCR_ID']";
    private static final String E_INVOICE_REF_NEW_EXPR = "//update[@schema='USER_REFERENCE']//values//cell[@name='EINVOICE_REF']";
    private static final String E_INVOICE_REF_OLD_EXPR = "//update[@schema='USER_REFERENCE']//oldValues//cell[@name='EINVOICE_REF']";
    private static final String NUMBER_TYPE_OLD_EXPR = "//update[@schema='USER_REFERENCE']//oldValues//cell[@name='NUMBER_TYPE']";
    private static final String USER_REF_DESCR_NEW_EXPR = "//update[@schema='USER_REFERENCE']//values//cell[@name='USER_REF_DESCR']";
    private static final String USER_REF_DESCR_OLD_EXPR = "//update[@schema='USER_REFERENCE']//oldValues//cell[@name='USER_REF_DESCR']";
    private static final String INFO_IS_DELETED_NEW_EXPR = "//update[@schema='USER_REFERENCE']//values//cell[@name='INFO_IS_DELETED']";

    /**
     * Filter for UserReference Update Messages.
     * 
     * @param message
     *            message.
     * @return update message.
     */
    public XPathString getUserReferenceUpdateFilter(Node message) {
        return getString(USER_REFERENCE_UPDATE_FILTER_EXPR, message);
    }

    /**
     * Gets SusbcrId.
     * 
     * @param message
     *            message.
     * @return SusbcrId
     */
    public XPathLong getOldSusbcrId(Node message) {
        return getLong(SUBCRIPT_ID_OLD_EXPR, message);
    }

    /**
     * Gets NumberTypeId.
     * 
     * @param message
     *            message.
     * @return NumberTypeId
     */
    public XPathString getOldNumberTypeId(Node message) {
        return getString(NUMBER_TYPE_OLD_EXPR, message);
    }

    /**
     * Gets UserRefDescr.
     * 
     * @param message
     *            message.
     * @return UserRefDescr
     */
    public XPathString getNewUserRefDescr(Node message) {
        return getString(USER_REF_DESCR_NEW_EXPR, message);
    }

    /**
     * Gets UserRefDescr.
     * 
     * @param message
     *            message.
     * @return UserRefDescr
     */
    public XPathString getOldUserRefDescr(Node message) {
        return getString(USER_REF_DESCR_OLD_EXPR, message);
    }

    /**
     * Gets EinvoiceRef.
     * 
     * @param message
     *            message.
     * @return EinvoiceRef
     */
    public XPathString getNewEinvoiceRef(Node message) {
        return getString(E_INVOICE_REF_NEW_EXPR, message);
    }

    /**
     * Gets EinvoiceRef.
     * 
     * @param message
     *            message.
     * @return EinvoiceRef
     */
    public XPathString getOldEinvoiceRef(Node message) {
        return getString(E_INVOICE_REF_OLD_EXPR, message);
    }

    /**
     * Gets InfoIsDelete field status.
     * 
     * @param message
     *            message.
     * @return infoIsDeleted.
     */
    public XPathString getNewInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_NEW_EXPR, message);
    }
}
