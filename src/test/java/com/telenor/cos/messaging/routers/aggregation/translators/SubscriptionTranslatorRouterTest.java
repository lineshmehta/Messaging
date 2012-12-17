package com.telenor.cos.messaging.routers.aggregation.translators;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.routers.RouterBaseTest;
import com.telenor.cos.test.category.ServiceTest;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@Category(ServiceTest.class)
@DirtiesContext
public class SubscriptionTranslatorRouterTest extends RouterBaseTest{

    private final static String XML = "<insert schema=\"Subscription\">"
            + "<values>"
            + "<cell name=\"subscr_id\" type=\"NUMERIC\">32143317</cell>"
            + "<cell name=\"acc_id\" type=\"INT\">999999001</cell>"
            + "<cell name=\"cust_id_user\" type=\"NUMERIC\">6935066</cell>"
            + "<cell name=\"subscr_valid_from_date\" type=\"DATETIME\">09.01.2012 00:00:00</cell>" 
            + "<cell name=\"subscr_valid_to_date\" type=\"DATETIME\" />"
            + "<cell name=\"subscr_has_secret_number\" type=\"CHAR\" />"
            + "<cell name=\"directory_number_id\" type=\"NUMERIC\">580000506010</cell>"
            + "<cell name=\"contract_id\" type=\"INT\">12716621</cell>"
            + "<cell name=\"s212_product_id\" type=\"NUMERIC\">04713</cell>"
            + "</values>"
            + "</insert>";
    
    @EndpointInject(uri = "mock:" + EndPointUri.SUBSCRIPTION_INCOMING_QUEUE)
    private MockEndpoint incomingSubscriptionEndpoint;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[] {"/producerTestContext.xml", "/camelTestContext.xml"});
    }
    
    public void setUp() throws Exception {
        super.setUp();
    }
    
    @Test(timeout = 20000)
    public void testSubscriptionNewTranslator() throws Exception{
        Event newSubscriptionEvent = null;
        
        template.sendBody(EndPointUri.SUBSCRIPTION_INCOMING_QUEUE, XML);
        assertReceivedMessage(incomingSubscriptionEndpoint, newSubscriptionEvent);
    }
    
    private void assertReceivedMessage(MockEndpoint mockEndpoint, Event subscriptionEvent) throws Exception {
        mockEndpoint.setAssertPeriod(1000); //wait to make sure no more than the expected messages are received
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.assertIsSatisfied();
        assertMessage(mockEndpoint, subscriptionEvent);
    }
    
    private void assertMessage(MockEndpoint mockEndpoint,  Event subscriptionEvent) {
        List <Exchange> actualMessages = mockEndpoint.getReceivedExchanges();
        String xml = actualMessages.get(0).getIn().getBody(String.class);
        assertTrue(xml.contains("32143317"));
    }
    
    
}
