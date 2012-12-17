package com.telenor.cos.messaging.web.form;

import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Form for DataKort and Twin.
 * @author Babaprakash D
 *
 */
public class RelSubscriptionForm {

    private Long subscrIdOwner;
    private Long subscrIdMember;
    private String relSubscrType;
    private boolean relInfoIsDeleted;
    private Long directoryNumberId;

    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public Long getSubscrIdOwner() {
        return subscrIdOwner;
    }

    public void setSubscrIdOwner(Long subscrIdOwner) {
        this.subscrIdOwner = subscrIdOwner;
    }

    public Long getSubscrIdMember() {
        return subscrIdMember;
    }

    public void setSubscrIdMember(Long subscrIdMember) {
        this.subscrIdMember = subscrIdMember;
    }

    public String getRelSubscrType() {
        return relSubscrType;
    }

    public void setRelSubscrType(String relSubscrType) {
        this.relSubscrType = StringEscapeUtils.escapeXml(relSubscrType);
    }

    public boolean isRelInfoIsDeleted() {
        return relInfoIsDeleted;
    }

    public void setRelInfoIsDeleted(boolean relInfoIsDeleted) {
        this.relInfoIsDeleted = relInfoIsDeleted;
    }
    public Long getDirectoryNumberId() {
        return directoryNumberId;
    }
    public void setDirectoryNumberId(Long directoryNumberId) {
        this.directoryNumberId = directoryNumberId;
    }

    @Override
    public String toString() {
        return "RelSubscriptionForm [subscrIdOwner=" + subscrIdOwner
                + ", subscrIdMember=" + subscrIdMember + ", relSubscrType="
                + relSubscrType + ", relInfoIsDeleted=" + relInfoIsDeleted
                + ", directoryNumberId=" + directoryNumberId + "]";
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
                + "    <" + eventType.toString() + " schema=\"REL_SUBSCRIPTION\">\n" + "        <values>\n"
                + "            <cell name=\"subscr_id_owner\" type=\"NUMERIC\">" + subscrIdOwner + "</cell>\n"
                + "            <cell name=\"subscr_id_member\" type=\"NUMERIC\">" + subscrIdMember + "</cell>\n"
                + "            <cell name=\"rel_subscr_type\" type=\"CHAR\">" + relSubscrType + "</cell>\n"
                + "            <cell name=\"info_is_deleted\" type=\"CHAR\">" + (relInfoIsDeleted ?"Y":"N") + "</cell>\n"
                + "        </values>\n" + "    </" + eventType.toString() + ">\n" + "</tran>";
    }
}
