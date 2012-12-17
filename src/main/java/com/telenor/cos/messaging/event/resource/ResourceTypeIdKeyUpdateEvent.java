package com.telenor.cos.messaging.event.resource;

import java.util.List;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;

/**
 * Event for RESOURCE_TYPE_ID_KEY column change in RESOURCE table.
 * @author Babaprakash D
 *
 */
public class ResourceTypeIdKeyUpdateEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = -4943988301250204914L;

    private Resource resource;
    private String newResourceTypeIdKey;
    private List<String> csUserIdsList;

    /**
     * Constructor of ResourceTypeIdKeyUpdateEvent.
     * @param resource  resource.
     * @param csUserIdsList list of csUserIds has access to resourceId.
     * @param newResourceTypeIdKey resourceTypeIdKey.
     */
    public ResourceTypeIdKeyUpdateEvent(Resource resource,List<String> csUserIdsList,String newResourceTypeIdKey) {
        super(resource.getResourceId(), ACTION.TYPE_ID_KEY_UPDATE, TYPE.RESOURCE);
        this.resource = resource;
        this.newResourceTypeIdKey = newResourceTypeIdKey;
        this.csUserIdsList = csUserIdsList;
    }

    public Resource getResource() {
        return resource;
    }

    public String getResourceTypeIdKey() {
        return newResourceTypeIdKey;
    }

    public List<String> getCsUserIdsList() {
        return csUserIdsList;
    }
}
