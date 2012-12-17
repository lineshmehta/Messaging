package com.telenor.cos.messaging.event;

import java.io.Serializable;

public class Customer implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1L;
    private Long customerId;
    
    /**
     * @param customerId Customer Id.
     */
    public Customer(Long customerId) {
        this.customerId = customerId;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
