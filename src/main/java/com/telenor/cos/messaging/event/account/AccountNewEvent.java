package com.telenor.cos.messaging.event.account;

import com.telenor.cos.messaging.event.Account;
import com.telenor.cos.messaging.event.Event;

/**
 * Creates Event for New Account.
 *
 */
public class AccountNewEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -4229571967075925808L;

    private Account account;

    /**
     * Constructor of AccountNewEvent
     * @param account the account
     */
    public AccountNewEvent(Account account) {
        super(account.getAccountId(), ACTION.CREATED, TYPE.ACCOUNT);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
