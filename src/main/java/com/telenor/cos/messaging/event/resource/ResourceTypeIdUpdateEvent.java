package com.telenor.cos.messaging.event.resource;

import java.util.List;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;

/**
 * Event for RESOURCE_TYPE_ID column change in RESOURCE table.
 * @author Babaprakash D
 *
 */
public class ResourceTypeIdUpdateEvent extends Event {

    private static final long serialVersionUID = 757012919212269540L;

    private Resource resource;
    private Integer resourceTypeId;
    private List<String> csUserIdsList;

    /**
     * @param resource resource.
     * @param csUserIdsList list of csUserIds has access to resourceId.
     * @param newResourceTypeId resourceTypeId.
     */
    public ResourceTypeIdUpdateEvent(Resource resource,List<String> csUserIdsList,Integer newResourceTypeId) {
        super(resource.getResourceId(),ACTION.TYPE_ID_UPDATE,TYPE.RESOURCE);
        this.resource = resource;
        this.resourceTypeId = newResourceTypeId;
        this.csUserIdsList = csUserIdsList;
    }

    public Resource getResource() {
        return resource;
    }

    public Integer getResourceTypeId() {
        return resourceTypeId;
    }

    public List<String> getCsUserIdsList() {
        return csUserIdsList;
    }
}
