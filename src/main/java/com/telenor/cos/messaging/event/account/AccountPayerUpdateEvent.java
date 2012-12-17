package com.telenor.cos.messaging.event.account;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;


/**
 * Creates Event for CUST_ID_PAYER(custIdPayer field in Account.java)
 * column change in Account Table.
 * @author Babaprakash D
 *
 */
public class AccountPayerUpdateEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    private CustomerName newPayerCustomerName;
    private CustomerAddress newPayerCustomerAddress;

    /**
     * Constructor of AccountPayerUpdateEvent.
     * @param accountId AccountId.
     * @param newPayerCustomerName New PayerCustomer Name.
     * @param newPayerCustomerAddress New PayerCustomer Address.
     */
    public AccountPayerUpdateEvent(Long accountId, CustomerName newPayerCustomerName, CustomerAddress newPayerCustomerAddress) {
        super(accountId, ACTION.PAYER_CHANGE, TYPE.ACCOUNT);
        this.newPayerCustomerName = newPayerCustomerName;
        this.newPayerCustomerAddress = newPayerCustomerAddress;
    }

    public CustomerName getNewPayerCustomerName() {
        return newPayerCustomerName;
    }

    public CustomerAddress getNewPayerCustomerAddress() {
        return newPayerCustomerAddress;
    }
}
