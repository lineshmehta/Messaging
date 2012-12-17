package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.subscription.SubscriptionChangedAccountEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class SubscriptionAccountChangedProducer extends AbstractProducer {

    @Autowired
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return subscriptionUpdateXpathExtractor.getNewAccId(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionId = XPathLong.getValue(subscriptionUpdateXpathExtractor.getOldSubscrId(message));
        Long accoundId = XPathLong.getValue(subscriptionUpdateXpathExtractor.getNewAccId(message));
        return Collections.<Event>singletonList(new SubscriptionChangedAccountEvent(subscriptionId, accoundId));
    }
}
