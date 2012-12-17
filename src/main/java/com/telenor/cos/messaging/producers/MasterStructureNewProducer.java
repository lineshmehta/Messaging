package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.MasterStructure;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureNewEvent;
import com.telenor.cos.messaging.producers.xpath.MasterStructureInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Producer for a new master structure event.
 * @author t798435
 *
 */
@Component
public class MasterStructureNewProducer extends AbstractProducer {
    
    @Autowired
    private MasterStructureInsertXpathExtractor masterStructureInsertXpathExtractor;

    @Override
    public List<Event> produceMessage(Node message) {
        MasterStructure masterStructure = createMasterStructure(message);
        List<Event> eventsList = new ArrayList<Event>();
        eventsList.add(new MasterStructureNewEvent(masterStructure));
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return masterStructureInsertXpathExtractor.getMasterIdMember(message)!= null;
    }

    /**
     * This method creates MasterStructure object.
     * @param message received from repServer.
     * @return MasterStructure --> masterStructure
     */
    private MasterStructure createMasterStructure(Node message) {
        MasterStructure masterStructure = new MasterStructure();
        masterStructure.setMasterId(XPathLong.getValue(masterStructureInsertXpathExtractor.getMasterIdMember(message)));
        masterStructure.setMasterIdOwner(XPathLong.getValue(masterStructureInsertXpathExtractor.getMasterIdOwner(message)));
        return masterStructure;
    }
}
