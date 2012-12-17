package com.telenor.cos.messaging.event.usermapping;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.TnuIdUserMapping;

/**
 * tnuId UserMapping new event. 
 * @author t808074
 *
 */
public class TnuIdUserMappingNewEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    private TnuIdUserMapping userMapping;

    /**
     * TnuIdUserMapping New Event.
     * @param userMapping TnuId UserMapping New Event.
     */
    public TnuIdUserMappingNewEvent(TnuIdUserMapping userMapping) {
        super(null,ACTION.CREATED,TYPE.USERMAPPING);
        this.userMapping = userMapping;
    }

    public TnuIdUserMapping getUserMapping() {
        return userMapping;
    }
}
