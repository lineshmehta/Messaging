package com.telenor.cos.messaging.event.subscription;

import java.util.Date;

import com.telenor.cos.messaging.event.Event;

public class SubscriptionExpiredEvent extends Event {
    
    private Date validToDate;
    private String subscrType;
    private String msisdn;

    private static final long serialVersionUID = -1L;

    /**
     * Constructor.
     * 
     * @param subscriptionId the subscription id
     * @param validToDate the valid to date
     */
    public SubscriptionExpiredEvent(Long subscriptionId, Date validToDate) {
        super(subscriptionId, ACTION.EXPIRED, TYPE.SUBSCRIPTION);
        this.validToDate = validToDate != null ? (Date) validToDate.clone() : null;
    }

    public Date getValidToDate() {
        return validToDate != null ? (Date) validToDate.clone() : null;
    }
    
    public String getSusbcrType() {
        return subscrType;
    }
    
    public void setSubscrType(String subscrType) {
        this.subscrType = subscrType;
    }
    
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}
