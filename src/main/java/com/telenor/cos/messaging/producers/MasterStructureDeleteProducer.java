package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.MasterStructureUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author t798435
 */
@Component
public class MasterStructureDeleteProducer extends AbstractProducer {

    @Autowired
    private MasterStructureUpdateXpathExtractor masterStructureUpdateXpathExtractor;

    @Override
    public List<Event> produceMessage(Node message) {
        Long masterIdMember = XPathLong.getValue(masterStructureUpdateXpathExtractor.getOldMastIdMember(message));
        List<Event> eventsList = new ArrayList<Event>();
        eventsList.add(new MasterStructureDeleteEvent(masterIdMember));
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        String newInfoIsDeleted = XPathString.getValue(masterStructureUpdateXpathExtractor.getNewInfoIsDeleted(message));
        return "Y".equals(newInfoIsDeleted);
    }
}
