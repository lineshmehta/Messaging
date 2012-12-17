package com.telenor.cos.messaging.web.form;

import java.util.UUID;

/**
 * @author t798435
 *
 */
public class MasterStructureForm {

    private Long mastIdOwner;
    private Long oldMastIdOwner;
    private Long mastIdMember;
    
    private boolean infoIsDeleted;
    
    public enum EventType {
        INSERT, UPDATE, DELETE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    
    public Long getMastIdOwner() {
        return mastIdOwner;
    }

    public void setMastIdOwner(Long mastIdOwner) {
        this.mastIdOwner = mastIdOwner;
    }

    public Long getOldMastIdOwner() {
        return oldMastIdOwner;
    }

    public void setOldMastIdOwner(Long oldMastIdOwner) {
        this.oldMastIdOwner = oldMastIdOwner;
    }

    public Long getMastIdMember() {
        return mastIdMember;
    }

    public void setMastIdMember(Long mastIdMember) {
        this.mastIdMember = mastIdMember;
    }

    public boolean isInfoIsDeleted() {
        return infoIsDeleted;
    }

    public void setInfoIsDeleted(boolean infoIsDeleted) {
        this.infoIsDeleted = infoIsDeleted;
    }
    
    @Override
    public String toString() {
        return "MasterStructureForm [mastIdOwner=" + mastIdOwner
                + ", oldMastIdOwner=" + oldMastIdOwner + ", mastIdMember="
                + mastIdMember + ", infoIsDeleted=" + infoIsDeleted + "]";
    }

    /**
     * creates an xml representation of this form
     * 
     * @param eventType insert.
     * @return New Event Xml.
     */
    public String toNewEventXML(EventType eventType) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n"
                + "    <" + eventType.toString() + " schema=\"MAST_STRUCTURE\">\n" + "        <values>\n"
                + "            <cell name=\"MAST_ID_OWNER\" type=\"NUMERIC\">" + mastIdOwner + "</cell>\n"
                + "            <cell name=\"MAST_ID_MEMBER\" type=\"NUMERIC\">" + mastIdMember + "</cell>\n"
                + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeleted ? "Y":"N") + "</cell>\n"
                + "        </values>\n" + "    </" + eventType.toString() + ">\n" + "</tran>";
    }

    /**
     * Creates an XML representation of update and delete Event.
     * Note both update and delete will have similar xml representation
     * @param eventType
     *            when event Type is Update
     * @return XML representation.
     */
    public String toUpdateEventXML(EventType eventType) {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlToSend.append("<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <update schema=\"MAST_STRUCTURE\">\n");
        xmlToSend.append("        <values>\n");
        xmlToSend.append(createValues(eventType).toString());
        xmlToSend.append("        </values>\n");
        xmlToSend.append("        <oldValues>\n");
        xmlToSend.append(createOldValues().toString());
        xmlToSend.append("        </oldValues>\n");
        xmlToSend.append("    </update>\n");
        xmlToSend.append("</tran>");
        return xmlToSend.toString();
    }

    private StringBuffer createValues(EventType eventType) {
        StringBuffer values = new StringBuffer();

        if (eventType.toString().equalsIgnoreCase("delete")) {
            if (infoIsDeleted) {
                values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "Y" + "</cell>\n");
            } else {
                values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "N" + "</cell>\n");
            }
        } else if (eventType.toString().equals("update")) {
            values.append("            <cell name=\"MAST_ID_OWNER\" type=\"NUMERIC\">" + mastIdOwner + "</cell>\n");
        }
        return values;
    }

    private StringBuffer createOldValues() {
        StringBuffer values = new StringBuffer();
        values.append("            <cell name=\"MAST_ID_OWNER\" type=\"NUMERIC\">" + mastIdOwner + "</cell>\n");
        values.append("            <cell name=\"MAST_ID_MEMBER\" type=\"NUMERIC\">" + mastIdMember + "</cell>\n");
        return values;
    }
}