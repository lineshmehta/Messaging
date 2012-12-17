package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.customer.CustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.handlers.CustomerLogicalDeleteHandler;
import com.telenor.cos.messaging.producers.xpath.CustomerUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class CustomerLogicalDeleteProducer extends AbstractProducer {

    @Autowired
    private CustomerUpdateXpathExtractor customerUpdateXpathExtractor;


    @Autowired
    private CustomerLogicalDeleteHandler customerLogicalDeleteHandler;

    @Override
    public List<Event> produceMessage(Node message) {
        Long customerId = XPathLong.getValue(customerUpdateXpathExtractor.getOldCustomerId(message));
        List<Event> eventsList = new ArrayList<Event>();
        CustomerLogicalDeleteEvent customerLogicalDeleteEvent = new CustomerLogicalDeleteEvent(customerId);
        //Call Handler to save Event to Cache.
        customerLogicalDeleteHandler.handle(customerLogicalDeleteEvent);
        eventsList.add(customerLogicalDeleteEvent);
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        XPathString infoIsDeleted = customerUpdateXpathExtractor.getNewInfoIsDeleted(message);
        return infoIsDeleted != null && "Y".equals(infoIsDeleted.getValue());
    }
}
