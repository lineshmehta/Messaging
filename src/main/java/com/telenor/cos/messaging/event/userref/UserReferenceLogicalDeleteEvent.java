package com.telenor.cos.messaging.event.userref;

import com.telenor.cos.messaging.event.Event;

/**
 * Event for Update Messages in USER_REFERENCE(INFO_IS_DELETED) table.
 *
 * @author Babaprakash D
 *
 */
public class UserReferenceLogicalDeleteEvent extends Event {

    /**
     *
     */
    private static final long serialVersionUID = 2965882173858446870L;

    private String numberType;

    /**
     * Constructor of UserReferenceLogicalDeleteEvent
     * @param subscriptionId subscriptionId.
     * @param numberType numberType.
     */
    public UserReferenceLogicalDeleteEvent(Long subscriptionId,String numberType) {
        super(subscriptionId, ACTION.LOGICAL_DELETE, TYPE.USER_REFERENCE);
        this.numberType = numberType;
    }

    public String getNumberType() {
        return numberType;
    }
}
