package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureDeleteEvent;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureNewEvent;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureUpdateEvent;
import com.telenor.cos.test.category.IntegrationTest;

/**
 * @author t798435
 */
@Category(IntegrationTest.class)
public class MasterStructureRouterIntegrationTest extends CommonIntegrationTest {

    private static final Long MASTERID_MEMBER = Long.valueOf("666");
    private static final Long NEW_MASTERID_OWNER = Long.valueOf("888");
    private static final Long OLD_MASTERID_OWNER = Long.valueOf("777");

    private static final String NEW_MASTER_STRUCTURE_XML = "dataset/masterstructure_new.xml";
    private static final String UPDATE_MASTER_STRUCTURE_XML = "dataset/masterstructure_update.xml";
    private static final String DELETE_MASTER_STRUCTURE_XML = "dataset/masterstructure_delete.xml";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.MASTERSTRUCTURE_NEW);
        getJms().receive(ExternalEndpoints.MASTERSTRUCTURE_DELETE);
        getJms().receive(ExternalEndpoints.MASTERSTRUCTURE_UPDATE);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    /**
     * Integration Test for MasterStructure New Event.
     * @throws Exception
     */
    @Test(timeout = 30000)
    public void masterStructureNewEventIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(NEW_MASTER_STRUCTURE_XML);
        MasterStructureNewEvent consumedMasterStructureEvent = (MasterStructureNewEvent) getJms().receive(ExternalEndpoints.MASTERSTRUCTURE_NEW, correlationId);
        assertMasterStructureEvent(consumedMasterStructureEvent,ACTION.CREATED,MASTERID_MEMBER);
        assertEquals("Unexpected MasterIdOwner", OLD_MASTERID_OWNER, consumedMasterStructureEvent.getMasterStructure().getMasterIdOwner());
    }

    /**
     * Integration Test for MasterStructure Update Event.
     * @throws Exception
     */
    @Test(timeout = 30000)
    public void masterStructureUpdateEventIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(UPDATE_MASTER_STRUCTURE_XML);
        MasterStructureUpdateEvent consumedMasterStructureUpdateEvent = (MasterStructureUpdateEvent) getJms().receive(ExternalEndpoints.MASTERSTRUCTURE_UPDATE, correlationId);
        assertMasterStructureEvent(consumedMasterStructureUpdateEvent,ACTION.UPDATED,MASTERID_MEMBER);
        assertEquals("Unexpected MasterIdOwner", NEW_MASTERID_OWNER, consumedMasterStructureUpdateEvent.getMasterStructure().getMasterIdOwner());
    }

    /**
     * Integration Test for Master Structure Delete Event.
     * @throws Exception
     */
    @Test(timeout = 30000)
    public void masterStructureDeleteEventIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(DELETE_MASTER_STRUCTURE_XML);
        MasterStructureDeleteEvent consumedMasterStructureDeleteEvent = (MasterStructureDeleteEvent) getJms().receive(ExternalEndpoints.MASTERSTRUCTURE_DELETE, correlationId);
        assertMasterStructureEvent(consumedMasterStructureDeleteEvent,ACTION.DELETE,MASTERID_MEMBER);
    }

    private void assertMasterStructureEvent(Event actualMasterStructureEvent, ACTION expectedAction, Long expectedMasterId) {
        assertAction(actualMasterStructureEvent,expectedAction);
        assertEquals("Unexpected master id", expectedMasterId, actualMasterStructureEvent.getDomainId());
    }
}
