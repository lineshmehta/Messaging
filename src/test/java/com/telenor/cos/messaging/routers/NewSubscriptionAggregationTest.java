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
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.customer.CustomerNewEvent;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.util.SubscriptionTestXml;
import com.telenor.cos.messaging.util.TestHelper;
import com.telenor.cos.test.category.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/fullEmbeddedApplication.xml")
@Category(IntegrationTest.class)
@DirtiesContext
public class NewSubscriptionAggregationTest extends CamelTestSupport {
    
    private final static CachableCustomer CUSTOMER = new CachableCustomer(Long.valueOf(6935066));
    
    @Autowired
    private ProducerTemplate producer;

    @Autowired
    private ConsumerTemplate consumer;
    
    @Autowired
    private JmsTemplate jms;
    
    @Autowired // TODO use mock
    private CustomerCache customerCache;
    
    @Autowired
    private SubscriptionTypeCache subscriptionTypeCache;
    
    @Before
    public void before() throws Exception {
        jms.setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        jms.receive(EndPointUri.INCOMING_QUEUE);
        jms.receive(EndPointUri.SUBSCRIPTION_NEW_TOPIC);
        jms.receive(EndPointUri.CUSTOMER_NEW_TOPIC);
        jms.setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
        customerCache.insert(CUSTOMER.getCustomerId(), CUSTOMER);
        subscriptionTypeCache.insert(SubscriptionTestXml.S212_PRODUCT_ID, SubscriptionTestXml.PRODUCT_ID);
    }
    
    @After
    public void cleanUp() {
        customerCache.remove(CUSTOMER.getCustomerId());
        subscriptionTypeCache.remove(SubscriptionTestXml.S212_PRODUCT_ID);
    }
    
    @Test (timeout = 45000)
    public void testAggregateNewSubscription() throws Exception {
        producer.sendBody(EndPointUri.INCOMING_QUEUE, SubscriptionTestXml.INSERT_XML);
        producer.sendBody(EndPointUri.INCOMING_QUEUE, SubscriptionTestXml.UPDATE_ONE_XML); // Updates productId
        producer.sendBody(EndPointUri.INCOMING_QUEUE, new TestHelper().fileToString("dataset/customer_new.xml"));
        producer.sendBody(EndPointUri.INCOMING_QUEUE, SubscriptionTestXml.UPDATE_TWO_XML);  // Updates accountId
         
        // Verify that the subscription aggregation doesn't mess-up for other events
        CustomerNewEvent customerNewEvent = consumer.receiveBody(EndPointUri.CUSTOMER_NEW_TOPIC, CustomerNewEvent.class);
        assertEquals("Unexpected action", ACTION.CREATED, customerNewEvent.getAction());
        
        NewSubscriptionEvent aggregatedSubscriptionEvent = consumer.receiveBody(EndPointUri.SUBSCRIPTION_NEW_TOPIC, NewSubscriptionEvent.class);
        assertEquals("Unexpected action", ACTION.CREATED, aggregatedSubscriptionEvent.getAction());
        assertEquals("Unexpected subscription id", SubscriptionTestXml.ID_OF_AGGREGATED_SUBSCRIPTION, aggregatedSubscriptionEvent.getDomainId());
        assertEquals("Unexpected product id", SubscriptionTestXml.PRODUCT_ID, aggregatedSubscriptionEvent.getData().getSubscriptionType());
        assertEquals("Unexpected account id", SubscriptionTestXml.ACCOUNT_ID, aggregatedSubscriptionEvent.getData().getAccountId());
    }
    

}
