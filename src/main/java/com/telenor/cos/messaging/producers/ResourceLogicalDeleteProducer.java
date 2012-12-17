package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.resource.ResourceLogicalDeleteEvent;
import com.telenor.cos.messaging.handlers.ResourceLogicalDeleteHandler;
import com.telenor.cos.messaging.producers.xpath.ResourceUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for resource logical delete
 * 
 * @author Babaprakash D
 * 
 */
@Component
public class ResourceLogicalDeleteProducer extends AbstractResourceProducer {

    @Autowired
    private ResourceUpdateXpathExtractor resourceUpdateXpathExtractor;

    @Autowired
    private ResourceLogicalDeleteHandler resourceLogicalDeleteHandler;

    @Override
    public Boolean isApplicable(Node message) {
        String infoIsDeleted = XPathString.getValue(resourceUpdateXpathExtractor.getNewInfoIsDeleted(message));
        return "Y".equals(infoIsDeleted);
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long resourceId = XPathLong.getValue(resourceUpdateXpathExtractor.getResourceId(message));
        ResourceLogicalDeleteEvent resourceLogicalDeleteEvent = new ResourceLogicalDeleteEvent(resourceId);
        resourceLogicalDeleteHandler.handle(resourceLogicalDeleteEvent);
        return Collections.<Event>singletonList(resourceLogicalDeleteEvent);
    }
}
