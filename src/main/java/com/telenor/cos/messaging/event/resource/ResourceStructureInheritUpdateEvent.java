package com.telenor.cos.messaging.event.resource;

import java.util.List;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;

/**
 * Event for RESOURCE_HAS_STRUCTURE_INHERIT column change in RESOURCE table.
 * @author Babaprakash D
 *
 */
public class ResourceStructureInheritUpdateEvent extends Event {

    /**
     * 
     */
    private static final long serialVersionUID = 7259231460116314043L;

    private Resource resource;
    private boolean newResourceStructureInherit;
    private List<String> csUserIdsList;

    /**
     * Constructor of ResourceStructureInheritUpdateEvent.
     * @param resource resource.
     * @param newResourceStructureInherit resourceHasStructureInherit.
     * @param csUserIdsList list of csUserIds has access to resourceId.
     */
    public ResourceStructureInheritUpdateEvent(Resource resource,List<String> csUserIdsList, boolean newResourceStructureInherit) {
        super(resource.getResourceId(), ACTION.STRUCTURE_INHERIT_UPDATE, TYPE.RESOURCE);
        this.resource = resource;
        this.newResourceStructureInherit = newResourceStructureInherit;
        this.csUserIdsList = csUserIdsList;
    }

    public Resource getResource() {
        return resource;
    }

    public boolean getResourceHasStructureInherit() {
        return newResourceStructureInherit;
    }

    public List<String> getCsUserIdsList() {
        return csUserIdsList;
    }
}
