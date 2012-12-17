package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangedStatusEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class SubscriptionChangedStatusProducer extends AbstractProducer {

    @Autowired
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return subscriptionUpdateXpathExtractor.getNewSubscrStatusId(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionId = XPathLong.getValue(subscriptionUpdateXpathExtractor.getOldSubscrId(message));
        String statusId = XPathString.getValue(subscriptionUpdateXpathExtractor.getNewSubscrStatusId(message));
        return Collections.<Event>singletonList(new SubscriptionChangedStatusEvent(subscriptionId, statusId));
    }
}
