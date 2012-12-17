package com.telenor.cos.messaging.integrationtests;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangeTypeEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangedAccountEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangedStatusEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionExpiredEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionLogicalDeleteEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionSecretNumberEvent;
import com.telenor.cos.test.category.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.Assert.assertEquals;

@Category(IntegrationTest.class)
public class SubscriptionTranslatorRouterIntegrationTest extends CommonIntegrationTest {

    private final static Long NEW_SUBSCRIPTION_ID = Long.valueOf("30868100");
    private final static Long LOGICAL_DELETED_SUBSCRIPTION_ID = Long.valueOf("32143317");
    private final static Long BARRED_SUBSCRIPTION_ID = Long.valueOf("12345678");
    private final static Long SUBSCRIPTION_ID = Long.valueOf("12345678");
    private final static Long EXPIRED_SUBSCRIPTION_ID = Long.valueOf("666");

    private final static Long CHANGED_ACCT_ID = Long.valueOf("3456");
    private final static Long CHANGED_SUBSCR_ID = Long.valueOf("3456789");
    private final static String SUBSCRIPTION_STATUS = "M";

    private final static String CUSTOMER_NEW = "dataset/customer_new.xml";
    private final static String SUBSCRIPTION_XML_INSERT = "dataset/NewSubscriptionTestInput.xml";
    private final static String SUBSCRIPTION_XML_LOGICAL_DELETE = "dataset/subscription_logical_delete.xml";
    private final static String SUBSCRIPTION_XML_EXPIRED = "dataset/expired_subscription.xml";
    private final static String SUBSCRIPTION_XML_CHANGED_ACCOUNT = "dataset/changedaccount_subscription.xml";
    private final static String SUBSCRIPTION_XML_TYPE_CHANGE = "dataset/subscription_type_change.xml";
    private final static String SUBSCRIPTION_XML_BARRED = "dataset/subscription_barred.xml";
    private final static String SUBSCRIPTION_XML_NOT_BARRED = "dataset/subscription_not_barred.xml";
    private final static String SUBSCRIPTION_XML_SECRET_NUMBER = "dataset/subscription_secret_number.xml";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.NEW_SUBSCRIPTIONS);
        getJms().receive(ExternalEndpoints.CUSTOMER_NEW);
        getJms().receive(ExternalEndpoints.UPDATED_SUBSCRIPTIONS);
        getJms().receive(ExternalEndpoints.DELETED_SUBSCRIPTIONS);
        getJms().receive(ExternalEndpoints.EXPIRED_SUBSCRIPTIONS);
        getJms().receive(ExternalEndpoints.CHANGE_ACCOUNT_SUBSCRIPTIONS);
        getJms().receive(ExternalEndpoints.CHANGE_USER_SUBSCRIPTIONS);
        getJms().receive(ExternalEndpoints.CHANGE_TYPE_SUBSCRIPTIONS);
        getJms().receive(ExternalEndpoints.UPDATED_SUBSCRIPTION_STATUS);
        getJms().receive(ExternalEndpoints.SECRET_NUMBER_UPADATE);
        getJms().setReceiveTimeout(6000L);
    }

    @Test(timeout = 10000)
    public void consumeNewSubscriptionMessageFromXml() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(CUSTOMER_NEW);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_XML_INSERT);
        NewSubscriptionEvent consumedSubscriptionEvent = (NewSubscriptionEvent)getJms().receive(ExternalEndpoints.NEW_SUBSCRIPTIONS, correlationId);
        assertAction(consumedSubscriptionEvent, ACTION.CREATED);
        assertEquals("Unexpected subscription id", NEW_SUBSCRIPTION_ID, consumedSubscriptionEvent.getDomainId());
    }

    @Test(timeout = 10000)
    public void consumeLogicalDeleteSubscriptionMessageFromXml() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_XML_LOGICAL_DELETE);
        SubscriptionLogicalDeleteEvent consumedSubscriptionEvent = (SubscriptionLogicalDeleteEvent)getJms().receive(ExternalEndpoints.DELETED_SUBSCRIPTIONS, correlationId);
        assertAction(consumedSubscriptionEvent, ACTION.LOGICAL_DELETE);
        assertEquals("Unexpected subscription id", LOGICAL_DELETED_SUBSCRIPTION_ID, consumedSubscriptionEvent.getDomainId());
    }

    @Test(timeout = 10000)
    public void consumeBarredSubscriptionMessageFromXml() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_XML_BARRED);
        SubscriptionChangedStatusEvent consumedSubscriptionEvent = (SubscriptionChangedStatusEvent) getJms().receive(ExternalEndpoints.UPDATED_SUBSCRIPTION_STATUS, correlationId);
        assertAction(consumedSubscriptionEvent, ACTION.STATUS_CHANGE);
        assertEquals("Wrong status", SUBSCRIPTION_STATUS, consumedSubscriptionEvent.getStatus());
        assertEquals("Unexpected subscription id", BARRED_SUBSCRIPTION_ID, consumedSubscriptionEvent.getDomainId());
    }

    @Test(timeout = 10000)
    public void consumeNotBarredSubscriptionMessageFromXml() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_XML_NOT_BARRED);
        SubscriptionChangedStatusEvent consumedSubscriptionEvent = (SubscriptionChangedStatusEvent) getJms().receive(ExternalEndpoints.UPDATED_SUBSCRIPTION_STATUS, correlationId);
        assertAction(consumedSubscriptionEvent, ACTION.STATUS_CHANGE);
        assertEquals("Wrong status", "", consumedSubscriptionEvent.getStatus());
        assertEquals("Unexpected subscription id", BARRED_SUBSCRIPTION_ID, consumedSubscriptionEvent.getDomainId());
    }

    @Test(timeout = 10000)
    public void consumeSecretNumberSubscriptionMessageFromXml() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_XML_SECRET_NUMBER);
        SubscriptionSecretNumberEvent consumedSubscriptionEvent = (SubscriptionSecretNumberEvent) getJms().receive(ExternalEndpoints.SECRET_NUMBER_UPADATE, correlationId);
        assertAction(consumedSubscriptionEvent, ACTION.SECRET_NUMBER);
        assertEquals("Unexpected subscription id", SUBSCRIPTION_ID, consumedSubscriptionEvent.getDomainId());
        assertEquals("Unexpected secret number value", true, consumedSubscriptionEvent.isSecretNumber());
    }

    @Test(timeout = 10000)
    public void consumeChangeTypeSubscriptionMessageFromXml() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_XML_TYPE_CHANGE);
        SubscriptionChangeTypeEvent consumedSubscriptionEvent = (SubscriptionChangeTypeEvent)getJms().receive(ExternalEndpoints.CHANGE_TYPE_SUBSCRIPTIONS, correlationId);
        assertAction(consumedSubscriptionEvent, ACTION.TYPE_CHANGE);
        assertEquals("Unexpected subscription id", Long.valueOf("5858585"), consumedSubscriptionEvent.getDomainId());
    }

    @Test(timeout = 10000)
    public void consumeExpiredSubscriptionMessageFromXml() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_XML_EXPIRED);
        SubscriptionExpiredEvent consumedSubscriptionEvent = (SubscriptionExpiredEvent)getJms().receive(ExternalEndpoints.EXPIRED_SUBSCRIPTIONS, correlationId);
        assertAction(consumedSubscriptionEvent, ACTION.EXPIRED);
        assertEquals("Unexpected subscription id", EXPIRED_SUBSCRIPTION_ID, consumedSubscriptionEvent.getDomainId());

    }

    @Test(timeout = 10000)
    public void consumeChangedAccountSubscriptionMessageFromXml() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_XML_CHANGED_ACCOUNT);
        SubscriptionChangedAccountEvent consumedSubscriptionEvent = (SubscriptionChangedAccountEvent) getJms().receive(ExternalEndpoints.CHANGE_ACCOUNT_SUBSCRIPTIONS, correlationId);
        assertAction(consumedSubscriptionEvent, ACTION.CHANGE_ACCOUNT);
        assertEquals("Unexpected subscription id", CHANGED_SUBSCR_ID, consumedSubscriptionEvent.getDomainId());
        assertEquals("Unexpected subscription id", CHANGED_ACCT_ID, consumedSubscriptionEvent.getAccountId());
    }
}
