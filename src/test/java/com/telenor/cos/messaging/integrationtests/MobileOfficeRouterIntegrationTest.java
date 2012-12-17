package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeDeleteEvent;
import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeNewEvent;
import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeUpdateEvent;
import com.telenor.cos.test.category.IntegrationTest;

@Category(IntegrationTest.class)
public class MobileOfficeRouterIntegrationTest extends CommonIntegrationTest {

    private static final String DIRECTORY_NUMBER = "111222555444";
    private static final String EXTENSION_NUMBER = "45678945";
    private static final String EXTENSION_NUMBER_OLD = "45678944";

    private static final String MOBILE_OFFICE_NEW_XML = "dataset/mobileoffice_new.xml";
    private static final String MOBILE_OFFICE_UPDATE_XML = "dataset/mobileoffice_update.xml";
    private static final String MOBILE_OFFICE_DELETE_XML = "dataset/mobileoffice_delete.xml";

    @Before
    public void beforeTest() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.MOBILE_OFFICE_NEW_TOPIC);
        getJms().receive(ExternalEndpoints.MOBILE_OFFICE_UPDATE_TOPIC);
        getJms().receive(ExternalEndpoints.MOBILE_OFFICE_DELETE_TOPIC);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test(timeout = 30000)
    public void testConsumerNewMobileOfficeEvent() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(MOBILE_OFFICE_NEW_XML);
        MobileOfficeNewEvent receivedEvent = (MobileOfficeNewEvent)getJms().receive(ExternalEndpoints.MOBILE_OFFICE_NEW_TOPIC, correlationId);
        assertAction(receivedEvent, ACTION.CREATED);
        assertExtensionAndDirectoryNumber(receivedEvent.getDirectoryNumber(),receivedEvent.getExtensionNumber());
    }

    @Test(timeout = 30000)
    public void testConsumerUpdateMobileOfficeEvent() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(MOBILE_OFFICE_UPDATE_XML);
        MobileOfficeUpdateEvent receivedEvent = (MobileOfficeUpdateEvent)getJms().receive(ExternalEndpoints.MOBILE_OFFICE_UPDATE_TOPIC, correlationId);
        assertAction(receivedEvent, ACTION.UPDATED);
        assertExtensionAndDirectoryNumber(receivedEvent.getDirectoryNumber(),receivedEvent.getExtensionNumber());
        assertEquals("Unexpected old(previous) extension number", EXTENSION_NUMBER_OLD, receivedEvent.getOldExtentionNumber());
    }

    @Test(timeout = 30000)
    public void testConsumerDeleteMobileOfficeEvent() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(MOBILE_OFFICE_DELETE_XML);
        MobileOfficeDeleteEvent receivedEvent = (MobileOfficeDeleteEvent)getJms().receive(ExternalEndpoints.MOBILE_OFFICE_DELETE_TOPIC, correlationId);
        assertAction(receivedEvent, ACTION.DELETE);
        assertEquals("Unexpected directory number", DIRECTORY_NUMBER, receivedEvent.getDirectoryNumber());
        assertEquals("Unexpected old(previous) extension number", EXTENSION_NUMBER_OLD, receivedEvent.getOldExtensionNumber());
    }

    private void assertExtensionAndDirectoryNumber(String directoryNumber, String extensionNumber) {
        assertEquals("Unexpected directory number", DIRECTORY_NUMBER, directoryNumber);
        assertEquals("Unexpected extension number", EXTENSION_NUMBER, extensionNumber);
    }
}
