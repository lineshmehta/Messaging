package com.telenor.cos.messaging.acceptancetest;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.telenor.cos.graph.service.SubscriptionDto;
import com.telenor.cos.test.category.AcceptanceTest;
import com.telenor.cosread.service.search.SearchResultMetaDataDto;
import com.telenor.cosread.service.search.SubscriptionSearchResultDto;

@Category(AcceptanceTest.class)
public class NewSubscriptionTest extends CommonSubscriptionTest {
    
    private static final String SUBSCRIPTION_NEW_XML_FILE="NewSubscriptionTestInput.xml";
    
    private static final String NEW_SUBSCRIPTION_ID = "999999075";
    
    private static final String ACCOUNT_ID = "999999005";
    
    private static final String USER_CUST_ID = "6935066";
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }
    
    @Test
    public void testToVerifyNewSubscrMsgsInsertedInReadAndGraph() throws Exception {
        
        //Prepare input xml
        String inputXML = prepareInputXML(SUBSCRIPTION_NEW_XML_FILE);
        
        //Call repServer send service to post messages to queue.
        sendMsgsToRepServer(inputXML);
        
        //Added Below code to allow commit in solr to complete before data search.
        Thread.sleep(60000);
        
        //Call cosgraph read service to verify inserted data.
        SubscriptionDto masterSubscriptionDto = searchInCosGraphMaster(NEW_SUBSCRIPTION_ID);
        
        //Verify Subscription Data
        assertgraphSubscriptionDto(masterSubscriptionDto);
        
        SubscriptionDto slaveSubscriptionDto = searchInCosGraphSlave(NEW_SUBSCRIPTION_ID);
        
        //Verify Subscription Data
        assertgraphSubscriptionDto(slaveSubscriptionDto);
        
        //Call cosread search service to verify inserted data.
        SubscriptionSearchResultDto subscriptionSearchResultDto = searchInCosRead(createUserDto(62,"ScroogeMcDuck"),createClauseDto(NEW_SUBSCRIPTION_ID));
        
        //Verify Subscription Search Result Data.
        assertSubscriptionSearchResultDto(subscriptionSearchResultDto);
        
        deleteSubscrDataInCosRead(NEW_SUBSCRIPTION_ID);
        
        Thread.sleep(60000);
        
        //Call cosread search service to verify Deleted data.
        SubscriptionSearchResultDto subscriptionDto = searchInCosRead(createUserDto(62,"ScroogeMcDuck"),createClauseDto(NEW_SUBSCRIPTION_ID));
        
        assertEquals(0,subscriptionDto.getSubscriptions().size());
        
    }
    
    private void assertSubscriptionSearchResultDto(SubscriptionSearchResultDto subscriptionSearchResultDto) {
        List<com.telenor.cosread.service.search.SubscriptionDto> returnedSubscriptions = subscriptionSearchResultDto.getSubscriptions();
        SearchResultMetaDataDto searchResultMetaData = subscriptionSearchResultDto.getSearchResultMetaData();
        
        if(returnedSubscriptions != null && returnedSubscriptions.size() > 0) {
            Assert.assertEquals(ACCOUNT_ID,returnedSubscriptions.get(0).getAccountId());
            Assert.assertEquals(NEW_SUBSCRIPTION_ID,returnedSubscriptions.get(0).getSubscriptionId());
            Assert.assertEquals(0,searchResultMetaData.getFrom());
            Assert.assertEquals(1,searchResultMetaData.getTotalHits()); 
        }else {
            Assert.fail("zero Subscriptions are returned from cosread");
        }
        
    }
    private void assertgraphSubscriptionDto(SubscriptionDto subscriptionDto) {
        Assert.assertNotNull(subscriptionDto);
        Assert.assertEquals(USER_CUST_ID, subscriptionDto.getUserCustomerId());
    }
}
