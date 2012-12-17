package com.telenor.cos.messaging.routers.aggregation;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.telenor.cos.messaging.event.Subscription;

public class BeanMergerTest {

    private static final Long CONTRACT_ID = Long.valueOf(2);
    private static final Long ACCOUNT_ID = Long.valueOf(1);
    private static final Date VALID_FROM_DATE = new Date();
    
    private BeanMerger beanMerger;
    
    @Before
    public void before() {
        beanMerger = new BeanMerger();
    }
    
    @Test
    public void testMerge() throws Exception {
        Subscription targetSubscription = new Subscription();
        targetSubscription.setAccountId(ACCOUNT_ID);
        targetSubscription.setValidFromDate(VALID_FROM_DATE);
        
        Subscription subscriptionWithNewValues = new Subscription();
        subscriptionWithNewValues.setContractId(CONTRACT_ID);
        
        Subscription mergedSubscription = beanMerger.mergeNotNullValues(targetSubscription, subscriptionWithNewValues);
        assertEquals("Unexpected account id", ACCOUNT_ID, mergedSubscription.getAccountId());
        assertEquals("Unexpected contract id", CONTRACT_ID, mergedSubscription.getContractId());
        assertEquals("Unexpected valid from date", VALID_FROM_DATE, mergedSubscription.getValidFromDate());
        
        Subscription someMoreNewValues = new Subscription();
        someMoreNewValues.setFirstName("firstName");
        someMoreNewValues.setLastName("lastName");
        
        Subscription moreMergedSubscription = beanMerger.mergeNotNullValues(targetSubscription, someMoreNewValues);
        assertEquals("Unexpected account id", ACCOUNT_ID, moreMergedSubscription.getAccountId());
        assertEquals("Unexpected contract id", CONTRACT_ID, moreMergedSubscription.getContractId());
        assertEquals("Unexpected firstNameid", "firstName", moreMergedSubscription.getFirstName());
        assertEquals("Unexpected firstNameid", "lastName", moreMergedSubscription.getLastName());
    }
     
    @Test
    public void testFieldsNotOverWrittenByNull() throws Exception {
        Subscription targetSubscription = new Subscription();
        targetSubscription.setAccountId(ACCOUNT_ID);
        targetSubscription.setContractId(CONTRACT_ID);
        
        Subscription subscriptionWithNullValues = new Subscription();
        subscriptionWithNullValues.setContractId(null);
        subscriptionWithNullValues.setAccountId(null);
        
        Subscription mergedSubscription = beanMerger.mergeNotNullValues(targetSubscription, subscriptionWithNullValues);
        assertEquals("Unexpected account id", ACCOUNT_ID, mergedSubscription.getAccountId());
        assertEquals("Unexpected contract id", CONTRACT_ID, mergedSubscription.getContractId());
    }

}
