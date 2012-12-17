package com.telenor.cos.messaging.event;

/**
 *   
 * @author Babaprakash D
 *
 */
public class MasterCustomer extends MasterCustomerId {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    private String firstName;

    private String middleName;

    private String lastName;

    private Long organizationNumber;

    private Long kurtId;

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

    public Long getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(Long organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public Long getKurtId() {
        return kurtId;
    }

    public void setKurtId(Long kurtId) {
        this.kurtId = kurtId;
    }

    @Override
    public String toString() {
        return "MasterCustomer [firstName=" + firstName + ", middleName="
                + middleName + ", lastName=" + lastName
                + ", organizationNumber=" + organizationNumber + ", kurtId="
                + kurtId + "]";
    }
}
