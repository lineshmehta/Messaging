package com.telenor.cos.messaging.event;

import java.io.Serializable;
import java.util.Date;

/**
 * data
 */
public class Subscription implements Serializable {
    private Long accountId;
    private Long msisdn;
    private String subscriptionType;
    private Long msisdnTvilling;
    private Long msisdnDatakort;
    private Long msisdnDatakort2;
    private Long userCustomerId;
    private Date validFromDate;
    private Date validToDate;
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer shortNumber;
    private Long contractId;
    private String userReference;
    private String userReferenceDescription;
    private String invoiceReference;
    private Boolean isSecretNumber;
    private Long ownerSubscriptionId;
    private String statusId;

    private static final long serialVersionUID = -1L;


    public Long getAccountId() {
        return accountId;
    }


    public Long getMsisdn() {
        return msisdn;
    }


    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Integer getShortNumber() {
        return shortNumber;
    }

    public Long getMsisdnTvilling() {
        return msisdnTvilling;
    }


    public Long getMsisdnDatakort() {
        return msisdnDatakort;
    }

    public Long getMsisdnDatakort2() {
        return msisdnDatakort2;
    }

    public Long getUserCustomerId() {
        return userCustomerId;
    }


    public Date getValidFromDate() {
        return validFromDate != null ? (Date) validFromDate.clone() : null;
    }


    public Date getValidToDate() {
        return validToDate != null ? (Date) validToDate.clone() : null;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getMiddleName() {
        return middleName;
    }


    public String getLastName() {
        return lastName;
    }


    public Long getContractId() {
        return contractId;
    }


    public String getUserReference() {
        return userReference;
    }


    public String getUserReferenceDescription() {
        return userReferenceDescription;
    }


    public String getInvoiceReference() {
        return invoiceReference;
    }


    public Boolean getIsSecretNumber() {
        return isSecretNumber;
    }

    public Long getOwnerSubscriptionId() {
        return ownerSubscriptionId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }


    public void setMsisdn(Long msisdn) {
        this.msisdn = msisdn;
    }

    public void setShortNumber(Integer shortNumber) {
        this.shortNumber = shortNumber;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public void setMsisdnTvilling(Long msisdnTvilling) {
        this.msisdnTvilling = msisdnTvilling;
    }


    public void setMsisdnDatakort(Long msisdnDatakort) {
        this.msisdnDatakort = msisdnDatakort;
    }


    public void setMsisdnDatakort2(Long msisdnDatakort2) {
        this.msisdnDatakort2 = msisdnDatakort2;
    }


    public void setUserCustomerId(Long userCustomerId) {
        this.userCustomerId = userCustomerId;
    }


    public void setValidFromDate(Date validFromDate) {
        this.validFromDate = validFromDate != null ? (Date) validFromDate.clone() : null;
    }


    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate != null ? (Date) validToDate.clone() : null;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }


    public void setUserReference(String userReference) {
        this.userReference = userReference;
    }


    public void setUserReferenceDescription(String userReferenceDescription) {
        this.userReferenceDescription = userReferenceDescription;
    }


    public void setInvoiceReference(String invoiceReference) {
        this.invoiceReference = invoiceReference;
    }


    public void setIsSecretNumber(Boolean isSecretNumber) {
        this.isSecretNumber = isSecretNumber;
    }

    public void setOwnerSubscriptionId(Long ownerSubscriptionId) {
        this.ownerSubscriptionId = ownerSubscriptionId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }


    @Override
    public String toString() {
        return "Subscription [accountId=" + accountId + ", msisdn=" + msisdn
                + ", subscriptionType=" + subscriptionType
                + ", msisdnTvilling=" + msisdnTvilling + ", msisdnDatakort="
                + msisdnDatakort + ", msisdnDatakort2=" + msisdnDatakort2
                + ", userCustomerId=" + userCustomerId + ", validFromDate="
                + validFromDate + ", validToDate=" + validToDate
                + ", firstName=" + firstName + ", middleName=" + middleName
                + ", lastName=" + lastName + ", shortNumber=" + shortNumber
                + ", contractId=" + contractId + ", userReference="
                + userReference + ", userReferenceDescription="
                + userReferenceDescription + ", invoiceReference="
                + invoiceReference + ", isSecretNumber=" + isSecretNumber
                + ", ownerSubscriptionId=" + ownerSubscriptionId
                + ", statusId=" + statusId + "]";
    }

}
