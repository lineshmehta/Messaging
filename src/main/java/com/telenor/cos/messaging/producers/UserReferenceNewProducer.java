package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.UserReference;
import com.telenor.cos.messaging.event.userref.UserReferenceNewEvent;
import com.telenor.cos.messaging.producers.xpath.UserReferenceInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Producer for UserReference Insert Messages.
 * @author Babaprakash D
 *
 */
@Component
public class UserReferenceNewProducer extends AbstractProducer {

    @Autowired
    private UserReferenceInsertXpathExtractor userReferenceInsertXpathExtractor;

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionId = userReferenceInsertXpathExtractor.getSubscriptionId(message).getValue();
        UserReference userReference = createUserReference(message);
        UserReferenceNewEvent userReferenceNewEvent = new UserReferenceNewEvent(subscriptionId, userReference);
        List<Event> userReferenceNewEventList = new ArrayList<Event>();
        userReferenceNewEventList.add(userReferenceNewEvent);
        return userReferenceNewEventList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return userReferenceInsertXpathExtractor.getSubscriptionId(message) != null && userReferenceInsertXpathExtractor.getNumberType(message) != null;
    }

    private UserReference createUserReference(Node message) {
        UserReference userReference = new UserReference();
        userReference.setInvoiceRef(XPathString.getValue(userReferenceInsertXpathExtractor.getEInvoiceRef(message)));
        userReference.setNumberType(XPathString.getValue(userReferenceInsertXpathExtractor.getNumberType(message)));
        userReference.setUserRefDescr(XPathString.getValue(userReferenceInsertXpathExtractor.getUserRefDescription(message)));
        return userReference;
    }
}
