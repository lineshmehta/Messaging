package com.telenor.cos.messaging.event;

import java.io.Serializable;

public class SubscriptionEquipment implements Serializable {


    private static final long serialVersionUID = -1L;
    private Long msisdn;
    private String imsi;
    private String subscriptionType;

    /**
     * @param msisdn the msisdn
     * @param imsi the imsi number
     * @param subscriptionType the subscriptionType
     */
    public SubscriptionEquipment(Long msisdn, String imsi, String subscriptionType) {
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.subscriptionType = subscriptionType;
    }

    /**
     * @param imsi the imsi number
     */
    public SubscriptionEquipment(String imsi) {
        this.imsi = imsi;
    }

    public Long getMsisdn() {
        return msisdn;
    }

    public String getImsi() {
        return imsi;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }
}
