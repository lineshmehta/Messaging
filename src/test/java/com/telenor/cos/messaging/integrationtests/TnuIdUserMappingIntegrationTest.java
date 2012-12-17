package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.usermapping.TnuIdUserMappingNewEvent;
import com.telenor.cos.messaging.event.usermapping.TnuidUserMappingDeleteEvent;
import com.telenor.cos.test.category.IntegrationTest;

/**
 * IntegrationTest for {@link TnuIdUserMappingNewEvent}
 * @author Babaprakash D
 *
 */
@Category(IntegrationTest.class)
public class TnuIdUserMappingIntegrationTest extends CommonIntegrationTest {

    private static final String TNU_ID = "_1000030";
    private static final Integer APP_ID = 8;
    private static final String CS_USER_ID = "M0_100469591";

    private static final String NEW_USER_MAPPING_XML = "dataset/tnuIdUserMapping_new.xml";
    private static final String DELETE_USER_MAPPING_XML = "dataset/tnuIdUserMapping_delete.xml";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.TNUIDUSERMAPPING_NEW_TOPIC);
        getJms().receive(ExternalEndpoints.TNUIDUSERMAPPING_DELETE_TOPIC);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test(timeout = 30000)
    public void testNewTnuIdUserMappingEvent() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(NEW_USER_MAPPING_XML);
        TnuIdUserMappingNewEvent receivedEvent = (TnuIdUserMappingNewEvent)getJms().receive(ExternalEndpoints.TNUIDUSERMAPPING_NEW_TOPIC, correlationId);
        assertAction(receivedEvent, ACTION.CREATED);
        assertTnuIdUserMappingEvents(receivedEvent.getUserMapping().getTelenorUserId(),receivedEvent.getUserMapping().getApplicationId());
        assertEquals("Unexpected CosSecurity Id", CS_USER_ID ,receivedEvent.getUserMapping().getCosSecurityUserId());
    }

    @Test(timeout = 30000)
    public void testConsumeDeleteTnuIdUserMappingEvent() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(DELETE_USER_MAPPING_XML);
        TnuidUserMappingDeleteEvent receivedEvent = (TnuidUserMappingDeleteEvent)getJms().receive(ExternalEndpoints.TNUIDUSERMAPPING_DELETE_TOPIC, correlationId);
        assertAction(receivedEvent, ACTION.DELETE);
        assertTnuIdUserMappingEvents(receivedEvent.getTelenorUserId(),receivedEvent.getApplicationId());
    }
    private void assertTnuIdUserMappingEvents(String tnuId,Integer appId) {
        assertEquals("Unexpected TnuId", TNU_ID, tnuId);
        assertEquals("Unexpected AppId", APP_ID, appId); 
    }
}
