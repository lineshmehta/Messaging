package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangeUserEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class SubscriptionChangeUserProducer extends AbstractProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionChangeUserProducer.class);

    @Autowired
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Autowired
    private CustomerCache customerCache;

    @Override
    public Boolean isApplicable(Node message) {
        return subscriptionUpdateXpathExtractor.getNewCustIdUser(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionId = XPathLong.getValue(subscriptionUpdateXpathExtractor.getOldSubscrId(message));
        Long newUserCustomerId = XPathLong.getValue(subscriptionUpdateXpathExtractor.getNewCustIdUser(message));

        CachableCustomer customerUserDetailsFromCache = customerCache.get(newUserCustomerId);
        CustomerName newCustomer = new CustomerName(newUserCustomerId);

        if(customerUserDetailsFromCache != null) {
            newCustomer.setFirstName(customerUserDetailsFromCache.getFirstName());
            newCustomer.setMiddleName(customerUserDetailsFromCache.getMiddleName());
            newCustomer.setLastName(customerUserDetailsFromCache.getLastName());
        } else {
            LOG.warn("Customer with UserId [" + newUserCustomerId +"] not found in CustomerCache");
        }
        return Collections.<Event> singletonList(new SubscriptionChangeUserEvent(subscriptionId, newCustomer));
    }
}
