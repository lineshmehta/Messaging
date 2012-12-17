package com.telenor.cos.messaging.routers.aggregation.translators;

import com.telenor.cos.messaging.CosMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.producers.xpath.UserReferenceInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * @author Babaprakash D
 *
 */
@Component
public class NewUserReferenceAggregationTranslator {

    @Autowired
    private UserReferenceInsertXpathExtractor userReferenceInsertXpathExtractor;

    /**
     * Translates message to Event.
     * @param subscriptionEvent subscriptionEvent.
     * @param message message
     * @return Event.
     */
    public Event translate(NewSubscriptionEvent subscriptionEvent,Node message) {
        String einvoiceRef  = XPathString.getValue(userReferenceInsertXpathExtractor.getEInvoiceRef(message));
        String numberType = XPathString.getValue(userReferenceInsertXpathExtractor.getNumberType(message));
        String userRefDesc = XPathString.getValue(userReferenceInsertXpathExtractor.getUserRefDescription(message));

        if (subscriptionEvent == null ) {
            throw new CosMessagingException("NewSubscriptionEvent is empty (NULL).", null);
        }

        if ( subscriptionEvent.getData() == null) {
            throw new CosMessagingException("NewSubscriptionEvent.getData() returns empty (NULL) dataset.", null);
        }

        if ("ES".equals(numberType)) {
            subscriptionEvent.getData().setUserReference(userRefDesc);
            subscriptionEvent.getData().setInvoiceReference(einvoiceRef);
        } else if("TM".equals(numberType)) {
            subscriptionEvent.getData().setUserReferenceDescription(userRefDesc);            
        }
        return subscriptionEvent;
    }
}
