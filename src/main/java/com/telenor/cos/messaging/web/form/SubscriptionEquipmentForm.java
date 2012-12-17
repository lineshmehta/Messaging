package com.telenor.cos.messaging.web.form;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Date;
import java.util.UUID;

public class SubscriptionEquipmentForm {

    private String subscriptionId;
    private String subscriptionIdOld;

    private String imsi;
    private String imsiOld;

    private boolean infoIsDeleted;
    private boolean infoIsDeletedOld;

    private Date validToDate;
    private Date validToDateOld;

    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum SubscriptionEquipmentUpdateSubType {
        LOGICAL_DELETE, CHANGE_IMSI_NUMBER, DATE_EXPIRED;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }


    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = StringEscapeUtils.escapeXml(subscriptionId);
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = StringEscapeUtils.escapeXml(imsi);
    }

    public boolean getInfoIsDeleted() {
        return infoIsDeleted;
    }

    public void setInfoIsDeleted(boolean infoIsDeleted) {
        this.infoIsDeleted = infoIsDeleted;
    }

    public Date getValidToDate() {
        return this.validToDate != null ? (Date) validToDate.clone() : null;
    }

    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate != null ? (Date) validToDate.clone() : null;
    }

    public String getSubscriptionIdOld() {
        return subscriptionIdOld;
    }

    public void setSubscriptionIdOld(String subscriptionIdOld) {
        this.subscriptionIdOld = subscriptionIdOld;
    }

    public String getImsiOld() {
        return imsiOld;
    }

    public void setImsiOld(String imsiOld) {
        this.imsiOld = imsiOld;
    }

    public boolean isInfoIsDeletedOld() {
        return infoIsDeletedOld;
    }

    public void setInfoIsDeletedOld(boolean infoIsDeletedOld) {
        this.infoIsDeletedOld = infoIsDeletedOld;
    }

    public Date getValidToDateOld() {
        return this.validToDateOld != null ? (Date) validToDateOld.clone() : null;
    }

    public void setValidtoDateOld(Date validtoDateOld) {
        this.validToDateOld = validtoDateOld != null ? (Date) validtoDateOld.clone() : null;
    }

    /**
     * creates an xml representation of this form
     *
     * @param eventType insert, update or whatever
     * @return yes, it does sometimes
     */
    public String toNewXML(EventType eventType) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n" +
                "    <" + eventType.toString() + " schema=\"SUBSCRIPTION_EQUIPMENT_INFO\">\n" +
                "        <values>\n" +
                "            <cell name=\"SUBSCR_ID\" type=\"VARCHAR\">" + subscriptionId + "</cell>\n" +
                "            <cell name=\"IMSI_NUMBER_ID\" type=\"VARCHAR\">" + imsi + "</cell>\n" +
                "        </values>\n" +
                "    </" + eventType.toString() + ">\n" +
                "</tran>";
    }


    /**
     * Creates an XML representation of update Event.
     *
     * @param eventType when event Type is Update
     * @param subEventType SubscriptionEquipmentSubEventType.
     * @return XML representation.
     */
    public String toUpdateXML(EventType eventType, SubscriptionEquipmentUpdateSubType subEventType) {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlToSend.append("<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <" + eventType.toString() + " schema=\"SUBSCRIPTION_EQUIPMENT_INFO\">\n");
        xmlToSend.append("        <values>\n");
        xmlToSend.append(createValues(subEventType).toString());
        xmlToSend.append("        </values>\n");
        xmlToSend.append("        <oldValues>\n");
        xmlToSend.append(createOldValues(subEventType).toString());
        xmlToSend.append("        </oldValues>\n");
        xmlToSend.append("    </" + eventType.toString() + ">\n");
        xmlToSend.append("</tran>");
        return xmlToSend.toString();
    }

    private StringBuffer createValues(SubscriptionEquipmentUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();
        appendImsiNumberUpdate(subEventType, values);
        appendLogicalDelete(subEventType, values);
        appendDateExpired(subEventType, values);
        return values;
    }

    private void appendImsiNumberUpdate(SubscriptionEquipmentUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equalsIgnoreCase("CHANGE_IMSI_NUMBER")) {
            values.append("            <cell name=\"IMSI_NUMBER_ID\" type=\"VARCHAR\">" + imsi + "</cell>\n");
        }
    }

    private void appendLogicalDelete(SubscriptionEquipmentUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equalsIgnoreCase("LOGICAL_DELETE")) {
            values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeleted ? "Y":"N") + "</cell>\n");
        }
    }

    private void appendDateExpired(SubscriptionEquipmentUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equalsIgnoreCase("DATE_EXPIRED")) {
            values.append("            <cell name=\"VALID_TO_DATE\" type=\"CHAR\">" + validToDate + "</cell>\n");
        }
    }

    private StringBuffer createOldValues(SubscriptionEquipmentUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();
        values.append("            <cell name=\"SUBSCR_ID\" type=\"VARCHAR\">" + subscriptionIdOld + "</cell>\n");
        values.append("            <cell name=\"IMSI_NUMBER_ID\" type=\"VARCHAR\">" + imsiOld + "</cell>\n");
        if (subEventType.toString().equalsIgnoreCase("LOGICAL_DELETE")) {
            values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeletedOld ? "Y":"N") + "</cell>\n");
        }
        if (subEventType.toString().equalsIgnoreCase("DATE_EXPIRED")) {
            values.append("            <cell name=\"VALID_TO_DATE\" type=\"CHAR\">" + validToDateOld + "</cell>\n");
        }
        return values;

    }
}
