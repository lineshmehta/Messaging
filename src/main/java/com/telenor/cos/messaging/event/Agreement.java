package com.telenor.cos.messaging.event;

import java.io.Serializable;

/**
 * Domain class for Agreement
 */
public class Agreement implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6721581411363129882L;
    private Long agreementId;
    private Long masterCustomerId;

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public Long getMasterCustomerId() {
        return masterCustomerId;
    }

    public void setMasterCustomerId(Long masterCustomerId) {
        this.masterCustomerId = masterCustomerId;
    }

    @Override
    public String toString() {
        return "Agreement [agreementId=" + agreementId + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((agreementId == null) ? 0 : agreementId.hashCode());
        result = prime * result + ((masterCustomerId == null) ? 0 : masterCustomerId.hashCode());
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
        Agreement other = (Agreement) obj;
        if (agreementId == null) {
            if (other.agreementId != null) {
                return false;
            }
        } else if (!agreementId.equals(other.agreementId)) {
            return false;
        }
        if (masterCustomerId == null) {
            if (other.masterCustomerId != null) {
                return false;
            }
        } else if (!masterCustomerId.equals(other.masterCustomerId)) {
            return false;
        }
        return true;
    }

}
