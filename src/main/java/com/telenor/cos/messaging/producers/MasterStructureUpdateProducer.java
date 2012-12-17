package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import com.telenor.cos.messaging.event.MasterStructure;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.producers.xpath.MasterStructureUpdateXpathExtractor;

/**
 * @author t798435
 */
@Component
public class MasterStructureUpdateProducer extends AbstractProducer {
    
    @Autowired
    private MasterStructureUpdateXpathExtractor masterStructureUpdateXpathExtractor;

    @Override
    public List<Event> produceMessage(Node message) {
        List<Event> masterStructureUpdateList = new ArrayList<Event>();
        Long masterId = XPathLong.getValue(masterStructureUpdateXpathExtractor.getOldMastIdMember(message));
        MasterStructure masterStructure = new MasterStructure();
        masterStructure.setMasterIdOwner(XPathLong.getValue(masterStructureUpdateXpathExtractor.getNewMastIdOwner(message)));
        MasterStructureUpdateEvent masterStructureUpdateEvent= new MasterStructureUpdateEvent(masterId, masterStructure);
        masterStructureUpdateList.add(masterStructureUpdateEvent);
        return masterStructureUpdateList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return masterStructureUpdateXpathExtractor.getNewMastIdOwner(message) != null;
    }
}