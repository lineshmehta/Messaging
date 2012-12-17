package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceContentInheritUpdateEvent;
import com.telenor.cos.messaging.event.resource.ResourceLogicalDeleteEvent;
import com.telenor.cos.messaging.event.resource.ResourceNewEvent;
import com.telenor.cos.messaging.event.resource.ResourceStructureInheritUpdateEvent;
import com.telenor.cos.messaging.event.resource.ResourceTypeIdKeyUpdateEvent;
import com.telenor.cos.messaging.event.resource.ResourceTypeIdUpdateEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceNewEvent;
import com.telenor.cos.test.category.IntegrationTest;

/**
 * Integration Test for Resource Events.
 * @author Babaprakash D
 *
 */
@Category(IntegrationTest.class)
public class ResourceRouterIntegrationTest extends CommonIntegrationTest {

    private static final String NEW_USER_RESOURCE_XML = "dataset/userResource_new.xml";
    private static final String RESOURCE_NEW_XML = "dataset/resource_new.xml";
    private static final String RESOURCE_LOGICAL_DELETE_XML = "dataset/resource_logicaldelete.xml";
    private static final String RESOURCE_TYPE_ID_CHANGE_XML = "dataset/resource_typeid_change.xml";
    private static final String RESOURCE_TYPE_ID_KEY_CHANGE_XML = "dataset/resource_typeid_key_change.xml";
    private static final String RESOURCE_INHERIT_CONTENT_CHANGE_XML = "dataset/resource_inheritcontent_change.xml";
    private static final String RESOURCE_INHERIT_STRUCTURE_CHANGE_XML = "dataset/resource_inheritstructure_change.xml";
    private static final String MASTER_CUSTOMER_NEW = "dataset/mastercustomer_new.xml";
    private static final String RESOURCE_NEW_KURTID = "dataset/resource_new_kurtId.xml";
    private static final String RESOURCE_NEW_CUN = "dataset/resource_new_cun.xml";

