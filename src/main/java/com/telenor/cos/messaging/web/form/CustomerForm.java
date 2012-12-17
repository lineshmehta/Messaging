package com.telenor.cos.messaging.web.form;

import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class CustomerForm {
    
    private String addrCOName;
    private String addrLineMain;
    private String addrStreetName;
    private String custLastName;
    private String custMiddleName;
    private String custUnitNumber;
    private String custFirstName;
    private String postcodeNameMain;
    private String addrStreetNumber;
    private Integer custId;
    private Integer masterId;
    private Integer postcodeIdMain;
    
    private String oldAddrCOName;
    private String oldAddrLineMain;
    private String oldAddrStreetName;
    private String oldPostcodeNameMain;
    private String oldAddrStreetNumber;
    private Integer oldPostcodeIdMain;

    private boolean infoIsDeleted;
    
    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    
    public enum CustomerUpdateSubType {
        LOGICAL_DELETE,ADRESS_CHANGE,NAME_CHANGE; 

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    
    public String getAddrCOName() {
        return addrCOName;
    }

    public void setAddrCOName(String addrCOName) {
        this.addrCOName = StringEscapeUtils.escapeXml(addrCOName);
    }

    public String getAddrLineMain() {
        return addrLineMain;
    }

    public void setAddrLineMain(String addrLineMain) {
        this.addrLineMain = StringEscapeUtils.escapeXml(addrLineMain);
    }

    public String getAddrStreetName() {
        return addrStreetName;
    }

    public void setAddrStreetName(String addrStreetName) {
        this.addrStreetName = StringEscapeUtils.escapeXml(addrStreetName);
    }

    public String getAddrStreetNumber() {
        return addrStreetNumber;
    }

    public void setAddrStreetNumber(String addrStreetNumber) {
        this.addrStreetNumber = addrStreetNumber;
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public void setCustFirstName(String custFirstName) {
        this.custFirstName = StringEscapeUtils.escapeXml(custFirstName);
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getCustLastName() {
        return custLastName;
    }

    public void setCustLastName(String custLastName) {
        this.custLastName = StringEscapeUtils.escapeXml(custLastName);
    }

    public String getCustMiddleName() {
        return custMiddleName;
    }

    public void setCustMiddleName(String custMiddleName) {
        this.custMiddleName = StringEscapeUtils.escapeXml(custMiddleName);
    }

    public String getCustUnitNumber() {
        return custUnitNumber;
    }

    public void setCustUnitNumber(String custUnitNumber) {
        this.custUnitNumber = StringEscapeUtils.escapeXml(custUnitNumber);
    }

    public boolean isInfoIsDeleted() {
        return infoIsDeleted;
    }

    public void setInfoIsDeleted(boolean infoIsDeleted) {
        this.infoIsDeleted = infoIsDeleted;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public Integer getPostcodeIdMain() {
        return postcodeIdMain;
    }

    public void setPostcodeIdMain(Integer postcodeIdMain) {
        this.postcodeIdMain = postcodeIdMain;
    }

    public String getPostcodeNameMain() {
        return postcodeNameMain;
    }

    public void setPostcodeNameMain(String postcodeNameMain) {
        this.postcodeNameMain = StringEscapeUtils.escapeXml(postcodeNameMain);
    }
    
    public String getOldAddrCOName() {
        return oldAddrCOName;
    }

    public void setOldAddrCOName(String oldAddrCOName) {
        this.oldAddrCOName = oldAddrCOName;
    }

    public String getOldAddrLineMain() {
        return oldAddrLineMain;
    }

    public void setOldAddrLineMain(String oldAddrLineMain) {
        this.oldAddrLineMain = oldAddrLineMain;
    }

    public String getOldAddrStreetName() {
        return oldAddrStreetName;
    }

    public void setOldAddrStreetName(String oldAddrStreetName) {
        this.oldAddrStreetName = oldAddrStreetName;
    }

    public String getOldPostcodeNameMain() {
        return oldPostcodeNameMain;
    }

    public void setOldPostcodeNameMain(String oldPostcodeNameMain) {
        this.oldPostcodeNameMain = oldPostcodeNameMain;
    }

    public String getOldAddrStreetNumber() {
        return oldAddrStreetNumber;
    }

    public void setOldAddrStreetNumber(String oldAddrStreetNumber) {
        this.oldAddrStreetNumber = oldAddrStreetNumber;
    }

    public Integer getOldPostcodeIdMain() {
        return oldPostcodeIdMain;
    }

    public void setOldPostcodeIdMain(Integer oldPostcodeIdMain) {
        this.oldPostcodeIdMain = oldPostcodeIdMain;
    }

    @Override
    public String toString() {
        return "CustomerForm [addrCOName=" + addrCOName + ", addrLineMain=" + addrLineMain + ", addrStreetName="
                + addrStreetName + ", custLastName=" + custLastName + ", custMiddleName=" + custMiddleName
                + ", custUnitNumber=" + custUnitNumber + ", custFirstName=" + custFirstName + ", postcodeNameMain="
                + postcodeNameMain + ", addrStreetNumber=" + addrStreetNumber + ", custId=" + custId + ", masterId=" + masterId
                + ", postcodeIdMain=" + postcodeIdMain + ", oldAddrCOName=" + oldAddrCOName + ", oldAddrLineMain="
                + oldAddrLineMain + ", oldAddrStreetName=" + oldAddrStreetName + ", oldPostcodeNameMain=" + oldPostcodeNameMain
                + ", oldAddrStreetNumber=" + oldAddrStreetNumber + ", oldPostcodeIdMain=" + oldPostcodeIdMain
                + ", infoIsDeleted=" + infoIsDeleted + "]";
    }

    /**
     * creates an xml representation of this form
     * 
     * @param eventType
     *            insert, update or whatever
     * @return yes, it does sometimes
     */
    public String toNewEventXML(EventType eventType) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n"
                + "    <" + eventType.toString() + " schema=\"CUSTOMER\">\n" + "        <values>\n"
                + "            <cell name=\"CUST_ID\" type=\"NUMERIC\">" + custId + "</cell>\n"
                + "            <cell name=\"MASTER_ID\" type=\"INT\">" + masterId + "</cell>\n"
                + "            <cell name=\"CUST_FIRST_NAME\" type=\"VARCHAR\">" + custFirstName + "</cell>\n"
                + "            <cell name=\"CUST_MIDDLE_NAME\" type=\"VARCHAR\">" + custMiddleName + "</cell>\n"
                + "            <cell name=\"CUST_LAST_NAME\" type=\"VARCHAR\">" + custLastName + "</cell>\n"
                + "            <cell name=\"CUST_UNIT_NUMBER\" type=\"NUMERIC\">" + custUnitNumber + "</cell>\n"
                + "            <cell name=\"POSTCODE_ID_MAIN\" type=\"INT\">" + postcodeIdMain + "</cell>\n"
                + "            <cell name=\"POSTCODE_NAME_MAIN\" type=\"VARCHAR\">" + postcodeNameMain + "</cell>\n"
                + "            <cell name=\"ADDR_LINE_MAIN\" type=\"VARCHAR\">" + addrLineMain + "</cell>\n"
                + "            <cell name=\"ADDR_CO_NAME\" type=\"VARCHAR\">" + addrCOName + "</cell>\n"
                + "            <cell name=\"ADDR_STREET_NAME\" type=\"VARCHAR\">" + addrStreetName + "</cell>\n"
                + "            <cell name=\"ADDR_STREET_NUMBER\" type=\"VARCHAR\">" + addrStreetNumber + "</cell>\n"
                + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeleted ? "Y":"N")+ "</cell>\n"
                + "        </values>\n" + "    </" + eventType.toString() + ">\n" + "</tran>";
    }
    
    /**
     * Creates an XML representation of update Event.
     * 
     * @param eventType
     *            when event Type is Update
     * @param subEventType
     *            ee
     * @return XML representation.
     */
    public String toUpdateEventXML(EventType eventType, CustomerUpdateSubType subEventType) {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlToSend.append("<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <" + eventType.toString() + " schema=\"CUSTOMER\">\n");
        xmlToSend.append("        <values>\n");
        xmlToSend.append(createValues(subEventType).toString());
        xmlToSend.append("        </values>\n");
        xmlToSend.append("        <oldValues>\n");
        xmlToSend.append(createOldValues().toString());
        xmlToSend.append("        </oldValues>\n");
        xmlToSend.append("    </" + eventType.toString() + ">\n");
        xmlToSend.append("</tran>");
        return xmlToSend.toString();
    }
    
    /**
     * creates an xml representation of this form
     * 
     * @return yes, it does sometimes
     */
    public String createOldValues() {
        return    "            <cell name=\"CUST_ID\" type=\"NUMERIC\">" + custId + "</cell>\n"
                + "            <cell name=\"MASTER_ID\" type=\"INT\">" + masterId + "</cell>\n"
                + "            <cell name=\"CUST_FIRST_NAME\" type=\"VARCHAR\">" + custFirstName + "</cell>\n"
                + "            <cell name=\"CUST_MIDDLE_NAME\" type=\"VARCHAR\">" + custMiddleName + "</cell>\n"
                + "            <cell name=\"CUST_LAST_NAME\" type=\"VARCHAR\">" + custLastName + "</cell>\n"
                + "            <cell name=\"CUST_UNIT_NUMBER\" type=\"NUMERIC\">" + custUnitNumber + "</cell>\n"
                + "            <cell name=\"POSTCODE_ID_MAIN\" type=\"INT\">" + oldPostcodeIdMain + "</cell>\n"
                + "            <cell name=\"POSTCODE_NAME_MAIN\" type=\"VARCHAR\">" + oldPostcodeNameMain + "</cell>\n"
                + "            <cell name=\"ADDR_LINE_MAIN\" type=\"VARCHAR\">" + oldAddrLineMain + "</cell>\n"
                + "            <cell name=\"ADDR_CO_NAME\" type=\"VARCHAR\">" + oldAddrCOName + "</cell>\n"
                + "            <cell name=\"ADDR_STREET_NAME\" type=\"VARCHAR\">" + oldAddrStreetName + "</cell>\n"
                + "            <cell name=\"ADDR_STREET_NUMBER\" type=\"VARCHAR\">" + oldAddrStreetNumber + "</cell>\n";
    }
    
    private StringBuffer createValues(CustomerUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();

        if (subEventType.toString().equals("logical_delete")) {
            if (infoIsDeleted) {
                values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "Y" + "</cell>\n");
            }else{
                values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "N" + "</cell>\n");
            }
        }
        adressChange(subEventType, values);
        nameChange(subEventType, values);

        return values;
    }

    private void nameChange(CustomerUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("name_change")) {
            if(masterId!=null){
                values.append("             <cell name=\"MASTER_ID\" type=\"INT\">" + masterId + "</cell>\n");
            }
            if(StringUtils.isNotEmpty(custFirstName)){
                values.append("             <cell name=\"CUST_FIRST_NAME\" type=\"VARCHAR\">" + custFirstName + "</cell>\n");
            }
            if(StringUtils.isNotEmpty(custMiddleName)){
                values.append("             <cell name=\"CUST_MIDDLE_NAME\" type=\"VARCHAR\">" + custMiddleName + "</cell>\n");
            }
            if(StringUtils.isNotEmpty(custLastName)){
                values.append("             <cell name=\"CUST_LAST_NAME\" type=\"VARCHAR\">" + custLastName + "</cell>\n");
            }
            if(StringUtils.isNotEmpty(custUnitNumber)){
                values.append("             <cell name=\"CUST_UNIT_NUMBER\" type=\"NUMERIC\">" + custUnitNumber + "</cell>\n");
            }
        }
    }

    private void adressChange(CustomerUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("adress_change")) {
            if(postcodeIdMain!=null){
                values.append("             <cell name=\"POSTCODE_ID_MAIN\" type=\"INT\">" + postcodeIdMain + "</cell>\n");
            }
            if(StringUtils.isNotEmpty(postcodeNameMain)){
                values.append("             <cell name=\"POSTCODE_NAME_MAIN\" type=\"VARCHAR\">" + postcodeNameMain + "</cell>\n");
            }
            if(StringUtils.isNotEmpty(addrLineMain)){
                values.append("             <cell name=\"ADDR_LINE_MAIN\" type=\"VARCHAR\">" + addrLineMain + "</cell>\n");
            }
            if(StringUtils.isNotEmpty(addrCOName)){
                values.append("             <cell name=\"ADDR_CO_NAME\" type=\"VARCHAR\">" + addrCOName + "</cell>\n");
            }
            if(StringUtils.isNotEmpty(addrStreetName)){
                values.append("             <cell name=\"ADDR_STREET_NAME\" type=\"VARCHAR\">" + addrStreetName + "</cell>\n");
            }
            if(addrStreetNumber!=null){
                values.append("             <cell name=\"ADDR_STREET_NUMBER\" type=\"VARCHAR\">" + addrStreetNumber + "</cell>\n");
            }
        }
    }
}