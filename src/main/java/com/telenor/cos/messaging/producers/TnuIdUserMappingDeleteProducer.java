package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.usermapping.TnuidUserMappingDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.TnuIdUserMappingDeleteXPathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Component
public class TnuIdUserMappingDeleteProducer extends AbstractProducer {

    @Autowired
    private TnuIdUserMappingDeleteXPathExtractor tnuIdUserMappingDeleteXPathExtractor;

    @Override
    public List<Event> produceMessage(Node message) {
        String telenorUserId = tnuIdUserMappingDeleteXPathExtractor.getTelenorUserIdOld(message).getValue();
        Integer applicationId = XPathInteger.getValue(tnuIdUserMappingDeleteXPathExtractor.getApplicationIdOld(message));
        TnuidUserMappingDeleteEvent event = new TnuidUserMappingDeleteEvent(telenorUserId, applicationId);
        List<Event> tnuidUserMappingDeleteEventList = new ArrayList<Event>();
        tnuidUserMappingDeleteEventList.add(event);
        return tnuidUserMappingDeleteEventList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return tnuIdUserMappingDeleteXPathExtractor.getTelenorUserIdOld(message) != null;
    }

}
