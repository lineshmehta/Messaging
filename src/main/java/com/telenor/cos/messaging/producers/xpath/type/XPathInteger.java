package com.telenor.cos.messaging.producers.xpath.type;

/**
 * Wrapper for extracted Integer.
 */
public final class XPathInteger {

    private final Integer value;

    /**
     * Factory methos.
     *
     * @param value value
     * @return XPathInteger
     */
    public static XPathInteger valueOf(Integer value){
        return new XPathInteger(value);

    }

    /**
     * @return Integer value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Convinience method for getting the value of the XPathInteger object,
     * returns null if object is null.
     *
     * @param xPathInteger to get value from
     * @return value
     */
    public static Integer getValue(XPathInteger xPathInteger){
        return xPathInteger == null ? null : xPathInteger.getValue();
    }

    /**
     * Constructor.
     *
     * @param value Integer value
     */
    private XPathInteger(Integer value) {
        this.value = value;
    }
}
