package com.telenor.cos.messaging.jdbm;

import java.io.Serializable;

/**
 * Domain object for Customers, a subset of the CUSTOMER table in FKM.
 * 
 * @author Eirik Bergande (Capgemini)
 * 
 */
public class CachableCustomer implements Serializable, Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = -5987770678323657481L;

    private Long customerId;
    private Long masterCustomerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private Long custUnitNumber;
    private String postcodeIdMain;
    private String postcodeNameMain;
    private String addressLineMain;
    private String addressCOName;
    private String addressStreetName;
    private String addressStreetNumber;

    /**
     * Constructor for a Customer domain object
     * 
     * @param customerID
     *            The customerId
     */
    public CachableCustomer(Long customerID) {
        this.customerId = customerID;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public Long getMasterCustomerId() {
        return masterCustomerId;
    }

    public void setMasterCustomerId(Long masterCustomerId) {
        this.masterCustomerId = masterCustomerId;
    }

    public String getPostcodeIdMain() {
        return postcodeIdMain;
    }

    public void setPostcodeIdMain(String postcodeIdMain) {
        this.postcodeIdMain = postcodeIdMain;
    }

    public String getPostcodeNameMain() {
        return postcodeNameMain;
    }

    public void setPostcodeNameMain(String postcodeNameMain) {
        this.postcodeNameMain = postcodeNameMain;
    }

    public String getAddressLineMain() {
        return addressLineMain;
    }

    public void setAddressLineMain(String addressLineMain) {
        this.addressLineMain = addressLineMain;
    }

    public String getAddressCOName() {
        return addressCOName;
    }

    public void setAddressCOName(String addressCOName) {
        this.addressCOName = addressCOName;
    }

    public String getAddressStreetName() {
        return addressStreetName;
    }

    public void setAddressStreetName(String addressStreetName) {
        this.addressStreetName = addressStreetName;
    }

    public String getAddressStreetNumber() {
        return addressStreetNumber;
    }

    public void setAddressStreetNumber(String addressStreetNumber) {
        this.addressStreetNumber = addressStreetNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
        result = prime * result + ((masterCustomerId == null) ? 0 : masterCustomerId.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
        result = prime * result + ((postcodeIdMain == null) ? 0 : postcodeIdMain.hashCode());
        result = prime * result + ((postcodeNameMain == null) ? 0 : postcodeNameMain.hashCode());
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
        CachableCustomer other = (CachableCustomer) obj;
        if (customerId == null) {
            if (other.customerId != null) {
                return false;
            }
        } else if (!customerId.equals(other.customerId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CachableCustomer [customerId=" + customerId + ", masterCustomerId=" + masterCustomerId + ", firstName="
                + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", custUnitNumber=" + custUnitNumber
                + ", postcodeIdMain=" + postcodeIdMain + ", postcodeNameMain=" + postcodeNameMain + ", addressLineMain="
                + addressLineMain + ", addressCOName=" + addressCOName + ", addressStreetName=" + addressStreetName
                + ", addressStreetNumber=" + addressStreetNumber + "]";
    }

    /**
     * Clones the object
     * @return CachableCustomer a clone
     */
    protected CachableCustomer clone() {
        try {
            return (CachableCustomer) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
