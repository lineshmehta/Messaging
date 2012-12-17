package com.telenor.cos.messaging.event.account;

import com.telenor.cos.messaging.event.Event;

/**
 * Creates Event for ACC_TYPE_ID(type in Account.java) column change in Account Table. 
 * @author Babaprakash D
 *
 */
public class AccountTypeUpdateEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;
    private String accountType;

    /**
     * Constructor of AccountTypeUpdateEvent.
     * @param accountId AccountId.
     * @param accountType AccountType.
     */
    public AccountTypeUpdateEvent(Long accountId, String accountType) {
        super(accountId, ACTION.TYPE_CHANGE, TYPE.ACCOUNT);
        this.accountType = accountType;
    }
    /**
     * @return the accountType
     */
    public String getAccountType() {
        return accountType;
    }
}
