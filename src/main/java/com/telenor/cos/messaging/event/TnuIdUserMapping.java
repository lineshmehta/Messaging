package com.telenor.cos.messaging.event;

import java.io.Serializable;

/**
 * Domain Object for TnuUserId Mapping.
 * @author Babaprakash D
 *
 */
public class TnuIdUserMapping implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    private String telenorUserId;

    private Integer applicationId;

    private String cosSecurityUserId;

    public String getTelenorUserId() {
        return telenorUserId;
    }

    public void setTelenorUserId(String telenorUserId) {
        this.telenorUserId = telenorUserId;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getCosSecurityUserId() {
        return cosSecurityUserId;
    }

    public void setCosSecurityUserId(String cosSecurityUserId) {
        this.cosSecurityUserId = cosSecurityUserId;
    }

    @Override
    public String toString() {
        return "TnuIdUserMapping [telenorUserId=" + telenorUserId
                + ", applicationId=" + applicationId + ", cosSecurityUserId="
                + cosSecurityUserId + "]";
    }
}
