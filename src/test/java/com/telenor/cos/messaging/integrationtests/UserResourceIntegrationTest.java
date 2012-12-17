package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.userresource.UserResourceCsUserIdUpdateEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceDeleteEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceNewEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceResourceIdUpdateEvent;
import com.telenor.cos.test.category.IntegrationTest;

/**
 * IntegrationTest for UserResource Events.
 * @author Babaprakash D
 *
 */
@Category(IntegrationTest.class)
public class UserResourceIntegrationTest extends CommonIntegrationTest {

    private static final String RESOURCE_NEW_XML = "dataset/resource_new.xml";
    private static final String RESOURCE_NEW_XML_TWO = "dataset/resource_new_foruserresource.xml";
    private static final String NEW_USER_RESOURCE_XML = "dataset/userResource_new.xml";
    private static final String DELETE_USER_RESOURCE_XML = "dataset/userResource_Delete.xml";
    private static final String USERRESOURCE_RESOURCEID_UPDATE_XML = "dataset/userResource_resourceIdUpdate.xml";
    private static final String USERRESOURCE_CSUSERID_UPDATE_XML = "dataset/userResource_csUserIdUpdate.xml";

    private static final String CS_USER_ID = "cosmaster";
    private static final Integer RESOURCE_TYPE_ID = 4;
    private static final String RESOURCE_TYPE_ID_KEY = "369";
    private static final Boolean INHERIT_CONTENT = Boolean.FALSE;
    private static final Boolean INHERIT_STRUCTURE = Boolean.TRUE;

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.USERRESOURCE_NEW_TOPIC);
        getJms().receive(ExternalEndpoints.USERRESOURCE_DELETE_TOPIC);
        getJms().receive(ExternalEndpoints.USERRESOURCE_RESOURCE_ID_UPDATE_TOPIC);
        getJms().receive(ExternalEndpoints.USERRESOURCE_CSUSER_ID_UPDATE_TOPIC);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test(timeout = 30000)
    public void testNewUserResourceIntegrationTest() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_NEW_XML);

        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(NEW_USER_RESOURCE_XML);
        UserResourceNewEvent receivedEvent = (UserResourceNewEvent)getJms().receive(ExternalEndpoints.USERRESOURCE_NEW_TOPIC, correlationId);

        assertAction(receivedEvent,ACTION.CREATED);
        assertUserResourceEvent(receivedEvent.getUserResource().getUserId());
        assertResourceData(receivedEvent.getUserResource().getResource());
    }

    @Test(timeout = 30000)
    public void testDeleteUserResourceIntegrationTest() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_NEW_XML);

        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(DELETE_USER_RESOURCE_XML);
        UserResourceDeleteEvent receivedEvent = (UserResourceDeleteEvent)getJms().receive(ExternalEndpoints.USERRESOURCE_DELETE_TOPIC, correlationId);

        assertAction(receivedEvent,ACTION.DELETE);
        assertUserResourceEvent(receivedEvent.getUserResource().getUserId());
        assertResourceData(receivedEvent.getUserResource().getResource());
    }

    @Test(timeout = 30000)
    public void resourceIdUpdateIntegrationTest() throws Exception {

        sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_NEW_XML);
        sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_NEW_XML_TWO);

        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(USERRESOURCE_RESOURCEID_UPDATE_XML);
        UserResourceResourceIdUpdateEvent receivedEvent = (UserResourceResourceIdUpdateEvent)getJms().receive(ExternalEndpoints.USERRESOURCE_RESOURCE_ID_UPDATE_TOPIC, correlationId);
        Resource oldResource = receivedEvent.getOldUserResource().getResource();

        assertAction(receivedEvent,ACTION.RESOURCE_ID_CHANGE);
        assertUserResourceEvent(receivedEvent.getNewUserResource().getUserId());
        assertEquals("Unexpected ResourceId Old", Long.valueOf(457), receivedEvent.getOldUserResource().getResource().getResourceId());
        assertEquals("Unexpected ResourceId New", Long.valueOf(456), receivedEvent.getNewUserResource().getResource().getResourceId());
        assertResourceData(receivedEvent.getNewUserResource().getResource());
        assertEquals("Unexpected Resource TypeId" ,Integer.valueOf(4),oldResource.getResourceTypeId());
        assertEquals("Unexpected Resource TypeId Key" ,"457",oldResource.getResourceTypeIdKey());
        assertEquals("Unexpected Resource Content Inherit" ,Boolean.FALSE,oldResource.getResourceHasContentInherit());
        assertEquals("Unexpected Resource Structure Inherit" ,Boolean.TRUE,oldResource.getResourceHasStructureInherit());
    }

    @Test(timeout = 30000)
    public void csUserIdUpdateIntegrationTest() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(NEW_USER_RESOURCE_XML);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(USERRESOURCE_CSUSERID_UPDATE_XML);
        UserResourceCsUserIdUpdateEvent receivedEvent = (UserResourceCsUserIdUpdateEvent)getJms().receive(ExternalEndpoints.USERRESOURCE_CSUSER_ID_UPDATE_TOPIC, correlationId);
        assertAction(receivedEvent,ACTION.CS_USERID_CHANGE);
        assertEquals("Unexpected csuserId", "cosmaster1", receivedEvent.getUserResource().getUserId());
        assertEquals("Unexpected ResourceId", Long.valueOf(456), receivedEvent.getUserResource().getResource().getResourceId());
    }

    private void assertUserResourceEvent(String csUserIdToCompare) {
        assertEquals("Unexpected csuserId", CS_USER_ID, csUserIdToCompare);
    }

    private void assertResourceData(Resource resource) {
        assertEquals("Unexpected Resource TypeId" ,RESOURCE_TYPE_ID,resource.getResourceTypeId());
        assertEquals("Unexpected Resource TypeId Key" ,RESOURCE_TYPE_ID_KEY,resource.getResourceTypeIdKey());
        assertEquals("Unexpected Resource Content Inherit" ,INHERIT_CONTENT,resource.getResourceHasContentInherit());
        assertEquals("Unexpected Resource Structure Inherit" ,INHERIT_STRUCTURE,resource.getResourceHasStructureInherit());
    }
}
