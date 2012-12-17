package com.telenor.cos.messaging.event.masterstructure;

import com.telenor.cos.messaging.event.Event;

/**
 * @author t798435
 */
public class MasterStructureDeleteEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = 3225457175767681591L;

    /**
     * Constructor
     *
     * @param mastercustomerId id
     */
    public MasterStructureDeleteEvent(Long mastercustomerId) {
        super(mastercustomerId, ACTION.DELETE, TYPE.MASTERSTRUCTURE);
    }
}
