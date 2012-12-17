package com.telenor.cos.messaging.event;

public class CustomerName extends Customer {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;
    
    private Long masterCustomerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private Long custUnitNumber;
    
    /**
     * @param customerId customerId.
     */
    public CustomerName(Long customerId) {
        super(customerId);
    }

    public Long getMasterCustomerId() {
        return masterCustomerId;
    }

    public void setMasterCustomerId(Long masterCustomerId) {
        this.masterCustomerId = masterCustomerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getCustUnitNumber() {
        return custUnitNumber;
    }

    public void setCustUnitNumber(Long custUnitNumber) {
        this.custUnitNumber = custUnitNumber;
    }
}
