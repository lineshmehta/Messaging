package com.telenor.cos.messaging.event;


public class CustomerAddress extends Customer {

    /**
     * 
     */
    private static final long serialVersionUID = -2160873513232920739L;

    private String postcodeIdMain;
    private String postcodeNameMain;
    private String addressLineMain;
    private String addressCoName;
    private String addressStreetName;
    private String addressStreetNumber;

    /**
     * Constructor
     * @param customerId customerId.
     */
    public CustomerAddress(Long customerId) {
        super(customerId);
    }

    public void setPostcodeIdMain(String postcodeIdMain) {
        this.postcodeIdMain = postcodeIdMain;
    }

    public void setPostcodeNameMain(String postcodeNameMain) {
        this.postcodeNameMain = postcodeNameMain;
    }

    public void setAddressLineMain(String addressLineMain) {
        this.addressLineMain = addressLineMain;
    }

    public void setAddressCoName(String addressCoName) {
        this.addressCoName = addressCoName;
    }

    public void setAddressStreetName(String addressStreetName) {
        this.addressStreetName = addressStreetName;
    }

    public void setAddressStreetNumber(String addressStreetNumber) {
        this.addressStreetNumber = addressStreetNumber;
    }

    public String getPostcodeIdMain() {
        return postcodeIdMain;
    }

    public String getPostcodeNameMain() {
        return postcodeNameMain;
    }

    public String getAddressLineMain() {
        return addressLineMain;
    }

    public String getAddressCoName() {
        return addressCoName;
    }

    public String getAddressStreetName() {
        return addressStreetName;
    }

    public String getAddressStreetNumber() {
        return addressStreetNumber;
    }

    @Override
    public String toString() {
        return "CustomerAddress [postcodeIdMain=" + postcodeIdMain + ", postcodeNameMain=" + postcodeNameMain
                + ", addressLineMain=" + addressLineMain + ", addressCoName=" + addressCoName + ", addressStreetName="
                + addressStreetName + ", addressStreetNumber=" + addressStreetNumber + "]";
    }
    
    
}
