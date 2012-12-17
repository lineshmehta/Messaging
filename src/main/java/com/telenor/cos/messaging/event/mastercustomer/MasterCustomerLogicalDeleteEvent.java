package com.telenor.cos.messaging.event.mastercustomer;

import com.telenor.cos.messaging.event.Event;

public class MasterCustomerLogicalDeleteEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -5918695562378634590L;

    /**
     * Constructor
     *
     * @param mastercustomerId id
     */
    public MasterCustomerLogicalDeleteEvent(Long mastercustomerId) {
        super(mastercustomerId, ACTION.LOGICAL_DELETE, TYPE.MASTERCUSTOMER);
    }
}
