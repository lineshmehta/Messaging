package com.telenor.cos.messaging.web.form;

import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Web Form TnuIduserMapping.
 * @author Babaprakash D
 * 
 */
public class TnuUserIdMappingForm {

    private String tnuId;
    private Integer applicationId;
    private String csUserId;

    public enum EventType {
        INSERT, DELETE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    
    public String getTnuId() {
        return tnuId;
    }

    public void setTnuId(String tnuId) {
        this.tnuId = StringEscapeUtils.escapeXml(tnuId);
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getCsUserId() {
        return csUserId;
    }

    public void setCsUserId(String csUserId) {
        this.csUserId = StringEscapeUtils.escapeXml(csUserId);
    }
    
    @Override
    public String toString() {
        return "TnuUserIdMappingForm [tnuId=" + tnuId + ", applicationId="
                + applicationId + ", csUserId=" + csUserId + "]";
    }

    /**
     * creates an xml representation of this form
     * 
     * @param eventType
     *            insert
     * @return yes, it does sometimes
     */
    public String toNewEventXML(EventType eventType) {
        StringBuffer newEventXML = new StringBuffer();
        newEventXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<tran eventId=\"" + UUID.randomUUID().toString()
                + "\">\n");
        newEventXML.append("    <" + eventType.toString() + " schema=\"tnuid_user_mapping\">\n" + "        <values>\n");
        newEventXML.append("            <cell name=\"tnu_id\" type=\"VARCHAR\">" + tnuId + "</cell>\n");
        newEventXML.append("            <cell name=\"application_id\" type=\"NUMERIC\">" + applicationId + "</cell>\n");
        newEventXML.append("            <cell name=\"cs_user_id\" type=\"VARCHAR\">" + csUserId + "</cell>\n");
        newEventXML.append("        </values>\n" + "    </" + eventType.toString() + ">\n" + "</tran>");
        return newEventXML.toString();
    }

    /**
     * creates an xml representation of this form
     * 
     * @param eventType
     *            insert
     * @return yes, it does sometimes
     */
    public String toDeleteEventXML(EventType eventType) {
        StringBuffer deleteEventXML = new StringBuffer();
        deleteEventXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        deleteEventXML.append("    <" + eventType.toString() + " schema=\"tnuid_user_mapping\">\n" + "        <oldValues>\n");
        deleteEventXML.append("            <cell name=\"tnu_id\" type=\"VARCHAR\">" + tnuId + "</cell>\n");
        deleteEventXML.append("            <cell name=\"application_id\" type=\"NUMERIC\">" + applicationId + "</cell>\n");
        deleteEventXML.append("            <cell name=\"cs_user_id\" type=\"VARCHAR\">" + csUserId + "</cell>\n");
        deleteEventXML.append("         </oldValues>\n" + "    </" + eventType.toString() + ">\n" + "</tran>");
        return deleteEventXML.toString();
    }
}