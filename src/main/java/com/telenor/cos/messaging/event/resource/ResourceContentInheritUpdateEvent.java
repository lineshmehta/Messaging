package com.telenor.cos.messaging.event.resource;

import java.util.List;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;

/**
 * Event for RESOURCE_HAS_CONTENT_INHERIT column change in RESOURCE table.
 * @author Babaprakash D
 *
 */
public class ResourceContentInheritUpdateEvent extends Event {

    private static final long serialVersionUID = 5306282990362536029L;

    private Resource resource;
    private boolean newResourceContentInherit;
    private List<String> csUserIdsList;

    /**
     * Constructor of ResourceLogicalDeleteEvent.
     * @param resource resource.
     * @param newResourceContentInherit newResourceContentInherit.
     * @param csUserIdsList list of csUserIds has access to resourceId.
     */
    public ResourceContentInheritUpdateEvent(Resource resource,List<String> csUserIdsList,boolean newResourceContentInherit) {
        super(resource.getResourceId(), ACTION.CONTENT_INHERIT_UPDATE, TYPE.RESOURCE);
        this.resource = resource;
        this.newResourceContentInherit = newResourceContentInherit;
        this.csUserIdsList = csUserIdsList;
    }
    
    public Resource getResource() {
        return resource;
    }

    public boolean getResourceHasContentInherit() {
        return newResourceContentInherit;
    }

    public List<String> getCsUserIdsList() {
        return csUserIdsList;
    }
}
