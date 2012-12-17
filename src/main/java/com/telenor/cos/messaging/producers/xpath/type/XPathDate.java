package com.telenor.cos.messaging.producers.xpath.type;

import java.util.Date;

/**
 * Wrapper for extracted Date.
 */
public final class XPathDate {

    private final Date value;

    /**
     * Default constructor.
     */
    public XPathDate() {
        this.value = new Date();
    }
    
    /**
     * Constructor.
     *
     * @param value Date value
     */
    public XPathDate(Date value) {
        this.value = value == null ? null : (Date) value.clone();
    }


    /**
     * @return Date
     */
    public Date getValue() {
        return value == null ? null :(Date) value.clone();
    }

    /**
     * Factory method.
     *
     * @param date to set
     * @return XPathDate
     */
    public static XPathDate valueOf(final Date date){
        return new XPathDate(date);
    }

    /**
     * Convenience method for getting the value of the XPathDate object,
     * returns null if object is null.
     *
     * @param xPathDate to get value from
     * @return value
     */
    public static Date getValue(XPathDate xPathDate){
        return xPathDate == null ? null : xPathDate.getValue();
    }
}
