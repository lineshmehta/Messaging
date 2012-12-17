package com.telenor.cos.messaging.web.form;

import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

public class MobileOfficeForm {

    private String directoryNumber;
    private String extensionNumber;
    private String extensionNumberOld;

    private boolean infoIsDeleted;
    private boolean infoIsDeletedOld;
    
    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum MobileOfficeUpdateSubType {
        LOGICAL_DELETE,CHANGE_EXTENSION_NUMBER;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }


    public String getDirectoryNumber() {
        return directoryNumber;
    }

    public void setDirectoryNumber(String directoryNumber) {
        this.directoryNumber = StringEscapeUtils.escapeXml(directoryNumber);
    }

    public String getExtensionNumber() {
        return extensionNumber;
    }

    public void setExtensionNumber(String extensionNumber) {
        this.extensionNumber = StringEscapeUtils.escapeXml(extensionNumber);
    }

    public String getExtensionNumberOld() {
        return extensionNumberOld;
    }

    public void setExtensionNumberOld(String extensionNumberOld) {
        this.extensionNumberOld = StringEscapeUtils.escapeXml(extensionNumberOld);
    }

    public boolean getInfoIsDeleted() {
        return infoIsDeleted;
    }

    public void setInfoIsDeleted(boolean infoIsDeleted) {
        this.infoIsDeleted = infoIsDeleted;
    }

    public boolean getInfoIsDeletedOld() {
        return infoIsDeletedOld;
    }

    public void setInfoIsDeletedOld(boolean infoIsDeletedOld) {
        this.infoIsDeletedOld = infoIsDeletedOld;
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
                "    <" + eventType.toString() + " schema=\"MOBILE_OFFICE_MEMBERS\">\n" +
                "        <values>\n" +
                "            <cell name=\"DIRECTORY_NUMBER_ID\" type=\"VARCHAR\">" + directoryNumber + "</cell>\n" +
                "            <cell name=\"EXTENSION_NUMBER\" type=\"VARCHAR\">" + extensionNumber + "</cell>\n" +
                "        </values>\n" +
                "    </" + eventType.toString() + ">\n" +
                "</tran>";
    }


    /**
     * Creates an XML representation of update Event.
     *
     * @param eventType when event Type is Update
     * @param subEventType mobileOfficeSubEventType.
     * @return XML representation.
     */
    public String toUpdateXML(EventType eventType, MobileOfficeUpdateSubType subEventType) {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlToSend.append("<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <" + eventType.toString() + " schema=\"MOBILE_OFFICE_MEMBERS\">\n");
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

    private StringBuffer createValues(MobileOfficeUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();
        appendExtensionNumberUpdate(subEventType, values);
        appendLogicalDelete(subEventType, values);
        return values;
    }

    private void appendExtensionNumberUpdate(MobileOfficeUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equalsIgnoreCase("CHANGE_EXTENSION_NUMBER")) {
            values.append("            <cell name=\"EXTENSION_NUMBER\" type=\"VARCHAR\">" + extensionNumber + "</cell>\n");
        }
    }

    private void appendLogicalDelete(MobileOfficeUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equalsIgnoreCase("LOGICAL_DELETE")) {
            values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeleted ? "Y":"N") + "</cell>\n");
        }
    }

    private StringBuffer createOldValues(MobileOfficeUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();
        values.append("            <cell name=\"DIRECTORY_NUMBER_ID\" type=\"VARCHAR\">" + directoryNumber + "</cell>\n");
        values.append("            <cell name=\"EXTENSION_NUMBER\" type=\"VARCHAR\">" + extensionNumberOld + "</cell>\n");
        if (subEventType.toString().equalsIgnoreCase("LOGICAL_DELETE")) {
            values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeletedOld ? "Y":"N") + "</cell>\n");
        }
        return values;

    }
}
