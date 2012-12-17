package com.telenor.cos.messaging.web.form;

import java.util.UUID;

public class AgreementOwnerForm {

    private final String defaultXMLText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n";
    
    private Long agreementOwnerId;
    private Long masterId;
    private Long agreementId;
    private boolean infoIsDeleted;
    
    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public Long getAgreementOwnerId() {
        return agreementOwnerId;
    }

    public void setAgreementOwnerId(Long agreementOwnerId) {
        this.agreementOwnerId = agreementOwnerId;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public boolean isInfoIsDeleted() {
        return infoIsDeleted;
    }

    public void setInfoIsDeleted(boolean infoIsDeleted) {
        this.infoIsDeleted = infoIsDeleted;
    }
    
    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    /**
     * Creates an XML representation of new Event.
     *
     * @return XML representation.
     */
    public String toNewEventXML() {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append(defaultXMLText);
        xmlToSend.append("    <" + EventType.INSERT + " schema=\"AGREEMENT_OWNER\">\n");
        xmlToSend.append("        <values>\n");
        xmlToSend.append("            <cell name=\"AGREEMENT_ID\" type=\"NUMERIC\">" + agreementId + "</cell>\n");
        xmlToSend.append("            <cell name=\"AGREEMENT_OWNER_ID\" type=\"NUMERIC\">" + agreementOwnerId + "</cell>\n");
        xmlToSend.append("            <cell name=\"MASTER_ID\" type=\"NUMERIC\">" + masterId + "</cell>\n");
        xmlToSend.append("        </values>\n");
        xmlToSend.append("    </" + EventType.INSERT + ">\n");
        xmlToSend.append("</tran>");
        return xmlToSend.toString();
    }

    /**
     * Creates an XML representation of update Event.
     * @return XML representation.
     */
    public String createDeleteEventXML() {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append(defaultXMLText);
        xmlToSend.append("    <" + EventType.UPDATE + " schema=\"AGREEMENT_OWNER\">\n");
        xmlToSend.append("        <values>\n");
        xmlToSend.append("             <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">Y</cell>\n");
        xmlToSend.append("        </values>\n");
        xmlToSend.append("        <oldValues>\n");
        xmlToSend.append("            <cell name=\"AGREEMENT_OWNER_ID\" type=\"NUMERIC\">" + agreementOwnerId + "</cell>\n");
        xmlToSend.append("        </oldValues>\n");
        xmlToSend.append("    </" + EventType.UPDATE + ">\n");
        xmlToSend.append("</tran>");
        return xmlToSend.toString();
    }
}