package com.telenor.cos.messaging.producers.xpath.type;

/**
 * Wrapper for extracted String.
 */
public final class XPathString {

    private final String value;

    /**
     * Factory method.
     *
     * @param value to set
     * @return XPathString
     */
    public static XPathString valueOf(String value){
        return new XPathString(value);
    }

    /**
     * Convinience method for getting the value of the XPathString object,
     * returns null if object is null.
     *
     * @param xPathString to get value from
     * @return value
     */
    public static String getValue(XPathString xPathString){
        return xPathString == null ? null : xPathString.getValue();
    }

    /**
     * @return String value
     */
    public String getValue() {
        return value;
    }

    /**
     * Constructor.
     *
     * @param value String value
     */
    private XPathString(String value) {
        this.value = value;
    }
}
