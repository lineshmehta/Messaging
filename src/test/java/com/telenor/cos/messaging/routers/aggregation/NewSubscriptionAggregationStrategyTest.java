package com.telenor.cos.messaging.routers.aggregation;

import static com.telenor.cos.messaging.event.Event.ACTION.CREATED;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.routers.AbstractRouter;
import com.telenor.cos.messaging.util.SubscriptionTestXml;
import com.telenor.cos.messaging.util.TestHelper;
import com.telenor.cos.test.category.EmbeddedTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/fullEmbeddedApplication.xml")
@Category(EmbeddedTest.class)
@DirtiesContext
public class NewSubscriptionAggregationStrategyTest extends CamelTestSupport{

    private final static Long MSISDN = Long.valueOf("580000506010");
    private final static Long OWNER_SUBSCRIPTION_ID = Long.valueOf("30870128");
    private final static CachableCustomer CUSTOMER = new CachableCustomer(Long.valueOf(6935066));
    private final static String USER_REF_DESCR = "TEST REF 1";
    private final static String EINVOICE_REF = "TEST EINVOICE REF";

    private final static String CUSTOMER_NEW_XML = "dataset/customer_new.xml";
    private final static String REL_SUBSCRIPTION_DATA_CARD_NEW_XML = "dataset/rel_subscription_data_card_new.xml";
    private final static String REL_SUBSCRIPTION_DATA_CARD2_NEW_XML = "dataset/rel_subscription_data_card2_new.xml";
    private final static String REL_SUBSCRIPTION_TWIN_CARD_NEW_XML = "dataset/rel_subscription_twin_card_new.xml";
    private final static String USERREF_NEW_XML_NUMBERTYPE_TM = "dataset/userReference_new_numberType_TM.xml";
    private final static String USERREF_NEW_XML_NUMBERTYPE_ES = "dataset/userReference_new_numberType_ES.xml";

    @Autowired
    private NewSubscriptionAggregationStrategy newSubscriptionAggregationStrategy;

    @EndpointInject(uri = "mock:result")
    private MockEndpoint mockAggregateEndpoint;

    @EndpointInject(uri = "mock:fallThroughResult")
    private MockEndpoint mockFallThroughEndPoint;

    @Autowired // TODO use mock
    private CustomerCache customerCache;

    @Autowired
    private SubscriptionTypeCache subscriptionTypeCache;

    private TestHelper testHelper = new TestHelper();

    @Before
    public void before() {
        customerCache.insert(CUSTOMER.getCustomerId(), CUSTOMER);
        subscriptionTypeCache.insert(SubscriptionTestXml.S212_PRODUCT_ID, SubscriptionTestXml.PRODUCT_ID);
        mockAggregateEndpoint.setAssertPeriod(1000); //wait to make sure no more than the expected messages are received
    }

    @After
    public void cleanUp() {
        customerCache.remove(CUSTOMER.getCustomerId());
        subscriptionTypeCache.remove(SubscriptionTestXml.S212_PRODUCT_ID);
    }

