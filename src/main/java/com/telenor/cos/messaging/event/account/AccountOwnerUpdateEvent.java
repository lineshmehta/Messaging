package com.telenor.cos.messaging.event.account;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;

/**
 * Creates Event for CUST_ID_OWNER(custIdOwner field in Account.java) column change in Account Table.
 * 
 * @author Sanjin Cevro (Capgemini)
 * 
 */
@SuppressWarnings("serial")
public class AccountOwnerUpdateEvent extends Event {

    private CustomerName newOwnerCustomerName;
    private CustomerAddress newOwnerCustomerAddress;

    /**
     * Constructor of AccountPayerUpdateEvent.
     * 
     * @param accountId
     *            AccountId.
     * @param newOwnerCustomerName
     *            New OwnerCustomer Name.
     * @param newOwnerCustomerAddress
     *            New OwnerCustomer Address.
     */
    public AccountOwnerUpdateEvent(Long accountId, CustomerName newOwnerCustomerName, CustomerAddress newOwnerCustomerAddress) {
        super(accountId, ACTION.OWNER_CHANGE, TYPE.ACCOUNT);
        this.newOwnerCustomerName = newOwnerCustomerName;
        this.newOwnerCustomerAddress = newOwnerCustomerAddress;
    }

    public CustomerName getNewOwnerCustomerName() {
        return newOwnerCustomerName;
    }

    public CustomerAddress getNewOwnerCustomerAddress() {
        return newOwnerCustomerAddress;
    }
}
