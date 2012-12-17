package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.subscription.SubscriptionChangeUserEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.test.category.ServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@Category(ServiceTest.class)
public class SubscriptionChangeUserProducerTest {

    @Mock
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;
    
    @Mock
    private CustomerCache customerCache;

    private SubscriptionChangeUserProducer subscriptionChangeUserProducer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subscriptionChangeUserProducer = new SubscriptionChangeUserProducer();
        ReflectionTestUtils.setField(subscriptionChangeUserProducer, "subscriptionUpdateXpathExtractor", subscriptionUpdateXpathExtractor);
        ReflectionTestUtils.setField(subscriptionChangeUserProducer, "customerCache", customerCache);
    }
    
    @Test
    public void testProduceMessage() throws Exception {
        CachableCustomer customer = new CachableCustomer(2L);
        customer.setFirstName("Nils");
        customer.setMiddleName("Ole");
        customer.setLastName("Larsen");
        when(subscriptionUpdateXpathExtractor.getOldSubscrId(any(Node.class))).thenReturn(XPathLong.valueOf(1L));
        when(subscriptionUpdateXpathExtractor.getNewCustIdUser(any(Node.class))).thenReturn(XPathLong.valueOf(2L));
        when(customerCache.get(2L)).thenReturn(customer);
        SubscriptionChangeUserEvent event = (SubscriptionChangeUserEvent) (subscriptionChangeUserProducer.produceMessage(any(Node.class))).get(0);
        assertNotNull(event);
        assertEquals(Long.valueOf("1"), event.getDomainId());
        assertEquals(Long.valueOf("2"), event.getCustomerName().getCustomerId());
        assertEquals("Nils", event.getCustomerName().getFirstName());
        assertEquals("Ole", event.getCustomerName().getMiddleName());
        assertEquals("Larsen", event.getCustomerName().getLastName());
    }
}
