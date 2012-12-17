package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.MobileOfficeUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class MobileOfficeUpdateProducer extends AbstractProducer {

    @Autowired
    private MobileOfficeUpdateXpathExtractor mobileOfficeUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return mobileOfficeUpdateXpathExtractor.getExtensionNumber(message) != null &&
                    mobileOfficeUpdateXpathExtractor.getExtensionNumberOld(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        String directoryNumber = XPathString.getValue(mobileOfficeUpdateXpathExtractor.getDirectoryNumberOld(message));
        String extensionNumber = XPathString.getValue(mobileOfficeUpdateXpathExtractor.getExtensionNumber(message));
        String extensionNumberOld = XPathString.getValue(mobileOfficeUpdateXpathExtractor.getExtensionNumberOld(message));

        MobileOfficeUpdateEvent event = new MobileOfficeUpdateEvent(directoryNumber, extensionNumber, extensionNumberOld);
        return Collections.<Event>singletonList(event);
    }
}
