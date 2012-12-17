package com.telenor.cos.messaging.routers;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.util.SubscriptionTestXml;
import com.telenor.cos.test.category.EmbeddedTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/fullEmbeddedApplication.xml")
@Category(EmbeddedTest.class)
@DirtiesContext
public class RoutingInsideMessagingTest extends CamelTestSupport {
    
    private final static CachableCustomer CUSTOMER = new CachableCustomer(Long.valueOf(6935066));

    @Autowired
    private ProducerTemplate producer;

    @Autowired
    private ConsumerTemplate consumer;

    @Autowired
    private JmsTemplate jms;
    
    @Autowired // TODO use mock
    private CustomerCache customerCache;
    
    /**
     * before each method
     * 
     * @throws if
     *             error
     */
    @Before
    public void before() throws Exception {
        jms.setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        jms.receive(EndPointUri.INCOMING_EVENT_QUEUE);
        jms.receive(EndPointUri.SUBSCRIPTION_NEW_TOPIC);
        jms.receive(EndPointUri.SUBSCRIPTION_EXPIRED_TOPIC);
        jms.setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
        customerCache.insert(CUSTOMER.getCustomerId(), CUSTOMER);
    }
    
    @After
    public void cleanUp() {
        customerCache.remove(CUSTOMER.getCustomerId());
    }

    @Test(timeout = 30000)
    public void newEventEndpointToEndpoint() throws Exception {
        producer.sendBody(EndPointUri.INCOMING_QUEUE, SubscriptionTestXml.INSERT_XML);
        Event subscriptionEvent = consumer.receiveBody(EndPointUri.SUBSCRIPTION_NEW_TOPIC, Event.class);
        assertEquals("Unexpected action", ACTION.CREATED, subscriptionEvent.getAction());
        assertEquals("Unexpected subscription id", Long.valueOf("1"), subscriptionEvent.getDomainId());

    }

    @Test(timeout = 30000)
    public void expiredEventEndpointToEndpoint() throws Exception {
        producer.sendBody(EndPointUri.INCOMING_QUEUE, SubscriptionTestXml.SUBSCRIPTION_EXPIRED_XML);
        Event subscriptionEvent = consumer.receiveBody(EndPointUri.SUBSCRIPTION_EXPIRED_TOPIC, Event.class);
        assertEquals("Unexpected action", ACTION.EXPIRED, subscriptionEvent.getAction());
        assertEquals("Unexpected subscription id", Long.valueOf("999"), subscriptionEvent.getDomainId());
    }
    

}
