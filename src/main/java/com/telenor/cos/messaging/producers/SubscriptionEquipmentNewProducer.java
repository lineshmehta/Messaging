package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.SubscriptionEquipment;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentNewEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionEquipmentNewXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class SubscriptionEquipmentNewProducer extends AbstractProducer {

    @Autowired
    private SubscriptionEquipmentNewXpathExtractor subscriptionEquipmentNewXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return subscriptionEquipmentNewXpathExtractor.getImsiNumberId(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionId = XPathLong.getValue(subscriptionEquipmentNewXpathExtractor.getSubscriptionId(message));
        String imsi = XPathString.getValue(subscriptionEquipmentNewXpathExtractor.getImsiNumberId(message));
        return Collections.<Event>singletonList(new SubscriptionEquipmentNewEvent(subscriptionId, new SubscriptionEquipment(imsi)));
    }
}
