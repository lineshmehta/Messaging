package com.telenor.cos.messaging.web.form;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Common Form containing fields relevant for all use cases.
 */

public class SubscriptionForm {

    private Long accId;
    private Long contractId;
    private Long custIdPayer;
    private Long custIdResp;
    private Long custIdUser;
    private Long directoryNumberId;
    private Long oldAccountId;
    private Long oldCustIdUser;

    private boolean infoIsDeleted;
    private boolean oldSubscrHasSecretNumber;
    private boolean subscrHasSecretNumber;

    private String oldS212ProductId;
    private String oldSubscriptionStatusId;
    private String s212ProductId;
    private String subscrStatusId;
    private String subscrTypeId;

    private Long subscrId;

    private Date subscrValidFromDate;
    private Date subscrValidToDate;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");

    private RelSubscriptionForm relSubscriptionForm;
    
    private UserReferenceForm userReferenceForm;

    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum SubscriptionUpdateSubType {
        UPDATE_ONE, UPDATE_TWO, LOGICAL_DELETE, EXPIRED, CHANGE_ACCOUNT, SECRETNUMBER_CHANGE, CHANGE_SUBSCRIPTION_STATUS, CHANGE_USER, CHANGE_SUBSCRIPTION_TYPE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    /**
     * @return the field
     */
    public Long getSubscrId() {
        return subscrId;
    }

    /**
     * @param subscrId
     *            field
     */
    public void setSubscrId(Long subscrId) {
        this.subscrId = subscrId;
    }

    /**
     * @return the field
     */
    public Long getContractId() {
        return contractId;
    }

    /**
     * @param contractId
     *            field
     */
    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the field
     */
    public String getS212ProductId() {
        return s212ProductId;
    }

    /**
     * @param s212ProductId
     *            field
     */
    public void setS212ProductId(String s212ProductId) {
        this.s212ProductId = StringEscapeUtils.escapeXml(s212ProductId);
    }

    /**
     * @return the field
     */
    public Long getDirectoryNumberId() {
        return directoryNumberId;
    }

    /**
     * @param directoryNumberId
     *            field
     */
    public void setDirectoryNumberId(Long directoryNumberId) {
        this.directoryNumberId = directoryNumberId;
    }

    /**
     * @return the field
     */
    public Long getCustIdResp() {
        return custIdResp;
    }

    /**
     * @param custIdResp
     *            field
     */
    public void setCustIdResp(Long custIdResp) {
        this.custIdResp = custIdResp;
    }

    /**
     * @return the field
     */
    public Long getCustIdPayer() {
        return custIdPayer;
    }

    /**
     * @param custIdPayer
     *            field
     */
    public void setCustIdPayer(Long custIdPayer) {
        this.custIdPayer = custIdPayer;
    }

    /**
     * @return the field
     */
    public Long getCustIdUser() {
        return custIdUser;
    }

    /**
     * @param custIdUser
     *            field
     */
    public void setCustIdUser(Long custIdUser) {
        this.custIdUser = custIdUser;
    }

    /**
     * @return the field
     */
    public Date getSubscrValidFromDate() {
        return subscrValidFromDate != null ? (Date) subscrValidFromDate.clone() : null;
    }

    /**
     * @param subscrValidFromDate
     *            field
     */
    public void setSubscrValidFromDate(Date subscrValidFromDate) {
        this.subscrValidFromDate = subscrValidFromDate != null ? (Date) subscrValidFromDate.clone() : null;
    }

    /**
     * @return the field
     */
    public Date getSubscrValidToDate() {
        return subscrValidToDate != null ? (Date) subscrValidToDate.clone() : null;
    }

    /**
     * @param subscrValidToDate
     *            field
     */
    public void setSubscrValidToDate(Date subscrValidToDate) {
        this.subscrValidToDate = subscrValidToDate != null ? (Date) subscrValidToDate.clone() : null;
    }

    /**
     * @return the field
     */
    public String getSubscrStatusId() {
        return subscrStatusId;
    }

    /**
     * @param subscrStatusId
     *            field
     */
    public void setSubscrStatusId(String subscrStatusId) {
        this.subscrStatusId = StringEscapeUtils.escapeXml(subscrStatusId);
    }

    /**
     * @return the field
     */
    public boolean getSubscrHasSecretNumber() {
        return subscrHasSecretNumber;
    }

    /**
     * @param subscrHasSecretNumber
     *            field
     */
    public void setSubscrHasSecretNumber(boolean subscrHasSecretNumber) {
        this.subscrHasSecretNumber = subscrHasSecretNumber;
    }

    /**
     * @return the field
     */
    public Long getAccId() {
        return accId;
    }

    /**
     * @param accId
     *            field
     */
    public void setAccId(Long accId) {
        this.accId = accId;
    }

    /**
     * @return the field
     */
    public boolean getInfoIsDeleted() {
        return infoIsDeleted;
    }

    /**
     * @param infoIsDeleted
     *            field
     */
    public void setInfoIsDeleted(boolean infoIsDeleted) {
        this.infoIsDeleted = infoIsDeleted;
    }

    public String getSubscrTypeId() {
        return subscrTypeId;
    }

    public void setSubscrTypeId(String subscrTypeId) {
        this.subscrTypeId = subscrTypeId;
    }

    public Long getOldAccountId() {
        return oldAccountId;
    }

    public void setOldAccountId(Long oldAccountId) {
        this.oldAccountId = oldAccountId;
    }

    public Long getOldCustIdUser() {
        return oldCustIdUser;
    }

    public void setOldCustIdUser(Long oldCustIdUser) {
        this.oldCustIdUser = oldCustIdUser;
    }

    public boolean getOldSubscrHasSecretNumber() {
        return oldSubscrHasSecretNumber;
    }

    public void setOldSubscrHasSecretNumber(boolean oldSubscrHasSecretNumber) {
        this.oldSubscrHasSecretNumber = oldSubscrHasSecretNumber;
    }

    public String getOldSubscriptionStatusId() {
        return oldSubscriptionStatusId;
    }

    public void setOldSubscriptionStatusId(String oldSubscriptionStatusId) {
        this.oldSubscriptionStatusId = StringEscapeUtils.escapeXml(oldSubscriptionStatusId);
    }

    public String getOldS212ProductId() {
        return oldS212ProductId;
    }

    public void setOldS212ProductId(String oldS212ProductId) {
        this.oldS212ProductId = StringEscapeUtils.escapeXml(oldS212ProductId);
    }

    public RelSubscriptionForm getRelSubscriptionForm() {
        return relSubscriptionForm;
    }

    public void setRelSubscriptionForm(RelSubscriptionForm relSubscriptionForm) {
        this.relSubscriptionForm = relSubscriptionForm;
    }
    
    public UserReferenceForm getUserReferenceForm() {
        return userReferenceForm;
    }

    public void setUserReferenceForm(UserReferenceForm userReferenceForm) {
        this.userReferenceForm = userReferenceForm;
    }

    @Override
    public String toString() {
        return "SubscriptionForm [accId=" + accId + ", contractId="
                + contractId + ", custIdPayer=" + custIdPayer + ", custIdResp="
                + custIdResp + ", custIdUser=" + custIdUser
                + ", directoryNumberId=" + directoryNumberId
                + ", oldAccountId=" + oldAccountId + ", oldCustIdUser="
                + oldCustIdUser + ", infoIsDeleted=" + infoIsDeleted
                + ", oldSubscrHasSecretNumber=" + oldSubscrHasSecretNumber
                + ", subscrHasSecretNumber=" + subscrHasSecretNumber
                + ", oldS212ProductId=" + oldS212ProductId
                + ", oldSubscriptionStatusId=" + oldSubscriptionStatusId
                + ", s212ProductId=" + s212ProductId + ", subscrStatusId="
                + subscrStatusId + ", subscrTypeId=" + subscrTypeId
                + ", subscrId=" + subscrId + ", subscrValidFromDate="
                + subscrValidFromDate + ", subscrValidToDate="
                + subscrValidToDate + ", relSubscriptionForm="
                + relSubscriptionForm + "]";
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
                + "    <" + eventType.toString() + " schema=\"SUBSCRIPTION\">\n" + "        <values>\n"
                + "            <cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + subscrId + "</cell>\n"
                + "            <cell name=\"CONTRACT_ID\" type=\"INT\">" + contractId + "</cell>\n"
                + "            <cell name=\"PRODUCT_ID\" type=\"INT\">NULL</cell>\n"
                + "            <cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">NULL</cell>\n"
                + "            <cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">" + directoryNumberId + "</cell>\n"
                + "            <cell name=\"CUST_ID_RESP\" type=\"NUMERIC\">" + custIdResp + "</cell>\n"
                + "            <cell name=\"CUST_ID_PAYER\" type=\"NUMERIC\">" + custIdPayer + "</cell>\n"
                + "            <cell name=\"CUST_ID_USER\" type=\"NUMERIC\">" + custIdUser + "</cell>\n"
                + "            <cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">"
                + (subscrValidFromDate != null ? simpleDateFormat.format(subscrValidFromDate) : "") + "</cell>\n"
                + "            <cell name=\"SUBSCR_VALID_TO_DATE\" type=\"DATETIME\">"
                + (subscrValidToDate != null ? simpleDateFormat.format(subscrValidToDate) : "") + "</cell>\n"
                + "            <cell name=\"SUBSCR_STATUS_ID\" type=\"CHAR\">" + subscrStatusId + "</cell>\n"
                + "            <cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\">" + (subscrHasSecretNumber ? "Y" : "N")  + "</cell>\n"
                + "            <cell name=\"SUBSCR_TYPE_ID\" type=\"CHAR\">" + subscrTypeId + "</cell>\n"
                + "            <cell name=\"ACC_ID\" type=\"INT\">" + accId + "</cell>\n"
                + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + infoIsDeleted + "</cell>\n"
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
    public String toUpdateEventXML(EventType eventType, SubscriptionUpdateSubType subEventType) {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlToSend.append("<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <" + eventType.toString() + " schema=\"SUBSCRIPTION\">\n");
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

    private StringBuffer createValues(SubscriptionUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();
        appendUpdate1(subEventType,values);
        appendUpdate2(subEventType,values);
        appendLocicaldeleteValue(subEventType, values);
        appendExpiredValue(subEventType, values);
        appendChangeAccountValue(subEventType, values);
        appendSecretNumber(subEventType, values);
        appendSubscriptionStatus(subEventType, values);
        appendChangeUser(subEventType, values);
        appendChangeSubscriptionType(subEventType, values);
        return values;
    }

    private void appendUpdate1(SubscriptionUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("update_one")) {
            values.append("            <cell name=\"S212_PRODUCT_ID\" type=\"CHAR\">" + s212ProductId + "</cell>\n");
        } 
    }
    
    private void appendUpdate2(SubscriptionUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("update_two")) {
            values.append("            <cell name=\"DEALER_ID\" type=\"INT\">" + "3046" + "</cell>\n");
        }
    }

    private void appendChangeSubscriptionType(SubscriptionUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("change_subscription_type")) {
            values.append("            <cell name=\"S212_PRODUCT_ID\" type=\"CHAR\">" + s212ProductId + "</cell>\n");
        }
    }

