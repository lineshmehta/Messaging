package com.telenor.cos.messaging.producers.xpath.type;

/**
 * Wrapper for extracted Long.
 */
public final class XPathLong {

    private final Long value;

    /**
     * Factory method.
     *
     * @param value to set
     * @return XPathLong
     */
    public static XPathLong valueOf(int value){
        return new XPathLong(Long.valueOf(value));
    }

    /**
     * Factory method.
     *
     * @param value to set
     * @return XPathLong
     */
    public static XPathLong valueOf(String value){
        return new XPathLong(Long.valueOf(value));
    }

    /**
     * Factory method.
     *
     * @param value to set
     * @return XPathLong
     */
    public static XPathLong valueOf(Long value){
        return new XPathLong(value);
    }

    /**
     * Convinience method for getting the value of the XPathLong object,
     * returns null if object is null.
     *
     * @param xPathLong to get value from
     * @return value
     */
    public static Long getValue(XPathLong xPathLong){
        return xPathLong == null ? null : xPathLong.getValue();
    }

    /**
     * @return Long value
     */
    public Long getValue() {
        return value;
    }

    /**
     * Constructor.
     *
     * @param value Long value
     */
    private XPathLong(Long value) {
        this.value = value;
    }
}
