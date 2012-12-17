package com.telenor.cos.messaging.web.form;

import java.text.SimpleDateFormat;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

public class AccountForm {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private Long accId;
    private Long custIdPayer;
    private Long custIdResp;
    private Long oldCustIdResp;
    private Long oldCustIdPayer;
    
    private String accInvMedium;
    private String accountName;
    private String accTypeId;
    private String accStatusId;
    private String accStatusId2;
    private String oldAccInvMedium;
    private String oldAccountName;
    private String oldAccountStatusId;
    private String oldAccountStatusId2;
    private String oldAccountTypeId;
    
    private boolean infoIsDeleted;

    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum AccountUpdateSubType {
        NAME_CHANGE,STATUS_UPDATE,INVOICE_FORMAT_CHANGE,LOGICAL_DELETE,PAYER_CHANGE,OWNER_CHANGE,PAYMENT_STATUS_CHANGE,TYPE_CHANGE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    
    public Long getAccId() {
        return accId;
    }

    public void setAccId(Long accId) {
        this.accId = accId;
    }

    public Long getCustIdPayer() {
        return custIdPayer;
    }

    public void setCustIdPayer(Long custIdPayer) {
        this.custIdPayer = custIdPayer;
    }

    public Long getCustIdResp() {
        return custIdResp;
    }

    public void setCustIdResp(Long custIdResp) {
        this.custIdResp = custIdResp;
    }

    public Long getOldCustIdPayer() {
        return oldCustIdPayer;
    }

    public void setOldCustIdPayer(Long oldCustIdPayer) {
        this.oldCustIdPayer = oldCustIdPayer;
    }

    public Long getOldCustIdResp() {
        return oldCustIdResp;
    }

    public void setOldCustIdResp(Long oldCustIdResp) {
        this.oldCustIdResp = oldCustIdResp;
    }

    public String getAccInvMedium() {
        return accInvMedium;
    }

    public void setAccInvMedium(String accInvMedium) {
        this.accInvMedium = StringEscapeUtils.escapeXml(accInvMedium);
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = StringEscapeUtils.escapeXml(accountName);
    }

    public String getAccTypeId() {
        return accTypeId;
    }

    public void setAccTypeId(String accTypeId) {
        this.accTypeId = StringEscapeUtils.escapeXml(accTypeId);
    }

    public String getAccStatusId() {
        return accStatusId;
    }

    public void setAccStatusId(String accStatusId) {
        this.accStatusId = StringEscapeUtils.escapeXml(accStatusId);
    }

    public String getAccStatusId2() {
        return accStatusId2;
    }

    public void setAccStatusId2(String accStatusId2) {
        this.accStatusId2 = StringEscapeUtils.escapeXml(accStatusId2);
    }

    public String getOldAccInvMedium() {
        return oldAccInvMedium;
    }

    public void setOldAccInvMedium(String oldAccInvMedium) {
        this.oldAccInvMedium = StringEscapeUtils.escapeXml(oldAccInvMedium);
    }

    public String getOldAccountName() {
        return oldAccountName;
    }

    public void setOldAccountName(String oldAccountName) {
        this.oldAccountName = StringEscapeUtils.escapeXml(oldAccountName);
    }

    public String getOldAccountStatusId() {
        return oldAccountStatusId;
    }

    public void setOldAccountStatusId(String oldAccountStatusId) {
        this.oldAccountStatusId = StringEscapeUtils.escapeXml(oldAccountStatusId);
    }

    public String getOldAccountStatusId2() {
        return oldAccountStatusId2;
    }

    public void setOldAccountStatusId2(String oldAccountStatusId2) {
        this.oldAccountStatusId2 = StringEscapeUtils.escapeXml(oldAccountStatusId2);
    }

    public String getOldAccountTypeId() {
        return oldAccountTypeId;
    }

    public void setOldAccountTypeId(String oldAccountTypeId) {
        this.oldAccountTypeId = StringEscapeUtils.escapeXml(oldAccountTypeId);
    }

    public boolean isInfoIsDeleted() {
        return infoIsDeleted;
    }

    public void setInfoIsDeleted(boolean infoIsDeleted) {
        this.infoIsDeleted = infoIsDeleted;
    }
    
    @Override
    public String toString() {
        return "AccountForm [simpleDateFormat=" + simpleDateFormat + ", accId="
                + accId + ", custIdPayer=" + custIdPayer + ", custIdResp="
                + custIdResp + ", oldCustIdPayer=" + oldCustIdPayer + ", oldCustIdResp=" + oldCustIdResp
                + ", accInvMedium=" + accInvMedium + ", accountName="
                + accountName + ", accTypeId=" + accTypeId + ", accStatusId="
                + accStatusId + ", accStatusId2=" + accStatusId2
                + ", oldAccInvMedium=" + oldAccInvMedium + ", oldAccountName="
                + oldAccountName + ", oldAccountStatusId=" + oldAccountStatusId
                + ", oldAccountStatusId2=" + oldAccountStatusId2
                + ", oldAccountTypeId=" + oldAccountTypeId + ", infoIsDeleted="
                + infoIsDeleted + "]";
    }

    /**
     * creates an xml representation of this form
     *
     * @param eventType insert, update or whatever
     * @return yes, it does sometimes
     */
    public String toNewEventXML(EventType eventType) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n" +
                "    <" + eventType.toString() + " schema=\"ACCOUNT\">\n" +
                "        <values>\n" +
                "            <cell name=\"ACC_ID\" type=\"INT\">"+ accId +"</cell>\n" +
                "            <cell name=\"CUST_ID_RESP\" type=\"NUMERIC\">" + custIdResp + "</cell>\n" +
                "            <cell name=\"CUST_ID_PAYER\" type=\"NUMERIC\">"+ custIdPayer + "</cell>\n" +
                "            <cell name=\"ACC_NAME\" type=\"VARCHAR\">"+ accountName +"</cell>\n" +
                "            <cell name=\"ACC_STATUS_ID\" type=\"CHAR\">"+ accStatusId +"</cell>\n" +
                "            <cell name=\"ACC_TYPE_ID\" type=\"CHAR\">"+ accTypeId +"</cell>\n" +
                "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">"+ infoIsDeleted +"</cell>\n" +
                "            <cell name=\"ACC_STATUS_ID2\" type=\"CHAR\">"+ accStatusId2 +"</cell>\n" +
                "            <cell name=\"ACC_INV_MEDIUM\" type=\"CHAR\">"+ accInvMedium +"</cell>\n" +
                "        </values>\n" + 
                "    </" + eventType.toString() + ">\n" +
                "</tran>";
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
    public String toUpdateEventXML(EventType eventType, AccountUpdateSubType subEventType) {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlToSend.append("<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <" + eventType.toString() + " schema=\"ACCOUNT\">\n");
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

    private StringBuffer createValues(AccountUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();
        if (subEventType.toString().equals("logical_delete")) {
            if (infoIsDeleted) {
                values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "Y" + "</cell>\n");
            }else {
                values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "N" + "</cell>\n");
            }
        }
        if (subEventType.toString().equals("invoice_format_change")) {
            values.append("            <cell name=\"ACC_INV_MEDIUM\" type=\"CHAR\">"+ accInvMedium +"</cell>\n");
        }
        if (subEventType.toString().equals("name_change")) {
            values.append("            <cell name=\"ACC_NAME\" type=\"VARCHAR\">" + accountName + "</cell>\n");
        }
        if (subEventType.toString().equals("payment_status_change")) {
            values.append("            <cell name=\"ACC_STATUS_ID\" type=\"CHAR\">" + accStatusId + "</cell>\n");
        }
        if (subEventType.toString().equals("payer_change")) {
            values.append("            <cell name=\"CUST_ID_PAYER\" type=\"NUMERIC\">"+ custIdPayer + "</cell>\n");
        }
        if (subEventType.toString().equals("owner_change")) {
            values.append("            <cell name=\"CUST_ID_RESP\" type=\"NUMERIC\">"+ custIdResp + "</cell>\n");
        }
        if (subEventType.toString().equals("status_update")) {
            values.append("            <cell name=\"ACC_STATUS_ID2\" type=\"CHAR\">"+ accStatusId2 +"</cell>\n");
        }
        if (subEventType.toString().equals("type_change")) {
            values.append("            <cell name=\"ACC_TYPE_ID\" type=\"CHAR\">"+ accTypeId +"</cell>\n");
        }
        return values;
    }

    private StringBuffer createOldValues() {
        StringBuffer values = new StringBuffer();
        values.append("            <cell name=\"ACC_ID\" type=\"INT\">" + accId +"</cell>\n");
        values.append("            <cell name=\"CUST_ID_RESP\" type=\"NUMERIC\">" + oldCustIdResp + "</cell>\n");
        values.append("            <cell name=\"CUST_ID_PAYER\" type=\"NUMERIC\">" + oldCustIdPayer + "</cell>\n");
        values.append("            <cell name=\"ACC_NAME\" type=\"VARCHAR\">" + oldAccountName + "</cell>\n");
        values.append("            <cell name=\"ACC_STATUS_ID\" type=\"CHAR\">" + oldAccountStatusId + "</cell>\n");
        values.append("            <cell name=\"ACC_TYPE_ID\" type=\"CHAR\">" + oldAccountTypeId + "</cell>\n");
        values.append("            <cell name=\"ACC_STATUS_ID2\" type=\"CHAR\">" + oldAccountStatusId2 + "</cell>\n");
        values.append("            <cell name=\"ACC_INV_MEDIUM\" type=\"CHAR\">" + oldAccInvMedium + "</cell>\n");
        return values;
    }
}
