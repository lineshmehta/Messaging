package com.telenor.cos.messaging.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.resource.ResourceContentInheritUpdateEvent;
import com.telenor.cos.messaging.event.resource.ResourceLogicalDeleteEvent;
import com.telenor.cos.messaging.event.resource.ResourceNewEvent;
import com.telenor.cos.messaging.event.resource.ResourceStructureInheritUpdateEvent;
import com.telenor.cos.messaging.event.resource.ResourceTypeIdKeyUpdateEvent;
import com.telenor.cos.messaging.event.resource.ResourceTypeIdUpdateEvent;

/**
 * Test case for Resource Events.
 * @author Babaprakash D
 *
 */
public class ResourceEventTest extends AbstractEventTest {

    public static final Long RESOURCE_ID = Long.valueOf(1234);
    private Resource resource = new Resource(RESOURCE_ID);
    public static final List<String> CSUSER_IDSLIST = new ArrayList<String>();

    @Test
    public void testNewResourceEvent() {
        ResourceNewEvent resourceNewEvent = new ResourceNewEvent(resource);
        assertActionAndType(resourceNewEvent,ACTION.CREATED,TYPE.RESOURCE);
        assertEquals("Unexpected Resource Id",RESOURCE_ID,resourceNewEvent.getDomainId());
    }

    @Test
    public void testLogicalDeleteResourceEvent() {
        ResourceLogicalDeleteEvent resourceLogicalDeleteEvent = new ResourceLogicalDeleteEvent(RESOURCE_ID);
        assertActionAndType(resourceLogicalDeleteEvent,ACTION.LOGICAL_DELETE,TYPE.RESOURCE);
        assertEquals("Unexpected Resource Id",RESOURCE_ID,resourceLogicalDeleteEvent.getDomainId());
    }

    @Test
    public void testResourceContentInheritUpdateEvent() {
        ResourceContentInheritUpdateEvent resourceContentInheritUpdateEvent = new ResourceContentInheritUpdateEvent(resource,CSUSER_IDSLIST,true);
        assertActionAndType(resourceContentInheritUpdateEvent,ACTION.CONTENT_INHERIT_UPDATE,TYPE.RESOURCE);
        assertTrue(resourceContentInheritUpdateEvent.getResourceHasContentInherit());
    }

    @Test
    public void testResourceStructureInheritUpdateEvent() {
        ResourceStructureInheritUpdateEvent resourceStructureInheritUpdateEvent = new ResourceStructureInheritUpdateEvent(resource,CSUSER_IDSLIST,false);
        assertActionAndType(resourceStructureInheritUpdateEvent,ACTION.STRUCTURE_INHERIT_UPDATE,TYPE.RESOURCE);
        assertFalse(resourceStructureInheritUpdateEvent.getResourceHasStructureInherit());
    }

    @Test
    public void testResourceTypeIdKeyUpdateEvent() {
        ResourceTypeIdKeyUpdateEvent resourceTypeIdKeyUpdateEvent = new ResourceTypeIdKeyUpdateEvent(resource,CSUSER_IDSLIST,"1");
        assertActionAndType(resourceTypeIdKeyUpdateEvent,ACTION.TYPE_ID_KEY_UPDATE,TYPE.RESOURCE);
        assertEquals("Unexpected TypeId Key","1",resourceTypeIdKeyUpdateEvent.getResourceTypeIdKey());
    }

    @Test
    public void testResourceTypeIdUpdateEvent() {
        ResourceTypeIdUpdateEvent resourceTypeIdUpdateEvent = new ResourceTypeIdUpdateEvent(resource,CSUSER_IDSLIST,1);
        assertActionAndType(resourceTypeIdUpdateEvent,ACTION.TYPE_ID_UPDATE,TYPE.RESOURCE);
        assertEquals("Unexpected TypeId Key",Integer.valueOf(1),resourceTypeIdUpdateEvent.getResourceTypeId());
    }
}
