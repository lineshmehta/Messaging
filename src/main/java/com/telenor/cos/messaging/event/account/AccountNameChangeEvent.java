package com.telenor.cos.messaging.event.account;

import com.telenor.cos.messaging.event.Event;

/**
 * Creates Event for ACC_NAME column change in Account Table.
 * @author Babaprakash D
 *
 */
public class AccountNameChangeEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -4540041670476311072L;

    private String name;

    /**
     * Creates Account NameChangeEvent.
     * @param accountId accountId.
     * @param name account Name.
     */
    public AccountNameChangeEvent(Long accountId,String name) {
        super(accountId, ACTION.NAME_CHANGE, TYPE.ACCOUNT);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
