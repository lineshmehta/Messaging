package com.telenor.cos.messaging.event;

import java.io.Serializable;

/**
 * Domain object for Account. Values are mapped from xml (ReplicationServer)
 * @author Eirik Bergande (Capgemini)
 * 
 */
public class Account implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7530332383315594735L;

    private Long accountId;//ACC_ID in Account Table.
    private String name;//ACC_NAME in Account Table(AccountReference).
    private String type;//ACC_TYPE_ID in Account Table.
    private String status;//ACC_STATUS_ID2 in Account Table.
    private String paymentStatus;//ACC_STATUS_ID in Account Table.
    private Long masterIdPayer;//MASTER_ID in Customer Table.
    private Long masterIdOwner;//MASTER_ID in Customer Table.
    private String invoiceAddress;//POSTCODE_ID_MAIN and POSTCODE_NAME_MAIN in Customer Table.
    private String invoiceFormat;//ACC_INV_MEDIUM in Account Table.
    private Long organizationNumberPayer;//CUST_UNIT_NUMBER in Customer Table
    private String custFirstNamePayer;//CUST_FIRST_NAME in Customer Table.
    private String custMiddleNamePayer;//CUST_MIDDLE_NAME in Customer Table
    private String custLastNamePayer;//CUST_LAST_NAME in Customer Table.
    private Long custIdResp;//CUST_ID_RESP in Account Table.
    private Long custIdPayer;//CUST_ID_PAYER in Account Table.

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getMasterIdPayer() {
        return masterIdPayer;
    }

    public void setMasterIdPayer(Long masterIdPayer) {
        this.masterIdPayer = masterIdPayer;
    }

    public Long getMasterIdOwner() {
        return masterIdOwner;
    }

    public void setMasterIdOwner(Long masterIdOwner) {
        this.masterIdOwner = masterIdOwner;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getInvoiceFormat() {
        return invoiceFormat;
    }

    public void setInvoiceFormat(String invoiceFormat) {
        this.invoiceFormat = invoiceFormat;
    }

    public Long getOrganizationNumberPayer() {
        return organizationNumberPayer;
    }

    public void setOrganizationNumberPayer(Long organizationNumberPayer) {
        this.organizationNumberPayer = organizationNumberPayer;
    }

    public String getCustFirstNamePayer() {
        return custFirstNamePayer;
    }

    public void setCustFirstNamePayer(String custFirstNamePayer) {
        this.custFirstNamePayer = custFirstNamePayer;
    }

    public String getCustMiddleNamePayer() {
        return custMiddleNamePayer;
    }

    public void setCustMiddleNamePayer(String custMiddleNamePayer) {
        this.custMiddleNamePayer = custMiddleNamePayer;
    }

    public String getCustLastNamePayer() {
        return custLastNamePayer;
    }

    public void setCustLastNamePayer(String custLastNamePayer) {
        this.custLastNamePayer = custLastNamePayer;
    }

    public Long getCustIdPayer() {
        return custIdPayer;
    }

    public void setCustIdPayer(Long custIdPayer) {
        this.custIdPayer = custIdPayer;
    }
    
    public Long getCustIdResp() {
        return custIdResp;
    }

    public void setCustIdResp(Long custIdResp) {
        this.custIdResp = custIdResp;
    }

    @Override
    public String toString() {
        return "Account [accountId=" + accountId + ", name=" + name + ", type="
                + type + ", status=" + status + ", paymentStatus="
                + paymentStatus + ", masterIdPayer=" + masterIdPayer
                + ", masterIdOwner=" + masterIdOwner + ", invoiceAddress="
                + invoiceAddress + ", invoiceFormat=" + invoiceFormat
                + ", organizationNumberPayer=" + organizationNumberPayer
                + ", custFirstNamePayer=" + custFirstNamePayer
                + ", custMiddleNamePayer=" + custMiddleNamePayer
                + ", custLastNamePayer=" + custLastNamePayer + ", custIdResp="
                + custIdResp + ", custIdPayer=" + custIdPayer + "]";
    }
}