    @Test (timeout = 30000)
    public void testFullAggregation() throws Exception {
        mockAggregateEndpoint.expectedMessageCount(2);
        mockFallThroughEndPoint.expectedMessageCount(2);

        sendMessages(SubscriptionTestXml.INSERT_XML, SubscriptionTestXml.NO_MATCH_ON_ID_XML,
                testHelper.fileToString(REL_SUBSCRIPTION_TWIN_CARD_NEW_XML), SubscriptionTestXml.UPDATE_ONE_XML,
                SubscriptionTestXml.SINGLE_UPDATE_WITH_NO_MATCH_ON_ID_XML, SubscriptionTestXml.UPDATE_TWO_XML,
                testHelper.fileToString(CUSTOMER_NEW_XML),testHelper.fileToString(USERREF_NEW_XML_NUMBERTYPE_TM));

        waitForCompletionTimeout();
        List <Exchange> aggregateMessages = mockAggregateEndpoint.getReceivedExchanges();

        assertEquals("Unexpected number of aggregated messages", 2, aggregateMessages.size());
        NewSubscriptionEvent singleInsertSubscriptionEvent = (NewSubscriptionEvent) aggregateMessages.get(0).getIn().getBody(NewSubscriptionEvent.class);
        NewSubscriptionEvent aggregatedSubscriptionEvent = (NewSubscriptionEvent) aggregateMessages.get(1).getIn().getBody(NewSubscriptionEvent.class);

        assertEquals("Unexpected subscription id", SubscriptionTestXml.ID_OF_SINGLE_SUBSCRIPTION_INSERT, singleInsertSubscriptionEvent.getDomainId());
        assertEquals("Unexpected msisdn", MSISDN, aggregatedSubscriptionEvent.getData().getMsisdn());
        assertEquals("Unexpected owner subscription id", OWNER_SUBSCRIPTION_ID, aggregatedSubscriptionEvent.getData().getOwnerSubscriptionId());
        assertEquals("Unexpected twin card msisdn", MSISDN, aggregatedSubscriptionEvent.getData().getMsisdnTvilling());
        assertEquals("Unexpected UserRef Description", USER_REF_DESCR, aggregatedSubscriptionEvent.getData().getUserReferenceDescription());
        assertEquals("Unexpected subscription id", SubscriptionTestXml.ID_OF_AGGREGATED_SUBSCRIPTION, aggregatedSubscriptionEvent.getDomainId());
        assertEquals("Unexpected product id", SubscriptionTestXml.PRODUCT_ID, aggregatedSubscriptionEvent.getData().getSubscriptionType());
        assertEquals("Unexpected account id", SubscriptionTestXml.ACCOUNT_ID, aggregatedSubscriptionEvent.getData().getAccountId());

        List<Exchange> fallThroughMessages = mockFallThroughEndPoint.getReceivedExchanges();
        assertEquals("Unexpected number of fall though messages", 2, fallThroughMessages.size());

        String singleSubscriptionUpdateEventXML = fallThroughMessages.get(0).getIn().getBody(String.class);
        assertEquals("Unexpected message xml", singleSubscriptionUpdateEventXML, SubscriptionTestXml.SINGLE_UPDATE_WITH_NO_MATCH_ON_ID_XML);
        String newCustomerEventXML = fallThroughMessages.get(1).getIn().getBody(String.class);
        assertTrue("Unexpected message xml", newCustomerEventXML.contains("<insert schema=\"CUSTOMER\">") );
        assertMockEndpointsSatisfied();
    }

    @Test (timeout = 30000)
    public void testAggregationOfDataCardSubscripton() throws Exception {
        mockAggregateEndpoint.expectedMessageCount(1);
        sendMessages(SubscriptionTestXml.INSERT_XML, testHelper.fileToString(REL_SUBSCRIPTION_DATA_CARD_NEW_XML), SubscriptionTestXml.UPDATE_ONE_XML);
        waitForCompletionTimeout();

        List <Exchange> aggregateMessages = mockAggregateEndpoint.getReceivedExchanges();
        assertEquals("Unexpected number of aggregated messages", 1, aggregateMessages.size());
        NewSubscriptionEvent aggregatedSubscriptionEvent = (NewSubscriptionEvent) aggregateMessages.get(0).getIn().getBody(NewSubscriptionEvent.class);
        assertEquals("Unexpected subscription id", SubscriptionTestXml.ID_OF_AGGREGATED_SUBSCRIPTION, aggregatedSubscriptionEvent.getDomainId());
        assertEquals("Unexpected data card msisdn", MSISDN, aggregatedSubscriptionEvent.getData().getMsisdnDatakort());
        assertEquals("Unexpected product id", SubscriptionTestXml.PRODUCT_ID, aggregatedSubscriptionEvent.getData().getSubscriptionType());
        assertEquals("Unexpected owner subscription id", OWNER_SUBSCRIPTION_ID, aggregatedSubscriptionEvent.getData().getOwnerSubscriptionId());
        assertMockEndpointsSatisfied();
    }


