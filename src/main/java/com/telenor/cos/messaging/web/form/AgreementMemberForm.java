package com.telenor.cos.messaging.web.form;

import java.util.UUID;

public class AgreementMemberForm {

    private Long agreementMemberId;
    private Long agreementId;
    private Integer customerUnitNumber;
    private Long masterId;
    private boolean infoIsDeleted;

    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public Long getAgreementMemberId() {
        return agreementMemberId;
    }

    public void setAgreementMemberId(Long agreementMemberId) {
        this.agreementMemberId = agreementMemberId;
    }

    public boolean getInfoIsDeleted() {
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

    public Integer getCustomerUnitNumber() {
        return customerUnitNumber;
    }

    public void setCustomerUnitNumber(Integer customerUnitNumber) {
        this.customerUnitNumber = customerUnitNumber;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    /**
     * creates an xml representation of this form
     *
     * @return yes, it does sometimes
     */
    public String toNewEventXML() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n" +
                "    <" + EventType.INSERT + " schema=\"AGREEMENT_MEMBER_NEW\">\n" +
                "        <values>\n" +
                "            <cell name=\"AGREEMENT_MEMBER_ID\" type=\"NUMERIC\">"+ agreementMemberId +"</cell>\n" +
                "            <cell name=\"AGREEMENT_ID\" type=\"NUMERIC\">" + agreementId + "</cell>\n" +
                "            <cell name=\"CUST_UNIT_NUMBER\" type=\"INT\">"+ customerUnitNumber + "</cell>\n" +
                "            <cell name=\"MASTER_ID\" type=\"NUMERIC\">"+ masterId +"</cell>\n" +
                "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">N</cell>\n" +
                "        </values>\n" + 
                "    </" + EventType.INSERT + ">\n" +
                "</tran>";
    }

    /**
     * Creates an XML representation of update Event.
     *
     * @return XML representation.
     */
    public String toUpdateEventXML() {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlToSend.append("<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <" + EventType.UPDATE + " schema=\"AGREEMENT_MEMBER_NEW\">\n");
        xmlToSend.append("        <values>\n");
        xmlToSend.append(createValues());
        xmlToSend.append("        </values>\n");
        xmlToSend.append("        <oldValues>\n");
        xmlToSend.append(createOldValues());
        xmlToSend.append("        </oldValues>\n");
        xmlToSend.append("    </" + EventType.UPDATE + ">\n");
        xmlToSend.append("</tran>");
        return xmlToSend.toString();
    }

    private String createValues() {
        StringBuilder sb = new StringBuilder();
        if (infoIsDeleted) {
            if (infoIsDeleted) {
                sb.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "Y" + "</cell>\n");
            }else{
                sb.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "N" + "</cell>\n");
            }
        }
        return sb.toString();
    }

    private String createOldValues() {
        StringBuilder sb = new StringBuilder();
        sb.append("             <cell name=\"AGREEMENT_MEMBER_ID\" type=\"NUMERIC\">" + agreementMemberId + "</cell>");
        sb.append("             <cell name=\"CUST_UNIT_NUMBER\" type=\"INT\">"+ customerUnitNumber + "</cell>");
        sb.append("             <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">N</cell> ");
        sb.append("             <cell name=\"AGREEMENT_ID\" type=\"CHAR\">" + agreementId + "</cell>");
        sb.append("             <cell name=\"MASTER_ID\" type=\"NUMERIC\">"+ masterId +"</cell>");
        return sb.toString() ;
    }
}
