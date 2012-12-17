package com.telenor.cos.messaging.event.account;

import com.telenor.cos.messaging.event.Event;
/**
 * Creates Event for Account Logical Delete.
 * @author Babaprakash D
 *
 */
public class AccountLogicalDeleteEvent extends Event {

    private static final long serialVersionUID = -1L;

    /**
     * Constructor of AccountLogicalDeleteEvent
     * @param accountId AccountId.
     */
    public AccountLogicalDeleteEvent(Long accountId) {
        super(accountId, ACTION.LOGICAL_DELETE, TYPE.ACCOUNT);
    }
}
