package com.telenor.cos.messaging.event.account;

import com.telenor.cos.messaging.event.Event;

/**
 * Creates Event for ACC_INV_MEDIUM(invoiceFormat field in Account.java)
 * column change in Account Table.
 * @author Babaprakash D
 *
 */
public class AccountInvoiceFormatUpdateEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    private String invoiceFormat;


    /**
     * Constructor for AccountInvoiceFormatUpdateEvent.
     * @param accountId AccountId.
     * @param invoiceFormat invoiceFormat.
     */
    public AccountInvoiceFormatUpdateEvent(Long accountId,String invoiceFormat) {
        super(accountId, ACTION.INVOICE_FORMAT_CHANGE, TYPE.ACCOUNT);
        this.invoiceFormat = invoiceFormat;
    }

    public String getInvoiceFormat() {
        return invoiceFormat;
    }
}