    private static final Long RESOURCE_ID = Long.valueOf(456);
    private static final Integer RESOURCE_TYPE_ID = 4;
    private static final String RESOURCE_TYPE_ID_KEY = "369";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.RESOURCE_NEW_TOPIC);
        getJms().receive(ExternalEndpoints.RESOURCE_LOGICAL_DELETE_TOPIC);
        getJms().receive(ExternalEndpoints.RESOURCE_TYPE_ID_UPDATE_TOPIC);
        getJms().receive(ExternalEndpoints.RESOURCE_TYPE_ID_KEY_UPDATE_TOPIC);
        getJms().receive(ExternalEndpoints.RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC);
        getJms().receive(ExternalEndpoints.RESOURCE_STRUCTURE_INHERIT_UPDATE_TOPIC);
        getJms().receive(ExternalEndpoints.USERRESOURCE_NEW_TOPIC);
        getJms().setReceiveTimeout(3000L);
    }

    @Test(timeout = 40000)
    public void testResourceNewEventAndDataInsertedToResourceCache() throws Exception {
        Resource resource = sendResourceNewXMLAndGetResourceFromEvent(RESOURCE_NEW_XML);
        assertResource(resource,RESOURCE_ID,RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        assertResource(resource,Boolean.FALSE,Boolean.TRUE);

        Resource resourceFromCache = assertActionAndGetResourceFromCache();
        assertResource(resourceFromCache,RESOURCE_ID,RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        assertResource(resourceFromCache,Boolean.FALSE,Boolean.TRUE);
    }

    @Test(timeout = 10000)
    public void testResourceLogicalDeleteEventAndDataInsertedToResourceCache() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_NEW_XML);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_LOGICAL_DELETE_XML);

        ResourceLogicalDeleteEvent consumedLogicalDeleteEvent = (ResourceLogicalDeleteEvent)getJms().receive(ExternalEndpoints.RESOURCE_LOGICAL_DELETE_TOPIC, correlationId);

        assertAction(consumedLogicalDeleteEvent, ACTION.LOGICAL_DELETE);
        assertEquals(consumedLogicalDeleteEvent.getDomainId(), RESOURCE_ID);
    }

    @Test(timeout = 10000)
    public void testResourceContentInheritUpdateEventAndDataInsertedToResourceCache() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_NEW_XML);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_INHERIT_CONTENT_CHANGE_XML);

        ResourceContentInheritUpdateEvent consumedResourceContentInheritUpdateEvent = (ResourceContentInheritUpdateEvent)getJms().receive(ExternalEndpoints.RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC, correlationId);
        assertAction(consumedResourceContentInheritUpdateEvent, ACTION.CONTENT_INHERIT_UPDATE);

        Resource oldResource = consumedResourceContentInheritUpdateEvent.getResource();

        //Assert New ResourceHasContentInherit Value.
        assertResourceContentInherit(Boolean.TRUE, consumedResourceContentInheritUpdateEvent.getResourceHasContentInherit());

        //Assert Old Value of Resource.
        assertResource(oldResource,RESOURCE_ID,RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        assertResource(oldResource,Boolean.FALSE,Boolean.TRUE);

        /**
         * Below Code Tests, Data Inserted into Resource Cache.
         */
        Resource resourceFromCache = assertActionAndGetResourceFromCache();

        //Assert UserResource New Event.
        assertResource(resourceFromCache,RESOURCE_ID,RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        assertResource(resourceFromCache,Boolean.TRUE,Boolean.TRUE);
    }

    @Test(timeout = 10000)
    public void testResourceStructureInheritUpdateEventAndDataInsertedToResourceCache() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_NEW_XML);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_INHERIT_STRUCTURE_CHANGE_XML);

        ResourceStructureInheritUpdateEvent consumedResourceStructureInheritUpdateEvent = (ResourceStructureInheritUpdateEvent)getJms().receive(ExternalEndpoints.RESOURCE_STRUCTURE_INHERIT_UPDATE_TOPIC, correlationId);

        assertAction(consumedResourceStructureInheritUpdateEvent, ACTION.STRUCTURE_INHERIT_UPDATE);

        Resource oldResource = consumedResourceStructureInheritUpdateEvent.getResource();

        //Assert New ResourceHasStructureInherit Value.
        assertResourceStructureInherit(Boolean.FALSE,consumedResourceStructureInheritUpdateEvent.getResourceHasStructureInherit());

        //Assert Old Value of Resource.
        assertResource(oldResource,RESOURCE_ID,RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        assertResource(oldResource,Boolean.FALSE,Boolean.TRUE);

        Resource resourceFromCache = assertActionAndGetResourceFromCache();

        //Assert UserResource New Event.
        assertResource(resourceFromCache,RESOURCE_ID,RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        assertResource(resourceFromCache,Boolean.FALSE,Boolean.FALSE);
    }

    @Test(timeout = 10000)
    public void testResourceTypeIdKeyUpdateEventAndDataInsertedToResourceCache() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_NEW_XML);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_TYPE_ID_KEY_CHANGE_XML);

        ResourceTypeIdKeyUpdateEvent consumedResourceTypeIdKeyUpdateEvent = (ResourceTypeIdKeyUpdateEvent)getJms().receive(ExternalEndpoints.RESOURCE_TYPE_ID_KEY_UPDATE_TOPIC, correlationId);

        Resource oldResource = consumedResourceTypeIdKeyUpdateEvent.getResource();

        assertAction(consumedResourceTypeIdKeyUpdateEvent,ACTION.TYPE_ID_KEY_UPDATE);

        //Assert NewResourceTypeId Key.
        assertResourceTypeIdKey("366",consumedResourceTypeIdKeyUpdateEvent.getResourceTypeIdKey());

        //Assert Old Value of Resource.
        assertResource(oldResource,RESOURCE_ID,RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        assertResource(oldResource,Boolean.FALSE,Boolean.TRUE);

        Resource resourceFromCache = assertActionAndGetResourceFromCache();

        //Assert UserResource New Event.
        assertResource(resourceFromCache,RESOURCE_ID,RESOURCE_TYPE_ID,"366");
        assertResource(resourceFromCache,Boolean.FALSE,Boolean.TRUE);
    }

    @Test(timeout = 10000)
    public void testResourceTypeIdUpdateEventAndDataInsertedToResourceCache() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_NEW_XML);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(RESOURCE_TYPE_ID_CHANGE_XML);

        ResourceTypeIdUpdateEvent consumedResourceTypeIdUpdateEvent = (ResourceTypeIdUpdateEvent)getJms().receive(ExternalEndpoints.RESOURCE_TYPE_ID_UPDATE_TOPIC, correlationId);
        Resource oldResource = consumedResourceTypeIdUpdateEvent.getResource();

        //Assert New Resource Type Id.
        assertResourceTypeId(Integer.valueOf(5), consumedResourceTypeIdUpdateEvent.getResourceTypeId());
        assertAction(consumedResourceTypeIdUpdateEvent,ACTION.TYPE_ID_UPDATE);

        //Assert Old Value of Resource.
        assertResource(oldResource,RESOURCE_ID,RESOURCE_TYPE_ID,RESOURCE_TYPE_ID_KEY);
        assertResource(oldResource,Boolean.FALSE,Boolean.TRUE);

        Resource resourceFromCache = assertActionAndGetResourceFromCache();

        //Assert UserResource New Event.
        assertResource(resourceFromCache,RESOURCE_ID,Integer.valueOf(5),RESOURCE_TYPE_ID_KEY);
        assertResource(resourceFromCache, Boolean.FALSE,Boolean.TRUE);
    }

    /**
     * Following two test cases are added to test KurtIdCache and MasterCustomerCache. KurtId Cache will be accessed when resourceTypeId is 7 i.e KurtId.
     * MasterCustomerCache will be accessed when resourceTypeId is 6 i.e organization number.
     */
    @Test(timeout = 10000)
    public void testKurtIdCacheWithResourceNewEvent() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(MASTER_CUSTOMER_NEW);
        Resource resource = sendResourceNewXMLAndGetResourceFromEvent(RESOURCE_NEW_KURTID);
        assertResource(resource,RESOURCE_ID,RESOURCE_TYPE_ID,"369");
        assertResource(resource,Boolean.FALSE,Boolean.TRUE);
    }

    @Test(timeout = 10000)
    public void testMasterCustomerCacheWithResourceNewEvent() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(MASTER_CUSTOMER_NEW);
        Resource resource = sendResourceNewXMLAndGetResourceFromEvent(RESOURCE_NEW_CUN);
        assertResource(resource,RESOURCE_ID,RESOURCE_TYPE_ID,"369");
        assertResource(resource,Boolean.FALSE,Boolean.TRUE);
    }

    private Resource sendResourceNewXMLAndGetResourceFromEvent(String resourceXmlName) throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(resourceXmlName);
        ResourceNewEvent consumedResourceNewevent = (ResourceNewEvent)getJms().receive(ExternalEndpoints.RESOURCE_NEW_TOPIC, correlationId);
        assertAction(consumedResourceNewevent,ACTION.CREATED);
        Resource resource = consumedResourceNewevent.getResource();
        return resource;
    }

    private Resource assertActionAndGetResourceFromCache() throws Exception {
        String correlationIdOfUserResource = sendXmlToRepServerQueueAndReturnCorrelationId(NEW_USER_RESOURCE_XML);
        UserResourceNewEvent userResourceNewEvent = (UserResourceNewEvent)getJms().receive(ExternalEndpoints.USERRESOURCE_NEW_TOPIC, correlationIdOfUserResource);
        assertAction(userResourceNewEvent, ACTION.CREATED);
        return  userResourceNewEvent.getUserResource().getResource();
    }

    private void assertResource(Resource resourceFromCache,Long resourceId,Integer resourceTypeId,String resourceTypeIdKey) {
        assertResourceId(resourceFromCache, resourceId);
        assertResourceTypeId(resourceTypeId, resourceFromCache.getResourceTypeId());
        assertResourceTypeIdKey(resourceTypeIdKey, resourceFromCache.getResourceTypeIdKey());
    }

    private void assertResource(Resource resourceFromCache,boolean contentInherit,boolean structureInherit) {
        assertResourceContentInherit(contentInherit, resourceFromCache.getResourceHasContentInherit());
        assertResourceStructureInherit(structureInherit, resourceFromCache.getResourceHasStructureInherit());
    }

    private void assertResourceId(Resource resource,Long extpectecResourceId) {
        assertNotNull(resource);
        assertEquals("Unexpected ResourceId",extpectecResourceId, resource.getResourceId());
    }
    private void assertResourceTypeId(Integer expectedResourceTypeId,Integer actualResourceTypeId) {
        assertEquals("Unexpected ResourceTypeId",expectedResourceTypeId,actualResourceTypeId);
    }
    private void assertResourceTypeIdKey(String expectedResourceTypeIdKey,String actualResourceTypeIdKey) {
        assertEquals("Unexpected ResourceTypeId Key",expectedResourceTypeIdKey,actualResourceTypeIdKey);
    }
    private void assertResourceContentInherit(boolean expectedResourceContentInherit,boolean actualResourceContentInherit) {
        assertEquals("Unexpected Resource ContentInherit",expectedResourceContentInherit,actualResourceContentInherit);
    }
    private void assertResourceStructureInherit(boolean expectedResourceStructureInherit,boolean actualResourceStructureInherit) {
        assertEquals("Unexpected Resource StructureInherit",expectedResourceStructureInherit,actualResourceStructureInherit);
    }
}
