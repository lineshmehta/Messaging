package com.telenor.cos.messaging.web.form;

import java.text.SimpleDateFormat;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

public class MasterCustomerForm {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");

    private Long masterId;
    private Long kurtId;
    
    private Integer custUnitNumber;

    private String custLastName;
    private String custFirstName;
    private String custMiddleName;
    private String oldFirstName;
    private String oldMiddleName;
    private String oldLastName;
    
    private boolean infoIsDeleted;

    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum MasterCustomerUpdateSubType {
        LOGICAL_DELETE, NAME_CHANGE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    
    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public Integer getCustUnitNumber() {
        return custUnitNumber;
    }

    public void setCustUnitNumber(Integer custUnitNumber) {
        this.custUnitNumber = custUnitNumber;
    }

    public String getCustLastName() {
        return custLastName;
    }

    public void setCustLastName(String custLastName) {
        this.custLastName = StringEscapeUtils.escapeXml(custLastName);
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public void setCustFirstName(String custFirstName) {
        this.custFirstName = StringEscapeUtils.escapeXml(custFirstName);
    }

    public String getCustMiddleName() {
        return custMiddleName;
    }

    public void setCustMiddleName(String custMiddleName) {
        this.custMiddleName = StringEscapeUtils.escapeXml(custMiddleName);
    }

    public String getOldFirstName() {
        return oldFirstName;
    }

    public void setOldFirstName(String oldFirstName) {
        this.oldFirstName = StringEscapeUtils.escapeXml(oldFirstName);
    }

    public String getOldMiddleName() {
        return oldMiddleName;
    }

    public void setOldMiddleName(String oldMiddleName) {
        this.oldMiddleName = StringEscapeUtils.escapeXml(oldMiddleName);
    }

    public String getOldLastName() {
        return oldLastName;
    }

    public void setOldLastName(String oldLastName) {
        this.oldLastName = StringEscapeUtils.escapeXml(oldLastName);
    }

    public boolean isInfoIsDeleted() {
        return infoIsDeleted;
    }

    public void setInfoIsDeleted(boolean infoIsDeleted) {
        this.infoIsDeleted = infoIsDeleted;
    }
    
    public Long getKurtId() {
        return kurtId;
    }

    public void setKurtId(Long kurtId) {
        this.kurtId = kurtId;
    }
    
    @Override
    public String toString() {
        return "MasterCustomerForm [simpleDateFormat=" + simpleDateFormat
                + ", masterId=" + masterId + ", custUnitNumber="
                + custUnitNumber + ", custLastName=" + custLastName
                + ", custFirstName=" + custFirstName + ", custMiddleName="
                + custMiddleName + ", oldFirstName=" + oldFirstName
                + ", oldMiddleName=" + oldMiddleName + ", oldLastName="
                + oldLastName + ", infoIsDeleted=" + infoIsDeleted + "]";
    }

    /**
     * creates an xml representation of this form
     * 
     * @param eventType insert.
     * @return New Event Xml.
     */
    public String toNewEventXML(EventType eventType) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n"
                + "    <" + eventType.toString() + " schema=\"MASTER_CUSTOMER\">\n" + "        <values>\n"
                + "            <cell name=\"MASTER_ID\" type=\"NUMERIC\">" + masterId + "</cell>\n"
                + "            <cell name=\"KURT_ID\" type=\"INT\">" + kurtId + "</cell>\n"
                + "            <cell name=\"CUST_UNIT_NUMBER\" type=\"INT\">" + custUnitNumber + "</cell>\n"
                + "            <cell name=\"CUST_LAST_NAME\" type=\"VARCHAR\">" + custLastName + "</cell>\n"
                + "            <cell name=\"CUST_FIRST_NAME\" type=\"VARCHAR\">" + custFirstName + "</cell>\n"
                + "            <cell name=\"CUST_MIDDLE_NAME\" type=\"VARCHAR\">" + custMiddleName + "</cell>\n"
                + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeleted ?"Y":"N") + "</cell>\n"
                + "        </values>\n" + "    </" + eventType.toString() + ">\n" + "</tran>";
    }

    /**
     * Creates an XML representation of update Event.
     *
     * @param eventType
     *            when event Type is Update
     * @param subEventType
     *            what kind of update event
     * @return XML representation.
     */
    public String toUpdateEventXML(EventType eventType, MasterCustomerUpdateSubType subEventType) {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlToSend.append("<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <" + eventType.toString() + " schema=\"MASTER_CUSTOMER\">\n");
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

    private StringBuffer createValues(MasterCustomerUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();

        if (subEventType.toString().equals("logical_delete")) {
            if (infoIsDeleted) {
                values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "Y" + "</cell>\n");
            }else {
                values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "N" + "</cell>\n");
            }
        }
        
        if(subEventType.toString().equals("name_change")) {
            values.append("            <cell name=\"CUST_LAST_NAME\" type=\"VARCHAR\">" + custLastName + "</cell>\n");
            values.append("            <cell name=\"CUST_FIRST_NAME\" type=\"VARCHAR\">" + custFirstName + "</cell>\n");
            values.append("            <cell name=\"CUST_MIDDLE_NAME\" type=\"VARCHAR\">" + custMiddleName + "</cell>\n");
        }
        return values;
    }

    private StringBuffer createOldValues() {
        StringBuffer values = new StringBuffer();
        values.append("            <cell name=\"MASTER_ID\" type=\"NUMERIC\">" + masterId + "</cell>\n");
        values.append("            <cell name=\"CUST_UNIT_NUMBER\" type=\"INT\">" + custUnitNumber + "</cell>\n");
        values.append("            <cell name=\"CUST_LAST_NAME\" type=\"VARCHAR\">" + oldLastName + "</cell>\n");
        values.append("            <cell name=\"CUST_FIRST_NAME\" type=\"VARCHAR\">" + oldFirstName + "</cell>\n");
        values.append("            <cell name=\"CUST_MIDDLE_NAME\" type=\"VARCHAR\">" + oldMiddleName + "</cell>\n");
        return values;
    }
}