    @Test (timeout = 30000)
    public void testAggregationData2CardSubscripton() throws Exception {
        mockAggregateEndpoint.expectedMessageCount(1);
        sendMessages(SubscriptionTestXml.INSERT_XML, testHelper.fileToString(REL_SUBSCRIPTION_DATA_CARD2_NEW_XML), SubscriptionTestXml.UPDATE_ONE_XML);
        waitForCompletionTimeout();

        List <Exchange> aggregateMessages = mockAggregateEndpoint.getReceivedExchanges();
        assertEquals("Unexpected number of aggregated messages", 1, aggregateMessages.size());
        NewSubscriptionEvent aggregatedSubscriptionEvent = (NewSubscriptionEvent) aggregateMessages.get(0).getIn().getBody(NewSubscriptionEvent.class);
        assertEquals("Unexpected subscription id", SubscriptionTestXml.ID_OF_AGGREGATED_SUBSCRIPTION, aggregatedSubscriptionEvent.getDomainId());
        assertEquals("Unexpected data card2 msisdn", MSISDN, aggregatedSubscriptionEvent.getData().getMsisdnDatakort2());
        assertEquals("Unexpected product id", SubscriptionTestXml.PRODUCT_ID, aggregatedSubscriptionEvent.getData().getSubscriptionType());
        assertEquals("Unexpected owner subscription id", OWNER_SUBSCRIPTION_ID, aggregatedSubscriptionEvent.getData().getOwnerSubscriptionId());
        assertMockEndpointsSatisfied();
    }

    @Test (timeout = 30000)
    public void testAggregationTwinCardSubscripton() throws Exception {
        mockAggregateEndpoint.expectedMessageCount(1);
        sendMessages(SubscriptionTestXml.INSERT_XML, testHelper.fileToString(REL_SUBSCRIPTION_TWIN_CARD_NEW_XML), SubscriptionTestXml.UPDATE_ONE_XML);
        waitForCompletionTimeout();

        List <Exchange> aggregateMessages = mockAggregateEndpoint.getReceivedExchanges();
        assertEquals("Unexpected number of aggregated messages", 1, aggregateMessages.size());
        NewSubscriptionEvent aggregatedSubscriptionEvent = (NewSubscriptionEvent) aggregateMessages.get(0).getIn().getBody(NewSubscriptionEvent.class);
        assertEquals("Unexpected subscription id", SubscriptionTestXml.ID_OF_AGGREGATED_SUBSCRIPTION, aggregatedSubscriptionEvent.getDomainId());
        assertEquals("Unexpected twin card msisdn", MSISDN, aggregatedSubscriptionEvent.getData().getMsisdnTvilling());
        assertEquals("Unexpected product id", SubscriptionTestXml.PRODUCT_ID, aggregatedSubscriptionEvent.getData().getSubscriptionType());
        assertEquals("Unexpected owner subscription id", OWNER_SUBSCRIPTION_ID, aggregatedSubscriptionEvent.getData().getOwnerSubscriptionId());
        assertMockEndpointsSatisfied();
    }

    @Test(timeout = 30000)
    public void testAggregationOfUserRefMessageWithNumberTypeTM() throws Exception {
        mockAggregateEndpoint.expectedMessageCount(1);
        sendMessages(SubscriptionTestXml.INSERT_XML, testHelper.fileToString(USERREF_NEW_XML_NUMBERTYPE_TM), SubscriptionTestXml.UPDATE_ONE_XML);
        waitForCompletionTimeout();

        List <Exchange> aggregateMessages = mockAggregateEndpoint.getReceivedExchanges();
        assertEquals("Unexpected number of aggregated messages", 1, aggregateMessages.size());
        NewSubscriptionEvent aggregatedSubscriptionEvent = (NewSubscriptionEvent) aggregateMessages.get(0).getIn().getBody(NewSubscriptionEvent.class);
        assertEquals("Unexpected subscription id", SubscriptionTestXml.ID_OF_AGGREGATED_SUBSCRIPTION, aggregatedSubscriptionEvent.getDomainId());
        assertEquals("Unexpected UserRef Description", USER_REF_DESCR, aggregatedSubscriptionEvent.getData().getUserReferenceDescription());
    }

    @Test(timeout = 30000)
    public void testAggregationOfUserRefMessageWithNumberTypeES() throws Exception {
        mockAggregateEndpoint.expectedMessageCount(1);
        sendMessages(SubscriptionTestXml.INSERT_XML, testHelper.fileToString(USERREF_NEW_XML_NUMBERTYPE_ES), SubscriptionTestXml.UPDATE_ONE_XML);
        waitForCompletionTimeout();

        List <Exchange> aggregateMessages = mockAggregateEndpoint.getReceivedExchanges();
        assertEquals("Unexpected number of aggregated messages", 1, aggregateMessages.size());
        NewSubscriptionEvent aggregatedSubscriptionEvent = (NewSubscriptionEvent) aggregateMessages.get(0).getIn().getBody(NewSubscriptionEvent.class);
        assertEquals("Unexpected subscription id", SubscriptionTestXml.ID_OF_AGGREGATED_SUBSCRIPTION, aggregatedSubscriptionEvent.getDomainId());
        assertEquals("Unexpected UserRef Description", null, aggregatedSubscriptionEvent.getData().getUserReferenceDescription());
        assertEquals("Unexpected UserRef", USER_REF_DESCR, aggregatedSubscriptionEvent.getData().getUserReference());
        assertEquals("Unexpected EInvoice Ref", EINVOICE_REF, aggregatedSubscriptionEvent.getData().getInvoiceReference());
    }

