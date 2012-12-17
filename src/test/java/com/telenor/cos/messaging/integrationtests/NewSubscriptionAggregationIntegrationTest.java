package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.customer.CustomerNewEvent;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangedAccountEvent;
import com.telenor.cos.messaging.event.userref.UserReferenceNewEvent;
import com.telenor.cos.messaging.util.SubscriptionTestXml;
import com.telenor.cos.test.category.IntegrationTest;

@Category(IntegrationTest.class)
public class NewSubscriptionAggregationIntegrationTest extends CommonIntegrationTest {

    private static final Long CUSTOMER_ID = Long.valueOf("6935066");
    private static final Long MSISDN = Long.valueOf("580000506010");
    private final static Long CHANGED_ACCT_ID = Long.valueOf("3456");
    private final static Long CHANGED_SUBSCR_ID = Long.valueOf("3456789");
    private final static Long OWNER_SUBSCRIPTION_ID = Long.valueOf("30870128");
    private final static String USER_REF_DESCR = "TEST REF 1";

    @Before
    public void beforTest() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.INCOMING);
        getJms().receive(ExternalEndpoints.NEW_SUBSCRIPTIONS);
        getJms().receive(ExternalEndpoints.CUSTOMER_NEW);
        getJms().receive(ExternalEndpoints.CHANGE_ACCOUNT_SUBSCRIPTIONS);
        getJms().setReceiveTimeout(6000L);
    }

    @Test(timeout = 60000)
    public void testAggregateNewSubscription() throws Exception {
        sendXmlsToQueue(SubscriptionTestXml.INSERT_XML,
                SubscriptionTestXml.UPDATE_ONE_XML,
                convertFileToString("dataset/rel_subscription_twin_card_new.xml"),
                convertFileToString("dataset/customer_new.xml"),
                SubscriptionTestXml.UPDATE_TWO_XML,
                convertFileToString("dataset/userReference_new_numberType_TM.xml"));

        // Verify that the subscription aggregation doesn't mess-up for other events
        CustomerNewEvent customerNewEvent = (CustomerNewEvent) getJms().receiveAndConvert(ExternalEndpoints.CUSTOMER_NEW);
        assertNotNull("Did not receive customerNewEvent",customerNewEvent);
        assertEquals("Unexpected action", ACTION.CREATED, customerNewEvent.getAction());
        assertEquals("Unexpected customer id", CUSTOMER_ID, customerNewEvent.getDomainId());

        NewSubscriptionEvent aggregatedSubscriptionEvent = (NewSubscriptionEvent) getJms().receiveAndConvert(ExternalEndpoints.NEW_SUBSCRIPTIONS);
        assertNotNull("Did not receive Aggregated SubscriptionEvent",aggregatedSubscriptionEvent);
        assertEquals("Unexpected action", ACTION.CREATED, aggregatedSubscriptionEvent.getAction());
        assertEquals("Unexpected subscription id", SubscriptionTestXml.ID_OF_AGGREGATED_SUBSCRIPTION, aggregatedSubscriptionEvent.getDomainId());
        //Since SubscriptionType Cache not populated with data always productId will be null.
        assertNull("Unexpected ProductId",aggregatedSubscriptionEvent.getData().getSubscriptionType());
        assertEquals("Unexpected account id", SubscriptionTestXml.ACCOUNT_ID, aggregatedSubscriptionEvent.getData().getAccountId());
        assertFalse("Unexpected IsSecretNumber Value", aggregatedSubscriptionEvent.getData().getIsSecretNumber());
        assertEquals("Unexpected twin card msisdn", MSISDN, aggregatedSubscriptionEvent.getData().getMsisdnTvilling());
        assertEquals("Unexpected owner subscription id", OWNER_SUBSCRIPTION_ID, aggregatedSubscriptionEvent.getData().getOwnerSubscriptionId());
        assertEquals("Unexpected UserReference Description", USER_REF_DESCR, aggregatedSubscriptionEvent.getData().getUserReferenceDescription());
    }

    @Test (timeout = 30000)
    public void testSingleUpdateSubscription() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId("dataset/changedaccount_subscription.xml");
        SubscriptionChangedAccountEvent consumedSubscriptionEvent = (SubscriptionChangedAccountEvent) getJms().receive(ExternalEndpoints.CHANGE_ACCOUNT_SUBSCRIPTIONS,correlationId);
        assertNotNull("Did not receive SubscriptionChangedAccountEvent",consumedSubscriptionEvent);
        assertEquals("Unexpected action", ACTION.CHANGE_ACCOUNT, consumedSubscriptionEvent.getAction());
        assertEquals("Unexpected subscription id", CHANGED_SUBSCR_ID, consumedSubscriptionEvent.getDomainId());
        assertEquals("Unexpected subscription id", CHANGED_ACCT_ID, consumedSubscriptionEvent.getAccountId());
    }

    @Test (timeout = 30000)
    public void testSubscrUpdateAndUserRefInsertWithoutSubscrInsert() throws Exception {
        sendXmlsToQueue(convertFileToString("dataset/changedaccount_subscription.xml"),
                convertFileToString("dataset/userReference_new_numberType_TM.xml"));
        // Verify that we end up with two separate messages and not ONE aggregated message.
        SubscriptionChangedAccountEvent consumedSubscriptionEvent = (SubscriptionChangedAccountEvent) getJms().receiveAndConvert(ExternalEndpoints.CHANGE_ACCOUNT_SUBSCRIPTIONS);
        assertNotNull("Did not receive a Subscription Changed Account Event.",consumedSubscriptionEvent);
        UserReferenceNewEvent consumedUserReferenceEvent = (UserReferenceNewEvent) getJms().receiveAndConvert(ExternalEndpoints.USER_REFERENCE_NEW_TOPIC);
        assertNotNull("Did not receive a User Reference New Event", consumedUserReferenceEvent);
    }

    @Test(timeout = 60000)
    public void testNewSubscriptionIsSecretNumberStatus() throws Exception {
        sendXmlsToQueue(SubscriptionTestXml.SUBSCRIPTION_INSERT_XML,
                SubscriptionTestXml.SUBSCRIPTION_UPDATE_ONE_XML,
                SubscriptionTestXml.SUBSCRIPTION_UPDATE_TWO_XML);
        NewSubscriptionEvent aggregatedSubscriptionEvent = (NewSubscriptionEvent) getJms().receiveAndConvert(ExternalEndpoints.NEW_SUBSCRIPTIONS);
        assertNotNull("Did not receive a Aggregated Subscription Event.",aggregatedSubscriptionEvent);
        assertTrue("Unexpected IsSecretNumber Value", aggregatedSubscriptionEvent.getData().getIsSecretNumber());
    }

    private void sendXmlsToQueue(String...xmls) {
        for(int i =0;i<xmls.length;i++) {
            getJms().send(ExternalEndpoints.INCOMING_REP_SERVER, xmls[i]);
        }
    }
    private String convertFileToString(String fileLoc) throws Exception {
        return getTestHelper().fileToString(fileLoc);
    }
}
