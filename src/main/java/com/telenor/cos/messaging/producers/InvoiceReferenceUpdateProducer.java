package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.userref.InvoiceReferenceUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.UserReferenceUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class InvoiceReferenceUpdateProducer extends AbstractProducer {

    @Autowired
    private UserReferenceUpdateXpathExtractor userReferenceUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return userReferenceUpdateXpathExtractor.getNewEinvoiceRef(message) != null && isNumberTypeES(message);
    }

    @Override
    public List<Event> produceMessage(Node message) {
        String newEInvoiceRef = XPathString.getValue(userReferenceUpdateXpathExtractor.getNewEinvoiceRef(message));
        Long subscriptionId = userReferenceUpdateXpathExtractor.getOldSusbcrId(message).getValue();
        InvoiceReferenceUpdateEvent invoiceReferenceUpdateEvent = new InvoiceReferenceUpdateEvent(subscriptionId, newEInvoiceRef);
        return Collections.<Event>singletonList(invoiceReferenceUpdateEvent);
    }

    private boolean isNumberTypeES(Node message) {
        return "ES".equals(XPathString.getValue(userReferenceUpdateXpathExtractor.getOldNumberTypeId(message)));
    }
}