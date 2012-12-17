package com.telenor.cos.messaging.event.account;

import com.telenor.cos.messaging.event.Event;

/**
 * Creates Event for ACC_STATUS_ID2(status in Account.java) column change in Account Table. 
 * @author Babaprakash D
 *
 */
public class AccountStatusUpdateEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    private String status;

    /**
     * Constructor of AccountStatusUpdateEvent.
     * @param accountId accountId.
     * @param status account status.
     */
    public AccountStatusUpdateEvent(Long accountId,String status) {
        super(accountId, ACTION.STATUS_UPDATE, TYPE.ACCOUNT);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
