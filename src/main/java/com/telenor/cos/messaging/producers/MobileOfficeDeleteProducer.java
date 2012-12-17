package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.MobileOfficeUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for {@link com.telenor.cos.messaging.event.mobileoffice.MobileOfficeDeleteEvent}.
 */
@Component
public class MobileOfficeDeleteProducer extends AbstractProducer {

    @Autowired
    private MobileOfficeUpdateXpathExtractor mobileOfficeUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        String infoIsDeleted = XPathString.getValue(mobileOfficeUpdateXpathExtractor.getInfoIsDeleted(message));
        return "Y".equals(infoIsDeleted);
    }

    @Override
    public List<Event> produceMessage(Node message) {
        String directoryNumber = XPathString.getValue(mobileOfficeUpdateXpathExtractor.getDirectoryNumberOld(message));
        String oldExtensionNumber = XPathString.getValue(mobileOfficeUpdateXpathExtractor.getExtensionNumberOld(message));
        MobileOfficeDeleteEvent mobileOfficeDeleteEvent = new MobileOfficeDeleteEvent(directoryNumber, oldExtensionNumber);
        return Collections.<Event>singletonList(mobileOfficeDeleteEvent);
    }
}
