package com.telenor.cos.messaging.event;

import java.io.Serializable;

public class AgreementOwner implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2316233884292903870L;

    private Long masterId;
    private Long agreementId;

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((agreementId == null) ? 0 : agreementId.hashCode());
        result = prime * result + ((masterId == null) ? 0 : masterId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AgreementOwner other = (AgreementOwner) obj;
        if (agreementId == null) {
            if (other.agreementId != null) {
                return false;
            }
        } else if (!agreementId.equals(other.agreementId)) {
            return false;
        }
        if (masterId == null) {
            if (other.masterId != null) {
                return false;
            }
        } else if (!masterId.equals(other.masterId)) {
            return false;
        }
        return true;
    }

}
