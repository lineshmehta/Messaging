package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * XPath Extractor for UserReferenceNewEvent.
 * @author Babaprakash D
 *
 */
@Component
public class UserReferenceInsertXpathExtractor extends XPathExtractor {

    private static final String SUBSCRIPTION_ID_EXPR = "//insert[@schema='USER_REFERENCE']//values//cell[@name='SUBSCR_ID']";
    private static final String USER_REF_DESCR_EXPR = "//insert[@schema='USER_REFERENCE']//values//cell[@name='USER_REF_DESCR']";
    private static final String NUMBER_TYPE_EXPR = "//insert[@schema='USER_REFERENCE']//values//cell[@name='NUMBER_TYPE']";
    private static final String E_INVOICE_REF_EXPR = "//insert[@schema='USER_REFERENCE']//values//cell[@name='EINVOICE_REF']";

    /**
     * Gets SubscriptionId.
     * @param message message.
     * @return SubscriptionId.
     */
    public XPathLong getSubscriptionId(Node message) {
        return getLong(SUBSCRIPTION_ID_EXPR, message);
    }

    /**
     * Gets UserRefDescription.
     * @param message message.
     * @return UserRefDescription.
     */
    public XPathString getUserRefDescription(Node message) {
        return getString(USER_REF_DESCR_EXPR, message);
    }

    /**
     * Gets NumberType.
     * @param message message.
     * @return NumberType.
     */
    public XPathString getNumberType(Node message) {
        return getString(NUMBER_TYPE_EXPR, message);
    }
    
    /**
     * Gets einvoiceRef.
     * @param message message.
     * @return einvoiceRef.
     */
    public XPathString getEInvoiceRef(Node message) {
        return getString(E_INVOICE_REF_EXPR, message);
    }
}
