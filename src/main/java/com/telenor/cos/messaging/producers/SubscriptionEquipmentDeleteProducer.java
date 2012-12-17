package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.SubscriptionEquipment;
import com.telenor.cos.messaging.event.subscriptionEquipment.SubscriptionEquipmentDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionEquipmentUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class SubscriptionEquipmentDeleteProducer extends AbstractProducer {

    @Autowired
    private SubscriptionEquipmentUpdateXpathExtractor subscriptionEquipmentUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        Date validToDate = XPathDate.getValue(subscriptionEquipmentUpdateXpathExtractor.getValidToDate(message));
        boolean isExpired = validToDate != null && DateTime.now().isAfter(validToDate.getTime());

        String infoIsDeleted = XPathString.getValue(subscriptionEquipmentUpdateXpathExtractor.getInfoIsDeleted(message));
        boolean isDeleted = infoIsDeleted != null && "Y".equalsIgnoreCase(infoIsDeleted);
        return isExpired || isDeleted;
    }

    @Override
    public List<Event> produceMessage(Node message) {
       Long subscriptionId = XPathLong.getValue(subscriptionEquipmentUpdateXpathExtractor.getSubscriptionIdOld(message));
       String imsi = XPathString.getValue(subscriptionEquipmentUpdateXpathExtractor.getImsiNumberIdOld(message));
       return Collections.<Event>singletonList(new SubscriptionEquipmentDeleteEvent(subscriptionId, new SubscriptionEquipment(imsi)));
    }
}
