package com.telenor.cos.messaging.event.userref;

import com.telenor.cos.messaging.event.Event;

/**
 * Event for Update Messages in USER_REFERENCE(EINVOICE_REF) table.
 * @author Babaprakash D
 *
 */
public class InvoiceReferenceUpdateEvent extends Event {

    /**
     *
     */
    private static final long serialVersionUID = -1580020444398586541L;
    private String invoiceRef;

    /**
     * Constructor of InvoiceReferenceUpdateEvent
     * @param subscriptionId the subscriptionId
     * @param invoiceRef invoiceRef.
     */
    public InvoiceReferenceUpdateEvent(Long subscriptionId,String invoiceRef) {
        super(subscriptionId, ACTION.INVOICE_CHANGE, TYPE.USER_REFERENCE);
        this.invoiceRef = invoiceRef;
    }

    public String getInvoiceRef() {
        return invoiceRef;
    }
}