package com.telenor.cos.messaging.event;

import java.io.Serializable;

/**
 * @author Babaprakash D
 *
 */
public class UserReference implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1L;

    private String numberType;
    private String invoiceRef;
    private String userRefDescr;

    public String getNumberType() {
        return numberType;
    }

    public String getInvoiceRef() {
        return invoiceRef;
    }

    public String getUserRefDescr() {
        return userRefDescr;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public void setInvoiceRef(String invoiceRef) {
        this.invoiceRef = invoiceRef;
    }

    public void setUserRefDescr(String userRefDescr) {
        this.userRefDescr = userRefDescr;
    }

    @Override
    public String toString() {
        return "UserReference [numberType=" + numberType + ", invoiceRef="
                + invoiceRef + ", userRefDescr=" + userRefDescr + "]";
    }
}