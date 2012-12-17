package com.telenor.cos.messaging.integrationtests;


import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNameChangeEvent;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNewEvent;
import com.telenor.cos.test.category.IntegrationTest;

@Category(IntegrationTest.class)
public class MasterCustomerRouterIntegrationTest extends CommonIntegrationTest {

    private final static Long MASTER_ID = Long.valueOf("369");
    private static final String MC_FIRST_NAME = "ELLEN ANNE";
    private static final String MC_MIDDLE_NAME = "";
    private static final String MC_LAST_NAME = "TOLLAN";
    private static final Long MC_ORG_NUMBER = Long.valueOf(958064853);

    private static final String NEW_MC_XML = "dataset/mastercustomer_new.xml";
    private static final String MC_LOGICALDELETE_XML = "dataset/mastercustomer_logical_delete.xml";
    private static final String MASTERCUSTOMER_NAMECHANGE_XML = "dataset/mastercustomer_namechange.xml";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.MASTERCUSTOMER_NEW);
        getJms().receive(ExternalEndpoints.MASTERCUSTOMER_LOGICAL_DELETE);
        getJms().receive(ExternalEndpoints.MASTERCUSTOMER_NAME_CHANGE);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test
    public void masterCustomerNewEvent() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(NEW_MC_XML);
        MasterCustomerNewEvent consumedMasterCustomerNewEvent = (MasterCustomerNewEvent) getJms().receive(ExternalEndpoints.MASTERCUSTOMER_NEW, correlationId);
        assertMasterCustomerEvent(consumedMasterCustomerNewEvent, ACTION.CREATED, MASTER_ID);
        assertEquals("Unexpected Master customer FirstName",MC_FIRST_NAME,consumedMasterCustomerNewEvent.getMasterCustomer().getFirstName());
        assertEquals("Unexpected Master customer MiddleName",MC_MIDDLE_NAME,consumedMasterCustomerNewEvent.getMasterCustomer().getMiddleName());
        assertEquals("Unexpected Master customer LastName",MC_LAST_NAME,consumedMasterCustomerNewEvent.getMasterCustomer().getLastName());
        assertEquals("Unexpected Master customer Organisation Number",MC_ORG_NUMBER,consumedMasterCustomerNewEvent.getMasterCustomer().getOrganizationNumber());
    }

    @Test(timeout = 30000)
    public void consumeLogicalDeletedMasterCustomerMessage() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(MC_LOGICALDELETE_XML);
        MasterCustomerLogicalDeleteEvent consumedMasterCustomerEvent = (MasterCustomerLogicalDeleteEvent) getJms().receive(ExternalEndpoints.MASTERCUSTOMER_LOGICAL_DELETE, correlationId);
        assertMasterCustomerEvent(consumedMasterCustomerEvent, ACTION.LOGICAL_DELETE, MASTER_ID);
    }

    @Test(timeout = 30000)
    public void masterCustomerNameChangeEventIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(MASTERCUSTOMER_NAMECHANGE_XML);
        MasterCustomerNameChangeEvent consumedMasterCustomerNameChangeEvent = (MasterCustomerNameChangeEvent) getJms().receive(ExternalEndpoints.MASTERCUSTOMER_NAME_CHANGE, correlationId);
        assertMasterCustomerEvent(consumedMasterCustomerNameChangeEvent, ACTION.NAME_CHANGE, MASTER_ID);
        assertEquals("Unexpected Master customer FirstName", MC_FIRST_NAME, consumedMasterCustomerNameChangeEvent.getMasterCustomerName().getFirstName());
        assertEquals("Unexpected Master customer MiddleName","", consumedMasterCustomerNameChangeEvent.getMasterCustomerName().getMiddleName());
        assertEquals("Unexpected Master customer LastName", MC_LAST_NAME, consumedMasterCustomerNameChangeEvent.getMasterCustomerName().getLastName());
    }

    private void assertMasterCustomerEvent(Event actualMasterCustomerEvent, ACTION expectedAction, Long expectedMasterId) {
        assertAction(actualMasterCustomerEvent, expectedAction);
        assertEquals("Unexpected master id", expectedMasterId, actualMasterCustomerEvent.getDomainId());
    }
}
