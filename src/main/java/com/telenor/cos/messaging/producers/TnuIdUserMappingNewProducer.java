package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.TnuIdUserMapping;
import com.telenor.cos.messaging.event.usermapping.TnuIdUserMappingNewEvent;
import com.telenor.cos.messaging.producers.xpath.TnuIdUserMappingInsertXPathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Producer For TnuIdUserMapping Event.
 * @author Babaprakash D
 *
 */
@Component
public class TnuIdUserMappingNewProducer extends AbstractProducer {

    @Autowired
    private TnuIdUserMappingInsertXPathExtractor tnuIdUserMappingInsertXPathExtractor;

    /**
     * Translates Message to Event.
     * @param message received from repServer.
     * @return TnuIdUserMappingNewEvent.
     */
    @Override
    public List<Event> produceMessage(Node message) {
        TnuIdUserMapping userMapping = createTnuIdUserMapping(message);
        List<Event> userMappingEventList = new ArrayList<Event>();
        userMappingEventList.add(new TnuIdUserMappingNewEvent(userMapping));
        return userMappingEventList;
    }
    /**
     * @param message received from repServer.
     * @return true if message is applicable to producer.
     */
    @Override
    public Boolean isApplicable(Node message) {
        return tnuIdUserMappingInsertXPathExtractor.getTelenorUserId(message) != null;
    }

    private TnuIdUserMapping createTnuIdUserMapping(Node message) {
        TnuIdUserMapping userMapping = new TnuIdUserMapping();
        userMapping.setApplicationId(XPathInteger.getValue(tnuIdUserMappingInsertXPathExtractor.getApplicationId(message)));
        userMapping.setCosSecurityUserId(XPathString.getValue(tnuIdUserMappingInsertXPathExtractor.getCosSecurityUserId(message)));
        userMapping.setTelenorUserId(tnuIdUserMappingInsertXPathExtractor.getTelenorUserId(message).getValue());
        return userMapping;
    }
}
