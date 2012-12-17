package com.telenor.cos.messaging.event.userref;

import com.telenor.cos.messaging.event.Event;

/**
 * Event for Update Messages in USER_REFERENCE(USER_REF_DESCR) table.
 *
 * @author Babaprakash D
 *
 */
public class UserReferenceDescriptionUpdateEvent extends Event {

    /**
     *
     */
    private static final long serialVersionUID = -6670785282691609175L;

    private String userRefDescr;
    private String numberType;

    /**
     * Constructor of UserReferenceDescriptionUpdateEvent.
     * @param subscriptionId the subscriptionId
     * @param userReferenceDescr userReferenceDescr.
     * @param numberType numberType.
     */
    public UserReferenceDescriptionUpdateEvent(Long subscriptionId,String userReferenceDescr,String numberType) {
        super(subscriptionId, ACTION.USERREF_DESC_CHG, TYPE.USER_REFERENCE);
        this.userRefDescr = userReferenceDescr;
        this.numberType = numberType;
    }

    public String getUserRefDescr() {
        return userRefDescr;
    }

    public String getNumberType() {
        return numberType;
    }
}