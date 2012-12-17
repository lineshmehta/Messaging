package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.MasterCustomerUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;


@Component
public class MasterCustomerLogicalDeleteProducer extends AbstractProducer {

    @Autowired
    private MasterCustomerUpdateXpathExtractor masterCustomerUpdateXpathExtractor;

    @Override
    public List<Event> produceMessage(Node message) {
        Long masterId = XPathLong.getValue(masterCustomerUpdateXpathExtractor.getOldMasterCustomerId(message));
        List<Event> eventsList = new ArrayList<Event>();
        eventsList.add(new MasterCustomerLogicalDeleteEvent(masterId));
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        String infoIsDeleted = XPathString.getValue(masterCustomerUpdateXpathExtractor.getNewInfoIsDeleted(message));
        return "Y".equals(infoIsDeleted);

    }
}
