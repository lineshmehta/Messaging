package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.userref.UserReferenceDescriptionUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.UserReferenceUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class UserReferenceDescriptionUpdateProducer extends AbstractProducer {

    @Autowired
    private UserReferenceUpdateXpathExtractor userReferenceUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return userReferenceUpdateXpathExtractor.getNewUserRefDescr(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionId = userReferenceUpdateXpathExtractor.getOldSusbcrId(message).getValue();
        String newUserReferenceDescr = XPathString.getValue(userReferenceUpdateXpathExtractor.getNewUserRefDescr(message));
        String numberType = XPathString.getValue(userReferenceUpdateXpathExtractor.getOldNumberTypeId(message));
        return Collections.<Event> singletonList(new UserReferenceDescriptionUpdateEvent(subscriptionId, newUserReferenceDescr,numberType));
    }
}
