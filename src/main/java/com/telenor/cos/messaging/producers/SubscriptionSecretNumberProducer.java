package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.subscription.SubscriptionSecretNumberEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class SubscriptionSecretNumberProducer extends AbstractProducer {

    @Autowired
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return subscriptionUpdateXpathExtractor.getNewSubscrHasSecretNumber(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionIdOld = XPathLong.getValue(subscriptionUpdateXpathExtractor.getOldSubscrId(message));
        String hasSecretNumber = subscriptionUpdateXpathExtractor.getNewSubscrHasSecretNumber(message).getValue();
        return Collections.<Event>singletonList(new SubscriptionSecretNumberEvent(subscriptionIdOld, "Y".equals(hasSecretNumber) ? true : false));
    }
}
