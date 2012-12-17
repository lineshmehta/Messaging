package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class MobileOfficeUpdateXpathExtractor extends XPathExtractor {

    private static final String MOBILE_OFFICE_FILTER_EXPR = "//update[@schema='MOBILE_OFFICE_MEMBERS']";
    private static final String DIRECTORY_NUMBER_EXPR = "//update[@schema='MOBILE_OFFICE_MEMBERS']//oldValues//cell[@name='DIRECTORY_NUMBER_ID']";
    private static final String EXTENSION_NUMBER_EXPR = "//update[@schema='MOBILE_OFFICE_MEMBERS']//values//cell[@name='EXTENSION_NUMBER']";
    private static final String EXTENSION_NUMBER_OLD_EXPR = "//update[@schema='MOBILE_OFFICE_MEMBERS']//oldValues//cell[@name='EXTENSION_NUMBER']";
    private static final String INFO_IS_DELETED_EXPR = "//update[@schema='MOBILE_OFFICE_MEMBERS']//values//cell[@name='INFO_IS_DELETED']";
    private static final String INFO_IS_DELETED_OLD_EXPR = "//update[@schema='MOBILE_OFFICE_MEMBERS']//oldValues//cell[@name='INFO_IS_DELETED']";

    /**
     * Filter for MobileOffice Update Messages.
     * 
     * @param message
     *            message.
     * @return update message.
     */
    public XPathString getMobileOfficeUpdateFilter(Node message) {
        return getString(MOBILE_OFFICE_FILTER_EXPR, message);
    }

    /**
     * Gets the directoryNumber from the message
     * 
     * @param message
     *            the message
     * @return directoryNumber
     */
    public XPathString getDirectoryNumberOld(Node message) {
        return getString(DIRECTORY_NUMBER_EXPR, message);
    }

    /**
     * Gets the extensionNumber from the message
     * 
     * @param message
     *            the message
     * @return extensionNumber
     */
    public XPathString getExtensionNumber(Node message) {
        return getString(EXTENSION_NUMBER_EXPR, message);
    }

    /**
     * Gets the extensionNumber from the message
     * 
     * @param message
     *            the message
     * @return extensionNumber
     */
    public XPathString getExtensionNumberOld(Node message) {
        return getString(EXTENSION_NUMBER_OLD_EXPR, message);
    }

    /**
     * Gets infoIsDeleted from the message
     * 
     * @param message
     *            the message
     * @return infoIsDeleted
     */
    public XPathString getInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_EXPR, message);
    }

    /**
     * Gets previous infoIsDeleted from the message
     * 
     * @param message
     *            the message
     * @return previous infoIsDeleted
     */
    public XPathString getInfoIsDeletedOld(Node message) {
        return getString(INFO_IS_DELETED_OLD_EXPR, message);
    }

}
