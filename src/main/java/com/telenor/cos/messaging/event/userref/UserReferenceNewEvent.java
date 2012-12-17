package com.telenor.cos.messaging.event.userref;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.UserReference;

/**
 * Event for Insert Messages in USER_REFERENCE table.
 * @author Babaprakash D
 *
 */
public class UserReferenceNewEvent extends Event {

    /**
     *
     */
    private static final long serialVersionUID = -3860400974868947886L;

    private UserReference userReference;

    /**
     * Creates Event for New UserReference.
     * @param subscriptionId subscriptionId.
     * @param userReference userReference.
     */
    public UserReferenceNewEvent(Long subscriptionId,UserReference userReference) {
        super(subscriptionId, ACTION.CREATED, TYPE.USER_REFERENCE);
        this.userReference = userReference;
    }

    public UserReference getUserReference() {
        return userReference;
    }
}
