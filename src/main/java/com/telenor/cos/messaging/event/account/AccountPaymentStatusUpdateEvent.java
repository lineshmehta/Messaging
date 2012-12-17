package com.telenor.cos.messaging.event.account;

import com.telenor.cos.messaging.event.Event;

/**
 * Creates Event for ACC_STATUS_ID(paymentStatus field in Account.java) column change in Account Table.
 * @author Babaprakash D
 *
 */
public class AccountPaymentStatusUpdateEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;
    private String paymentStatus;

    /**
     * Constructor of AccountPaymentStatusUpdateEvent
     * @param accountId AccountId.
     * @param paymentStatus paymentStatus.
     */
    public AccountPaymentStatusUpdateEvent(Long accountId,String paymentStatus) {
        super(accountId, ACTION.PAYMENT_STATUS_CHANGE, TYPE.ACCOUNT);
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
