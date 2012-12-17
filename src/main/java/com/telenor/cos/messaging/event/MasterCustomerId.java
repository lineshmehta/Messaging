package com.telenor.cos.messaging.event;

import java.io.Serializable;

/**
 * @author Babaprakash D
 * This class is shared between MasterCustomer and MasterStructure.
 *
 */
public class MasterCustomerId implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    private Long masterId;

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    @Override
    public String toString() {
        return "MasterCustomer [masterId=" + masterId + "]";
    }
}
