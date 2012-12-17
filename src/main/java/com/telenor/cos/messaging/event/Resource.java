package com.telenor.cos.messaging.event;

import java.io.Serializable;

/**
 * Domain Object of Resource.
 * @author Babaprakash D
 *
 */
public class Resource implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1L;

    private Long resourceId;

    private Integer resourceTypeId;

    private String resourceTypeIdKey;

    private boolean resourceHasContentInherit;

    private boolean resourceHasStructureInherit;

    /**
     * Constructor of Resource.
     * @param resourceId resourceId.
     */
    public Resource(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Integer resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getResourceTypeIdKey() {
        return resourceTypeIdKey;
    }

    public void setResourceTypeIdKey(String resourceTypeIdKey) {
        this.resourceTypeIdKey = resourceTypeIdKey;
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

    @Override
    public String toString() {
        return "Resource [resourceId=" + resourceId + ", resourceTypeId="
                + resourceTypeId + ", resourceTypeIdKey=" + resourceTypeIdKey
                + ", resourceHasContentInherit=" + resourceHasContentInherit
                + ", resourceHasStructureInherit="
                + resourceHasStructureInherit + "]";
    }
}
