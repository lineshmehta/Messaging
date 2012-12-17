package com.telenor.cos.messaging.event.usermapping;

import com.telenor.cos.messaging.event.Event;

public class TnuidUserMappingDeleteEvent extends Event {

    private static final long serialVersionUID = 5684542710790643293L;

    private String telenorUserId;
    private Integer applicationId;

    /**
     * Constructor
     * 
     * @param telenorUserID
     *            the telenorUserId
     * @param applicationId
     *            the applicationId
     */
    public TnuidUserMappingDeleteEvent(String telenorUserID, Integer applicationId) {
        super(null, ACTION.DELETE, TYPE.USERMAPPING);
        this.telenorUserId = telenorUserID;
        this.applicationId = applicationId;
    }

    public String getTelenorUserId() {
        return telenorUserId;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

}
