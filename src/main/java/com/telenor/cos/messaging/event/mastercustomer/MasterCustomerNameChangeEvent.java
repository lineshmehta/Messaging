package com.telenor.cos.messaging.event.mastercustomer;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.MasterCustomer;

/**
 * This class creates event for the changes to any of the
 * following columns in the Master Customer Table:
 * 1) CUST_FIRST_NAME
 * 2) CUST_MIDDLE_NAME
 * 3) CUST_LAST_NAME
 * primary key to reference these is: MASTER_ID
 * @author t798435
 */
public class MasterCustomerNameChangeEvent extends Event {

    private static final long serialVersionUID = -4327395910539199976L;
    
    private MasterCustomer masterCustomerName;

    /**
     * Creates MasterStructure NameChangeEvent.
     * @param masterId --> masterId.
     * @param name --> MasterCustomerName.
     */
    public MasterCustomerNameChangeEvent(Long masterId, MasterCustomer name) {
        super(masterId, ACTION.NAME_CHANGE, TYPE.MASTERCUSTOMER);
        this.masterCustomerName = name;
    }

    public MasterCustomer getMasterCustomerName() {
        return masterCustomerName;
    }
}
