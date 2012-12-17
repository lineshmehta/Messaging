package com.telenor.cos.messaging.event;

import java.io.Serializable;

public class AgreementMember implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long agreementMemberId;
    private Long agreementId;
    private Long masterId;

    public Long getAgreementMemberId() {
        return agreementMemberId;
    }

    public void setAgreementMemberId(Long agreementMemberId) {
        this.agreementMemberId = agreementMemberId;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    @Override
    public String toString() {
        return "AgreementMember [agreementMemberId=" + agreementMemberId
                + ", agreementId=" + agreementId + ", masterId=" + masterId
                + "]";
    }

}
