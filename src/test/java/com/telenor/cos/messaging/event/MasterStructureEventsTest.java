package com.telenor.cos.messaging.event;

import org.junit.Test;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureDeleteEvent;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureNewEvent;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureUpdateEvent;

/**
 * Test Case for MasterStructureEvents.
 * @author t798435
s */
public class MasterStructureEventsTest extends AbstractEventTest {

    /**
     * Tests whether TYPE and ACTION are set properly or not.
     */
    @Test
    public void createNewMasterStructureEvent() {
        MasterStructure masterStructure = new MasterStructure();
        masterStructure.setMasterId(1234L);
        masterStructure.setMasterIdOwner(5678L);
        Event event = new MasterStructureNewEvent(masterStructure);
        assertActionAndType(event,ACTION.CREATED,TYPE.MASTERSTRUCTURE);
    }

    @Test
    public void createMasterStructureUpdateEvent() {
        Long masterId = 1234L;
        MasterStructure masterStructure = new MasterStructure();
        masterStructure.setMasterId(masterId);
        masterStructure.setMasterIdOwner(5678L);
        Event event = new MasterStructureUpdateEvent(masterId, masterStructure);
        assertActionAndType(event,ACTION.UPDATED,TYPE.MASTERSTRUCTURE);
    }

    @Test
    public void createMasterStructureDeleteEvent() {
        Long masterId = 1234L;
        Event event = new MasterStructureDeleteEvent(masterId);
        assertActionAndType(event,ACTION.DELETE,TYPE.MASTERSTRUCTURE);
    }
}
