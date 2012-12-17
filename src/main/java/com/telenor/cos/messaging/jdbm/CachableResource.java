package com.telenor.cos.messaging.jdbm;

import java.io.Serializable;

import com.telenor.cos.messaging.CosMessagingException;

/**
 * Domain object for Resource, a subset of the RESOURCE table in FKM.
 *
 * @author Babaprakash D
 *
 */
public class CachableResource implements Serializable, Cloneable {

    private static final long serialVersionUID = -6586199085225205410L;

    private Long resourceId;

    private Integer resourceTypeId;

    private String resourceTypeIdKey;

    private String resourceHasContentInherit;

    private String resourceHasStructureInherit;

    /**
     * CachableResource Constrcutor.
     *
     * @param resourceId
     *            resourceId.
     */
    public CachableResource(Long resourceId) {
        super();
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

    public String getResourceHasContentInherit() {
        return resourceHasContentInherit;
    }

    public void setResourceHasContentInherit(String resourceHasContentInherit) {
        this.resourceHasContentInherit = resourceHasContentInherit;
    }

    public String getResourceHasStructureInherit() {
        return resourceHasStructureInherit;
    }

    public void setResourceHasStructureInherit(String resourceHasStructureInherit) {
        this.resourceHasStructureInherit = resourceHasStructureInherit;
    }

    @Override
    public String toString() {
        return "CachableResource [resourceId=" + resourceId + ", resourceTypeId=" + resourceTypeId + ", resourceTypeIdKey="
                + resourceTypeIdKey + ", resourceHasContentInherit=" + resourceHasContentInherit
                + ", resourceHasStructureInherit=" + resourceHasStructureInherit + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CachableResource other = (CachableResource) obj;
        if (resourceId == null) {
            if (other.resourceId != null) {
                return false;
            }
        } else if (!resourceId.equals(other.resourceId)) {
            return false;
        }
        return true;
    }

    /**
     * Clones the object
     *
     * @return CachableResource a clone
     */
    protected CachableResource clone() {
        try {
            return (CachableResource) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CosMessagingException("Problem occured with Clone() method in CachableResource", e);
        }
    }
}