    private void appendChangeUser(SubscriptionUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("change_user")) {
            values.append("            <cell name=\"CUST_ID_USER\" type=\"CHAR\">" + custIdUser + "</cell>\n");
        }
    }

    private void appendSubscriptionStatus(SubscriptionUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("change_subscription_status")) {
            values.append("            <cell name=\"SUBSCR_STATUS_ID\" type=\"CHAR\">" + subscrStatusId + "</cell>\n");
        }
    }

    private void appendSecretNumber(SubscriptionUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("secretnumber_change")) {
            values.append("            <cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\">" + (subscrHasSecretNumber ? "Y" : "N") + "</cell>\n");
        }
    }

    private void appendChangeAccountValue(SubscriptionUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("change_account")) {
            values.append("            <cell name=\"ACC_ID\" type=\"CHAR\">" + accId + "</cell>\n");
        }
    }

    private void appendExpiredValue(SubscriptionUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("expired")) {
            values.append("            <cell name=\"SUBSCR_VALID_TO_DATE\" type=\"CHAR\">"
                    + (subscrValidToDate != null ? simpleDateFormat.format(subscrValidToDate) : "") + "</cell>\n");
        }
    }

    private void appendLocicaldeleteValue(SubscriptionUpdateSubType subEventType, StringBuffer values) {
        if (subEventType.toString().equals("logical_delete")) {
            values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeleted ? "Y" : "N") + "</cell>\n");
        }
    }

    private StringBuffer createOldValues() {
        StringBuffer values = new StringBuffer();
        values.append("            <cell name=\"SUBSCR_ID\" type=\"NUMERIC\">" + subscrId + "</cell>\n");
        values.append("            <cell name=\"CONTRACT_ID\" type=\"INT\">" + contractId + "</cell>\n");
        values.append("            <cell name=\"S212_PRODUCT_ID\" type=\"NUMERIC\">" + oldS212ProductId + "</cell>\n");
        values.append("            <cell name=\"DEALER_ID\" type=\"INT\">" + null + "</cell>\n");
        values.append("            <cell name=\"DIRECTORY_NUMBER_ID\" type=\"NUMERIC\">" + directoryNumberId + "</cell>\n");
        values.append("            <cell name=\"CUST_ID_RESP\" type=\"NUMERIC\">" + custIdResp + "</cell>\n");
        values.append("            <cell name=\"CUST_ID_PAYER\" type=\"NUMERIC\">" + custIdPayer + "</cell>\n");
        values.append("            <cell name=\"CUST_ID_USER\" type=\"NUMERIC\">" + oldCustIdUser + "</cell>\n");
        values.append("            <cell name=\"SUBSCR_TYPE_ID\" type=\"CHAR\">" + subscrTypeId + "</cell>\n");
        values.append("            <cell name=\"SUBSCR_VALID_FROM_DATE\" type=\"DATETIME\">"
                + (subscrValidFromDate != null ? simpleDateFormat.format(subscrValidFromDate) : "") + "</cell>\n");
        values.append("            <cell name=\"SUBSCR_STATUS_ID\" type=\"CHAR\">" + oldSubscriptionStatusId + "</cell>\n");
        values.append("            <cell name=\"SUBSCR_HAS_SECRET_NUMBER\" type=\"CHAR\">" + (oldSubscrHasSecretNumber ? "Y" : "N")
                + "</cell>\n");
        values.append("            <cell name=\"ACC_ID\" type=\"INT\">" + oldAccountId + "</cell>\n");
        return values;
    }
}