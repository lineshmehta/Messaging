package com.telenor.cos.messaging.routers;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentDeleteEvent;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentEvent;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentNewEvent;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentUpdateEvent;
import com.telenor.cos.test.category.ServiceTest;

@Category(ServiceTest.class)
@DirtiesContext
public class SubscriptionEquipmentEventsRouterTest extends StandardXmlFormattingRouterBaseTest {

    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_EQUPMENT_NEW_TOPIC)
    private MockEndpoint mockNewEndpoint;

    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_EQUPMENT_UPDATE_TOPIC)
    private MockEndpoint mockUpdateEndpoint;

    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_EQUPMENT_DELETE_TOPIC)
    private MockEndpoint mockLogicalDeleteEndpoint;

    @Test(timeout = 2000)
    public void routeNewEvent() throws Exception {
        SubscriptionEquipmentEvent event = new SubscriptionEquipmentNewEvent(1L, null);
        template.sendBody(getIncomingQueue(), event);
        assertReceivedMessage(mockNewEndpoint, event, ACTION.CREATED);
    }

    @Test(timeout=2000)
    public void routeUpdateEvent() throws Exception{
        SubscriptionEquipmentEvent event = new SubscriptionEquipmentUpdateEvent(1L, null);
        template.sendBody(getIncomingQueue(), event);
        assertReceivedMessage(mockUpdateEndpoint, event, ACTION.UPDATED);
    }

    @Test(timeout=2000)
    public void routeDeleteEvent() throws Exception{
        SubscriptionEquipmentEvent event = new SubscriptionEquipmentDeleteEvent(1L, null);
        template.sendBody(getIncomingQueue(), event);
        assertReceivedMessage(mockLogicalDeleteEndpoint, event, ACTION.LOGICAL_DELETE);
    }

    @Override
    protected String getIncomingQueue() {
        return EndPointUri.SUBSCRIPTION_EQUPMENT_INCOMING_QUEUE;
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/router/subscriptionEquipmentRouter.xml");
    }

}