    @Test(timeout = 30000)
    public void testAggregationOfMultipleUserRefInsertMessagesWithoutSubscriptionInsert() throws Exception {
        mockAggregateEndpoint.expectedMessageCount(0);
        mockFallThroughEndPoint.setAssertPeriod(1000);
        mockFallThroughEndPoint.expectedMessageCount(2);

        String userRefNewXMLNumberTypeESXML = testHelper.fileToString(USERREF_NEW_XML_NUMBERTYPE_ES);
        String userRefNewXMLNumberTypeTMXML = testHelper.fileToString(USERREF_NEW_XML_NUMBERTYPE_TM);

        sendMessages(userRefNewXMLNumberTypeESXML, userRefNewXMLNumberTypeTMXML);
        waitForCompletionTimeout();

        List <Exchange> aggregateMessages = mockAggregateEndpoint.getReceivedExchanges();
        assertEquals("Unexpected number of aggregated messages", 0, aggregateMessages.size());

        List<Exchange> fallThroughMessages = mockFallThroughEndPoint.getReceivedExchanges();
        assertEquals("Unexpected number of fall through messages", 2, fallThroughMessages.size());

        assertMockEndpointsSatisfied();
    }

    @Test(timeout = 30000)
    public void testAggregationOfSubscrUpdateAndUserRefInsertWithoutSubscrInsert() throws Exception {

        mockAggregateEndpoint.expectedMessageCount(0);
        mockFallThroughEndPoint.expectedMessageCount(2);

        sendMessages(SubscriptionTestXml.UPDATE_ONE_XML, testHelper.fileToString(USERREF_NEW_XML_NUMBERTYPE_TM));
        waitForCompletionTimeout();

        List <Exchange> aggregateMessages = mockAggregateEndpoint.getReceivedExchanges();
        assertEquals("Unexpected number of aggregated messages", 0, aggregateMessages.size());

        List<Exchange> fallThroughMessages = mockFallThroughEndPoint.getReceivedExchanges();
        assertEquals("Unexpected number of fall through messages", 2, fallThroughMessages.size());

        assertMockEndpointsSatisfied();
    }


    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new TestRouter();
    }

    private class TestRouter extends AbstractRouter {

        @Override
        public void configure() throws Exception {
            from("direct:start")
            .convertBodyTo(Document.class)
            .choice()
            .when().xpath("//*[@schema='SUBSCRIPTION'] | //insert[@schema='REL_SUBSCRIPTION'] | //insert[@schema='USER_REFERENCE']")
            .to("direct:subscriptionMessages")
            .otherwise()
            .to(mockFallThroughEndPoint)
            .end();

            String xpath = "//insert[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_ID']/text() | " +
                           "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='SUBSCR_ID']/text() | " +
                           "//insert[@schema='REL_SUBSCRIPTION']//values//cell[@name='subscr_id_member']/text() | " +
                           "//insert[@schema='USER_REFERENCE']//values//cell[@name='SUBSCR_ID']/text() ";
            from("direct:subscriptionMessages")
            .aggregate(xpath(xpath), newSubscriptionAggregationStrategy)
            .completionPredicate(header("abortAggregation").isEqualTo(true))
            .ignoreInvalidCorrelationKeys()
            .completionTimeout(1000)
            .choice()
                .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, "Got a subscription of type: " + CREATED)
                    .to(mockAggregateEndpoint)
                .otherwise()
                    .to(mockFallThroughEndPoint)
            .end();
        }
    }

    private void sendMessages(String... messages) throws Exception {
        for(String message : messages) {
            template.sendBody("direct:start", message);
        }
    }

    private void waitForCompletionTimeout() throws InterruptedException {
        Thread.sleep(3000);
    }

}
