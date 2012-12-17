package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.userref.UserReferenceLogicalDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.UserReferenceUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class UserReferenceLogicalDeleteProducer extends AbstractProducer {

    @Autowired
    private UserReferenceUpdateXpathExtractor userReferenceUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        String infoIsDeleted = XPathString.getValue(userReferenceUpdateXpathExtractor.getNewInfoIsDeleted(message));
        return "Y".equalsIgnoreCase(infoIsDeleted);
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionId = XPathLong.getValue(userReferenceUpdateXpathExtractor.getOldSusbcrId(message));
        String numberTypeIdOld = XPathString.getValue(userReferenceUpdateXpathExtractor.getOldNumberTypeId(message));
        return Collections.<Event> singletonList(new UserReferenceLogicalDeleteEvent(subscriptionId,numberTypeIdOld));
    }
}
