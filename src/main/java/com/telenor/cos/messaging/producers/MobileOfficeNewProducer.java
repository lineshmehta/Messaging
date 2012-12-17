package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeNewEvent;
import com.telenor.cos.messaging.producers.xpath.MobileOfficeInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Component
public class MobileOfficeNewProducer extends AbstractProducer {

    @Autowired
    private MobileOfficeInsertXpathExtractor mobileOfferInsertXpathExtractor;

    @Override
    public List<Event> produceMessage(Node message) {
        String directoryNumber = XPathString.getValue(mobileOfferInsertXpathExtractor.getDirectoryNumber(message));
        String extensionNumber = XPathString.getValue(mobileOfferInsertXpathExtractor.getExtensionNumber(message));
        List<Event> eventList = new ArrayList<Event>();
        MobileOfficeNewEvent event = new MobileOfficeNewEvent(directoryNumber, extensionNumber);
        eventList.add(event);
        return eventList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return mobileOfferInsertXpathExtractor.getDirectoryNumber(message) != null
                && mobileOfferInsertXpathExtractor.getExtensionNumber(message) != null;
    }
}
