package com.telenor.cos.messaging.event.mastercustomer;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.MasterCustomer;

/**
 * New MasterCustomer Event.
 * @author Babaprakash D
 *
 */
public class MasterCustomerNewEvent extends Event {

    private static final long serialVersionUID = 714384149008397110L;
    
    private MasterCustomer masterCustomer;

    /**
     * New MasterCustomer Event Constructor.
     * @param masterCustomer masterCustomer object
     */
    public MasterCustomerNewEvent(MasterCustomer masterCustomer) {
        super(masterCustomer.getMasterId(), ACTION.CREATED, TYPE.MASTERCUSTOMER);
        this.masterCustomer = masterCustomer;
    }

    public MasterCustomer getMasterCustomer() {
        return masterCustomer;
    }
}
