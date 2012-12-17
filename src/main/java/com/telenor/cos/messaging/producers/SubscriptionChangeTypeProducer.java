package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangeTypeEvent;
import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class SubscriptionChangeTypeProducer extends AbstractProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionChangeTypeProducer.class);

    @Autowired
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Autowired
    private SubscriptionTypeCache subscriptionTypeCache;

    @Override
    public Boolean isApplicable(Node message) {
        return subscriptionUpdateXpathExtractor.getNewS212ProductId(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionId = XPathLong.getValue(subscriptionUpdateXpathExtractor.getOldSubscrId(message));
        String s212ProductId = XPathString.getValue((subscriptionUpdateXpathExtractor.getNewS212ProductId(message)));
        String type = subscriptionTypeCache.get(s212ProductId);
        if(StringUtils.isEmpty(type)) {
            LOG.error("No Subscription Type corresponding to S212ProductId [" + s212ProductId +"] was found in the Subscription Type Cache! Hence Subscription with id [" + subscriptionId + "] will not be updated correctly!");
        }
        return Collections.<Event> singletonList(new SubscriptionChangeTypeEvent(subscriptionId, type));
    }
}
