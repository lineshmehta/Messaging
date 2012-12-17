package com.telenor.cos.messaging.web.form;

import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Web Form for Resource Events.
 * @author Babaprakash D
 *
 */
public class ResourceForm {

    private boolean infoIsDeleted;

    private boolean oldResourceHasContentInherit;
    private boolean oldResourceHasStructureInherit;
    private String oldResourceTypeIdKey;
    private boolean resourceHasContentInherit;
    private boolean resourceHasStructureInherit;
    private String resourceTypeIdKey;

    private Long resourceId;
    
    private Integer oldResourceTypeId;
    private Integer resourceTypeId;
    
    
    public enum EventType {
        INSERT, UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public enum ResourceUpdateSubType {
        LOGICAL_DELETE, CONTENT_INHERIT_UPDATE, STRUCTURE_INHERIT_UPDATE, TYPE_ID_UPDATE, TYPE_ID_KEY_UPDATE;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    
    public boolean isInfoIsDeleted() {
        return infoIsDeleted;
    }

    public void setInfoIsDeleted(boolean infoIsDeleted) {
        this.infoIsDeleted = infoIsDeleted;
    }

    public boolean getOldResourceHasContentInherit() {
        return oldResourceHasContentInherit;
    }

    public void setOldResourceHasContentInherit(boolean oldResourceHasContentInherit) {
        this.oldResourceHasContentInherit = oldResourceHasContentInherit;
    }

    public boolean getOldResourceHasStructureInherit() {
        return oldResourceHasStructureInherit;
    }

    public void setOldResourceHasStructureInherit(boolean oldResourceHasStructureInherit) {
        this.oldResourceHasStructureInherit = oldResourceHasStructureInherit;
    }

    public String getOldResourceTypeIdKey() {
        return oldResourceTypeIdKey;
    }

    public void setOldResourceTypeIdKey(String oldResourceTypeIdKey) {
        this.oldResourceTypeIdKey = StringEscapeUtils.escapeXml(oldResourceTypeIdKey);
    }

    public boolean getResourceHasContentInherit() {
        return resourceHasContentInherit;
    }

    public void setResourceHasContentInherit(boolean resourceHasContentInherit) {
        this.resourceHasContentInherit = resourceHasContentInherit;
    }

    public boolean getResourceHasStructureInherit() {
        return resourceHasStructureInherit;
    }

    public void setResourceHasStructureInherit(boolean resourceHasStructureInherit) {
        this.resourceHasStructureInherit = resourceHasStructureInherit;
    }

    public String getResourceTypeIdKey() {
        return resourceTypeIdKey;
    }

    public void setResourceTypeIdKey(String resourceTypeIdKey) {
        this.resourceTypeIdKey = StringEscapeUtils.escapeXml(resourceTypeIdKey);
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getOldResourceTypeId() {
        return oldResourceTypeId;
    }

    public void setOldResourceTypeId(Integer oldResourceTypeId) {
        this.oldResourceTypeId = oldResourceTypeId;
    }

    public Integer getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Integer resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }
    
    @Override
    public String toString() {
        return "ResourceForm [infoIsDeleted=" + infoIsDeleted
                + ", oldResourceHasContentInherit="
                + oldResourceHasContentInherit
                + ", oldResourceHasStructureInherit="
                + oldResourceHasStructureInherit + ", oldResourceTypeIdKey="
                + oldResourceTypeIdKey + ", resourceHasContentInherit="
                + resourceHasContentInherit + ", resourceHasStructureInherit="
                + resourceHasStructureInherit + ", resourceTypeIdKey="
                + resourceTypeIdKey + ", resourceId=" + resourceId
                + ", oldResourceTypeId=" + oldResourceTypeId
                + ", resourceTypeId=" + resourceTypeId + "]";
    }

    /**
     * creates an xml representation of this form
     * 
     * @param eventType insert.
     * @return New Event Xml.
     */
    public String toNewEventXML(EventType eventType) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n"
                + "    <" + eventType.toString() + " schema=\"RESOURCE\">\n" + "        <values>\n"
                + "            <cell name=\"RESOURCE_ID\" type=\"NUMERIC\">"+ resourceId +"</cell>\n"
                + "            <cell name=\"RESOURCE_TYPE_ID\" type=\"NUMERIC\">" + resourceTypeId +"</cell>\n"
                + "            <cell name=\"RESOURCE_TYPE_ID_KEY\" type=\"VARCHAR\">" + resourceTypeIdKey +"</cell>\n"
                + "            <cell name=\"RESOURCE_HAS_CONTENT_INHERIT\" type=\"CHAR\">" + (resourceHasContentInherit ? "Y":"N") +"</cell>\n"
                + "            <cell name=\"RESOURCE_HAS_STRUCTURE_INHERIT\" type=\"CHAR\">"+ (resourceHasStructureInherit ? "Y":"N") +"</cell>\n"
                + "            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + (infoIsDeleted ? "Y":"N") + "</cell>\n"
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
    public String toUpdateEventXML(EventType eventType, ResourceUpdateSubType subEventType) {
        StringBuffer xmlToSend = new StringBuffer();
        xmlToSend.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlToSend.append("<tran eventId=\"" + UUID.randomUUID().toString() + "\">\n");
        xmlToSend.append("    <" + eventType.toString() + " schema=\"RESOURCE\">\n");
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

    private StringBuffer createValues(ResourceUpdateSubType subEventType) {
        StringBuffer values = new StringBuffer();
        if (subEventType.toString().equals("logical_delete")) {
            if (infoIsDeleted) {
                values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "Y" + "</cell>\n");
            }else {
                values.append("            <cell name=\"INFO_IS_DELETED\" type=\"CHAR\">" + "N" + "</cell>\n");
            }
        }
        if (subEventType.toString().equals("content_inherit_update")) {
            values.append("            <cell name=\"RESOURCE_HAS_CONTENT_INHERIT\" type=\"CHAR\">"+ (resourceHasContentInherit? "Y":"N") +"</cell>\n");
        }
        if (subEventType.toString().equals("structure_inherit_update")) {
            values.append("            <cell name=\"RESOURCE_HAS_STRUCTURE_INHERIT\" type=\"CHAR\">"+ (resourceHasStructureInherit ? "Y":"N") +"</cell>\n");
        }
        if (subEventType.toString().equals("type_id_update")) {
            values.append("            <cell name=\"RESOURCE_TYPE_ID\" type=\"NUMERIC\">"+ resourceTypeId +"</cell>\n");
        }
        if (subEventType.toString().equals("type_id_key_update")) {
            values.append("            <cell name=\"RESOURCE_TYPE_ID_KEY\" type=\"VARCHAR\">"+ resourceTypeIdKey +"</cell>\n");
        }
        return values;
    }

    private StringBuffer createOldValues() {
        StringBuffer values = new StringBuffer();
        values.append("            <cell name=\"RESOURCE_ID\" type=\"NUMERIC\">"+ resourceId +"</cell>\n");
        values.append("            <cell name=\"RESOURCE_TYPE_ID\" type=\"NUMERIC\">" + oldResourceTypeId +"</cell>\n");
        values.append("            <cell name=\"RESOURCE_TYPE_ID_KEY\" type=\"VARCHAR\">" + oldResourceTypeIdKey +"</cell>\n");
        values.append("            <cell name=\"RESOURCE_HAS_CONTENT_INHERIT\" type=\"CHAR\">" + (oldResourceHasContentInherit ? "Y":"N") +"</cell>\n");
        values.append("            <cell name=\"RESOURCE_HAS_STRUCTURE_INHERIT\" type=\"CHAR\">"+ (oldResourceHasStructureInherit ? "Y":"N") +"</cell>\n");
        return values;
    }
}