package com.telenor.cos.messaging.web.form;

import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Form for UserReference Fields
 * @author Babaprakash D
 *
 */
public class UserReferenceForm {

    private Long subscriptionId;
    private Long directoryNumberId;
    private Long contractId;
    private String numberType;
    private String userRefDescr;
    private boolean infoIsDeleted;
    private String eInvoiceRef;

    private String oldUserRefDescr;
    private String oldEInvoiceRef;
    private boolean oldInfoIsDeleted;

    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum UserReferenceUpdateSubType {
        LOGICAL_DELETE,CHANGE_NUMBER_TYPE,CHANGE_USERREF_DESCR,CHANGE_EINVOICE_REF;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }
    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
    public String getNumberType() {
        return numberType;
    }
    public void setNumberType(String numberType) {
        this.numberType = StringEscapeUtils.escapeXml(numberType);
    }
    public String getUserRefDescr() {
        return userRefDescr;
    }
    public void setUserRefDescr(String userRefDescr) {
        this.userRefDescr = StringEscapeUtils.escapeXml(userRefDescr);
    }
    public boolean isInfoIsDeleted() {
        return infoIsDeleted;
    }
    public void setInfoIsDeleted(boolean infoIsDeleted) {
        this.infoIsDeleted = infoIsDeleted;
    }

    /**
     * @return eInvoiceRef
     */
    public String geteInvoiceRef() {
        return eInvoiceRef;
    }

    /**
     * @param eInvoiceRef eInvoiceRef.
     */
    public void seteInvoiceRef(String eInvoiceRef) {
        this.eInvoiceRef = StringEscapeUtils.escapeXml(eInvoiceRef);
    }

    public String getOldUserRefDescr() {
        return oldUserRefDescr;
    }
    public void setOldUserRefDescr(String oldUserRefDescr) {
        this.oldUserRefDescr = StringEscapeUtils.escapeXml(oldUserRefDescr);
    }
    public String getOldEInvoiceRef() {
        return oldEInvoiceRef;
    }
    public void setOldEInvoiceRef(String oldEInvoiceRef) {
        this.oldEInvoiceRef = StringEscapeUtils.escapeXml(oldEInvoiceRef);
    }
    public boolean isOldInfoIsDeleted() {
        return oldInfoIsDeleted;
    }
    public void setOldInfoIsDeleted(boolean oldInfoIsDeleted) {
        this.oldInfoIsDeleted = oldInfoIsDeleted;
    }
    
    public Long getDirectoryNumberId() {
        return directoryNumberId;
    }
    public void setDirectoryNumberId(Long directoryNumberId) {
        this.directoryNumberId = directoryNumberId;
    }
    public Long getContractId() {
        return contractId;
    }
    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Override
    public String toString() {
        return "UserReferenceForm [subscriptionId=" + subscriptionId + ", numberType="
                + numberType + ", userRefDescr=" + userRefDescr
                + ", infoIsDeleted=" + infoIsDeleted + ", eInvoiceRef="
                + eInvoiceRef + ", oldUserRefDescr=" + oldUserRefDescr + ", oldEInvoiceRef="
                + oldEInvoiceRef + ", oldInfoIsDeleted=" + oldInfoIsDeleted
                + "]";
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
                + "    <" + eventType.toString() + " schema=\"USER_REFERENCE\">\n" + "        <values>\n"
                + "            <cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + subscriptionId + "</cell>\n"
                + "            <cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">" + directoryNumberId + "</cell>\n"
                + "            <cell name=\"CONTRACT_ID\" type=\"INT\">" + contractId + "</cell>\n"
                + "            <cell name=\"NUMBER_TYPE\" type=\"CHAR\">" + numberType + "</cell>\n"
                + "            <cell name=\"USER_REF_DESCR\" type=\"VARCHAR\">" + userRefDescr + "</cell>\n"
                + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeleted ?"Y":"N") + "</cell>\n"
                + "            <cell name=\"EINVOICE_REF\" type=\"VARCHAR\">" + eInvoiceRef + "</cell>\n"
                + "        </values>\n" + "    </" + eventType.toString() + ">\n" + "</tran>";
    }

    /**
     * Creates an XML representation of update Event.
     * 
     * @param eventType when event Type is Update
     * @param subEventType userReferenceSubEventType.
     * @return XML representation.
     */
    public String toUpdateEventXML(EventType eventType, UserReferenceUpdateSubType subEventType) {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlToSend.append("<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <" + eventType.toString() + " schema=\"USER_REFERENCE\">\n");
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

    private StringBuffer createValues(UserReferenceUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();
        appendUserRefenceDescrUpdate(subEventType,values);
        appendEInvoiceRefUpdate(subEventType, values);
        appendLogicalDelete(subEventType, values);
        return values;
    }

    private void appendUserRefenceDescrUpdate(UserReferenceUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equalsIgnoreCase("CHANGE_USERREF_DESCR")) {
            values.append("            <cell name=\"USER_REF_DESCR\" type=\"VARCHAR\">" + userRefDescr + "</cell>\n");
        } 
    }

    private void appendEInvoiceRefUpdate(UserReferenceUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equalsIgnoreCase("CHANGE_EINVOICE_REF")) {
            values.append("            <cell name=\"EINVOICE_REF\" type=\"VARCHAR\">" + eInvoiceRef + "</cell>\n");
        }
    }

    private void appendLogicalDelete(UserReferenceUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equalsIgnoreCase("LOGICAL_DELETE")) {
            values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeleted ? "Y":"N") + "</cell>\n");
        }
    }

    private StringBuffer createOldValues() {
        StringBuffer values = new StringBuffer();
        values.append("            <cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + subscriptionId + "</cell>\n");
        values.append("            <cell name=\"NUMBER_TYPE\" type=\"CHAR\">" + numberType + "</cell>\n");
        values.append("            <cell name=\"USER_REF_DESCR\" type=\"VARCHAR\">" + oldUserRefDescr + "</cell>\n");
        values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (oldInfoIsDeleted ? "Y":"N") + "</cell>\n");
        values.append("            <cell name=\"EINVOICE_REF\" type=\"VARCHAR\">" + oldEInvoiceRef + "</cell>\n");
        return values;
    }
}
