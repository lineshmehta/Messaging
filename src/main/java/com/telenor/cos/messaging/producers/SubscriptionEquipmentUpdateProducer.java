package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.SubscriptionEquipment;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionEquipmentUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class SubscriptionEquipmentUpdateProducer extends AbstractProducer {

    @Autowired
    private SubscriptionEquipmentUpdateXpathExtractor subscriptionEquipmentUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return subscriptionEquipmentUpdateXpathExtractor.getImsiNumberIdNew(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionId = XPathLong.getValue(subscriptionEquipmentUpdateXpathExtractor.getSubscriptionIdOld(message));
        String imsi = XPathString.getValue(subscriptionEquipmentUpdateXpathExtractor.getImsiNumberIdNew(message));
        return Collections.<Event>singletonList(new SubscriptionEquipmentUpdateEvent(subscriptionId, new SubscriptionEquipment(imsi)));
    }
}
