package com.telenor.cos.messaging.integrationtests;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.User;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentDeleteEvent;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentNewEvent;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentUpdateEvent;
import com.telenor.cos.test.category.IntegrationTest;
import com.thoughtworks.xstream.XStream;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import static com.telenor.cos.messaging.event.CosCorrelationIdFactory.createMessageSelectorExpression;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Category(IntegrationTest.class)
public class SubscriptionEquipmentRouterIntegrationTest extends CommonIntegrationTest {

    private final static String SUBSCRIPTION_EQUIPMENT_NEW_XML = "dataset/subscription_equipment_info_new.xml";
    private final static String SUBSCRIPTION_EQUIPMENT_DELETE_XML = "dataset/subscription_equipment_info_delete.xml";
    private final static String SUBSCRIPTION_EQUIPMENT_UPDATE_XML = "dataset/subscription_equipment_info_update.xml";
    public static final String DATA_FORMAT = "UTF-8";
    public static final String IMSI = "89242010110169699";
    public static final int SUBSCRIPTION_ID = 23;

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.SUBSCRIPTION_EQUPMENT_DELETE_TOPIC);
        getJms().receive(ExternalEndpoints.SUBSCRIPTION_EQUPMENT_NEW_TOPIC);
        getJms().receive(ExternalEndpoints.SUBSCRIPTION_EQUPMENT_UPDATE_TOPIC);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test(timeout = 10000)
    public void consumeSubscriptionEquipmentInsertMessageFromXml() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_EQUIPMENT_NEW_XML);
        ActiveMQBytesMessage message = (ActiveMQBytesMessage) getJms(User.OTA).receiveSelected(ExternalEndpoints.SUBSCRIPTION_EQUPMENT_NEW_TOPIC,
                createMessageSelectorExpression(correlationId));
        SubscriptionEquipmentNewEvent actualEvent = (SubscriptionEquipmentNewEvent) extractEventFromMessage(message);
        assertNotNull("Not Received Subscription Equipement Event",actualEvent);
        assertEquals("Unexpected subscription id", 101857, actualEvent.getDomainId().longValue());
        assertEquals("Unexpected imsi", "121231234", actualEvent.getSubscriptionEquipment().getImsi());
    }

    @Test(timeout = 10000)
    public void consumeSubscriptionEquipmentDeleteMessageFromXml() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_EQUIPMENT_DELETE_XML);
        ActiveMQBytesMessage message = (ActiveMQBytesMessage) getJms(User.OTA).receiveSelected(ExternalEndpoints.SUBSCRIPTION_EQUPMENT_DELETE_TOPIC,
                createMessageSelectorExpression(correlationId));
        SubscriptionEquipmentDeleteEvent actualEvent = (SubscriptionEquipmentDeleteEvent)extractEventFromMessage(message);
        assertNotNull("Not Received Subscription Equipement Event",actualEvent);
        assertEquals("Unexpected subscription id", SUBSCRIPTION_ID, actualEvent.getDomainId().longValue());
        assertEquals("Unexpected imsi", IMSI, actualEvent.getSubscriptionEquipment().getImsi());
    }

    @Test(timeout = 10000)
    public void consumeSubscriptionEquipmentUpdateMessageFromXml() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(SUBSCRIPTION_EQUIPMENT_UPDATE_XML);
        ActiveMQBytesMessage message = (ActiveMQBytesMessage) getJms(User.OTA).receiveSelected(ExternalEndpoints.SUBSCRIPTION_EQUPMENT_UPDATE_TOPIC,
                createMessageSelectorExpression(correlationId));
        SubscriptionEquipmentUpdateEvent actualEvent = (SubscriptionEquipmentUpdateEvent) extractEventFromMessage(message);
        assertNotNull("Not Received Subscription Equipement Event",actualEvent);
        assertEquals("Unexpected subscription id", SUBSCRIPTION_ID, actualEvent.getDomainId().longValue());
        assertEquals("Unexpected imsi", IMSI, actualEvent.getSubscriptionEquipment().getImsi());
    }

    /**
     * @param message the message to extract from
     * @return Event
     * @throws Exception
     */
    private Event extractEventFromMessage(ActiveMQMessage message)throws Exception{
        String xml = new String(message.getContent().getData(), DATA_FORMAT);
        return (Event) new XStream().fromXML(xml);
    }
}
