package com.telenor.cos.messaging.web.form;

import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Form for UserResourceEvents.
 * @author Babaprakash D
 *
 */
public class UserResourceForm {

    private String csUserId;
    private Long resourceId;
    private String csUserIdOld;
    private String resourceIdOld;

    public String getCsUserId() {
        return csUserId;
    }
    public void setCsUserId(String csUserId) {
        this.csUserId = StringEscapeUtils.escapeXml(csUserId);
    }
    public Long getResourceId() {
        return resourceId;
    }
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getCsUserIdOld() {
        return csUserIdOld;
    }
    public void setCsUserIdOld(String csUserIdOld) {
        this.csUserIdOld = StringEscapeUtils.escapeXml(csUserIdOld);
    }
    public String getResourceIdOld() {
        return resourceIdOld;
    }
    public void setResourceIdOld(String resourceIdOld) {
        this.resourceIdOld = resourceIdOld;
    }

    public enum EventType {
        INSERT,DELETE,UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum UserResourceUpdateSubType {
        RESOURCE_ID_CHANGE,CS_USERID_CHANGE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    /**
     * creates an xml representation of this form
     * @return String representation of Xml.
     */
    public String toNewEventXML() {
        StringBuffer newEventXML = new StringBuffer();
        newEventXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        newEventXML.append("    <" + EventType.INSERT.toString() + " schema=\"USER_RESOURCE\">\n" + "        <values>\n");
        newEventXML.append("            <cell name=\"CS_USER_ID\" type=\"VARCHAR\">" + csUserId + "</cell>\n");
        newEventXML.append("            <cell name=\"RESOURCE_ID\" type=\"NUMERIC\">" + resourceId + "</cell>\n");
        newEventXML.append("        </values>\n" + "    </" + EventType.INSERT.toString() + ">\n" + "</tran>");
        return newEventXML.toString();
    }

    /**
     * creates an xml representation of this form
     * @param updateSubEventType updateSubEventType.
     * @return String representation of Xml.
     */
    public String toUpdateEventXML(UserResourceUpdateSubType updateSubEventType) {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <" + EventType.UPDATE.toString() + " schema=\"USER_RESOURCE\">\n" + "        <values>\n");
        xmlToSend.append(createValues(updateSubEventType).toString());
        xmlToSend.append("        </values>\n");
        xmlToSend.append("        <oldValues>\n");
        xmlToSend.append(createOldValues().toString());
        xmlToSend.append("        </oldValues>\n");
        xmlToSend.append("    </" + EventType.UPDATE.toString() + ">\n" + "</tran>");
        return xmlToSend.toString();
    }

    private StringBuffer createValues(UserResourceUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();
        if (subEventType.toString().equalsIgnoreCase("RESOURCE_ID_CHANGE")) {
            values.append("            <cell name=\"RESOURCE_ID\" type=\"NUMERIC\">"+ resourceId +"</cell>\n");
        }
        if (subEventType.toString().equalsIgnoreCase("CS_USERID_CHANGE")) {
            values.append("            <cell name=\"CS_USER_ID\" type=\"VARCHAR\">" + csUserId + "</cell>\n");
        }
        return values;
    }

    private StringBuffer createOldValues() {
        StringBuffer oldValues = new StringBuffer();
        oldValues.append("            <cell name=\"CS_USER_ID\" type=\"VARCHAR\">" + csUserIdOld + "</cell>\n");
        oldValues.append("            <cell name=\"RESOURCE_ID\" type=\"NUMERIC\">" + resourceIdOld + "</cell>\n");
        return oldValues;
    }

    /**
     * creates an xml representation of this form
     * @return String representation of Xml
     */
    public String toDeleteEventXML() {
        StringBuffer deleteEventXML = new StringBuffer();
        deleteEventXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        deleteEventXML.append("    <" + EventType.DELETE.toString() + " schema=\"USER_RESOURCE\">\n" + "        <oldValues>\n");
        deleteEventXML.append("            <cell name=\"CS_USER_ID\" type=\"VARCHAR\">" + csUserId + "</cell>\n");
        deleteEventXML.append("            <cell name=\"RESOURCE_ID\" type=\"NUMERIC\">" + resourceId + "</cell>\n");
        deleteEventXML.append("        </oldValues>\n" + "    </" + EventType.DELETE.toString() + ">\n" + "</tran>");
        return deleteEventXML.toString();
    }

    @Override
    public String toString() {
        return "UserResourceForm [csUserId=" + csUserId + ", resourceId="
                + resourceId + "]";
    }
}
