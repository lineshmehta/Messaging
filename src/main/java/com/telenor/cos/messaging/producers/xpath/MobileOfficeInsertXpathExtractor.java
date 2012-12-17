package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class MobileOfficeInsertXpathExtractor extends XPathExtractor {

    private static final String DIRECTORY_NUMBER_EXPR = "//insert[@schema='MOBILE_OFFICE_MEMBERS']//values//cell[@name='DIRECTORY_NUMBER_ID']";
    private static final String EXTENSION_NUMBER_EXPR = "//insert[@schema='MOBILE_OFFICE_MEMBERS']//values//cell[@name='EXTENSION_NUMBER']";

    /**
     * Gets the directoryNumber from the message
     * 
     * @param message
     *            the message
     * @return directoryNumber
     */
    public XPathString getDirectoryNumber(Node message) {
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
}
