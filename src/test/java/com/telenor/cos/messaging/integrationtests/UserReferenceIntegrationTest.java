package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.userref.InvoiceReferenceUpdateEvent;
import com.telenor.cos.messaging.event.userref.UserReferenceDescriptionUpdateEvent;
import com.telenor.cos.messaging.event.userref.UserReferenceLogicalDeleteEvent;
import com.telenor.cos.messaging.event.userref.UserReferenceNewEvent;
import com.telenor.cos.messaging.util.UserReferenceTestHelper;
import com.telenor.cos.test.category.IntegrationTest;

/**
 * Integration Test for UserReference Events.
 * @author Babaprakash D
 *
 */
@Category(IntegrationTest.class)
public class UserReferenceIntegrationTest extends CommonIntegrationTest {

    private final static Long SUBSCRIPTION_ID = Long.valueOf("32870370");
    private final static String NUMBER_TYPE = "ES";
    private final static String INVOICE_REF = "Test";
    private final static String USER_REF_DESCR = "TEST REF 2";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.USER_REFERENCE_NEW_TOPIC);
        getJms().receive(ExternalEndpoints.USER_REFERENCE_INVOICE_CHANGE_TOPIC);
        getJms().receive(ExternalEndpoints.USER_REFERENCE_DESCR_CHANGE_TOPIC);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test(timeout = 10000)
    public void testNewUserReferenceIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(UserReferenceTestHelper.USERREF_NEW_XML);
        UserReferenceNewEvent userReferenceNewEvent = (UserReferenceNewEvent)getJms().receive(ExternalEndpoints.USER_REFERENCE_NEW_TOPIC, correlationId);
        assertAction(userReferenceNewEvent, ACTION.CREATED);
        assertEquals("Unexpected Subscription Id",Long.valueOf(1),userReferenceNewEvent.getDomainId());
        assertEquals("Unexpected invoiceRef","TEST EINVOICE REF",userReferenceNewEvent.getUserReference().getInvoiceRef());
        assertEquals("Unexpected NumberType",NUMBER_TYPE,userReferenceNewEvent.getUserReference().getNumberType());
        assertEquals("Unexpected UserReferenceDescr","TEST REF 1",userReferenceNewEvent.getUserReference().getUserRefDescr());
    }

    @Test(timeout = 10000)
    public void invoiceReferenceUpdateIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(UserReferenceTestHelper.INVOICEREF_UPDATE_XML);
        InvoiceReferenceUpdateEvent invoiceRefUpdateEvent = (InvoiceReferenceUpdateEvent)getJms().receive(ExternalEndpoints.USER_REFERENCE_INVOICE_CHANGE_TOPIC, correlationId);
        assertAction(invoiceRefUpdateEvent, ACTION.INVOICE_CHANGE);
        assertEquals("Unexpected Subscription Id",SUBSCRIPTION_ID,invoiceRefUpdateEvent.getDomainId());
        assertEquals("Unexpected invoiceRef",INVOICE_REF,invoiceRefUpdateEvent.getInvoiceRef());
    }

    @Test(timeout = 10000)
    public void userReferenceDescrUpdateIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(UserReferenceTestHelper.USERREF_DESC_UPDATE_XML);
        UserReferenceDescriptionUpdateEvent userRefDescrUpdateEvent = (UserReferenceDescriptionUpdateEvent)getJms().receive(ExternalEndpoints.USER_REFERENCE_DESCR_CHANGE_TOPIC, correlationId);
        assertAction(userRefDescrUpdateEvent, ACTION.USERREF_DESC_CHG);
        assertEquals("Unexpected Subscription Id",SUBSCRIPTION_ID,userRefDescrUpdateEvent.getDomainId());
        assertEquals("Unexpected userRefDescr",USER_REF_DESCR,userRefDescrUpdateEvent.getUserRefDescr());
    }

    @Test(timeout = 10000)
    public void logicalDeleteIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(UserReferenceTestHelper.LOGICAL_DELETE_XML);
        UserReferenceLogicalDeleteEvent userReferenceLogicalDeleteEvent = (UserReferenceLogicalDeleteEvent)getJms().receive(ExternalEndpoints.USER_REFERENCE_LOGICAL_DELETE_TOPIC, correlationId);
        assertAction(userReferenceLogicalDeleteEvent, ACTION.LOGICAL_DELETE);
        assertEquals("Unexpected Subscription Id",SUBSCRIPTION_ID,userReferenceLogicalDeleteEvent.getDomainId());
        assertEquals("Unexpected Number Type","TM",userReferenceLogicalDeleteEvent.getNumberType());
    }
}