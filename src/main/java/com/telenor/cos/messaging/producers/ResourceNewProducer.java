package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceNewEvent;
import com.telenor.cos.messaging.handlers.ResourceNewHandler;
import com.telenor.cos.messaging.producers.xpath.ResourceInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Producer for New Resource Events.
 * @author Babaprakash D
 *
 */
@Component
public class ResourceNewProducer extends AbstractResourceProducer {

    @Autowired
    private ResourceInsertXpathExtractor resourceInsertXpathExtractor;

    @Autowired
    private ResourceNewHandler resourceNewHandler;

    @Override
    public Boolean isApplicable(Node message) {
        return resourceInsertXpathExtractor.getResourceId(message)!= null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        List<Event> resourceNewEventList = new ArrayList<Event>();
        Resource resource = createResource(message);
        ResourceNewEvent resourceNewEvent = new ResourceNewEvent(resource);
        resourceNewEventList.add(resourceNewEvent);
        resourceNewHandler.handle(resourceNewEvent);
        return resourceNewEventList;
    }

    private Resource createResource(Node message) {
        Resource resource = new Resource(XPathLong.getValue(resourceInsertXpathExtractor.getResourceId(message)));
        Integer resourceTypeId = XPathInteger.getValue(resourceInsertXpathExtractor.getResourceTypeId(message));
        String resourceTypeIdKey = XPathString.getValue(resourceInsertXpathExtractor.getResourceTypeIdKey(message));
        resource.setResourceTypeIdKey(getResourceTypeIdKeyFromMessageOrCache(resourceTypeId, resourceTypeIdKey));
        resource.setResourceTypeId(getResourceTypeId(resourceTypeId));
        resource.setResourceHasContentInherit("Y".equals(XPathString.getValue(resourceInsertXpathExtractor.getResourceHasContentInherit(message))));
        resource.setResourceHasStructureInherit("Y".equals(XPathString.getValue(resourceInsertXpathExtractor.getResourceHasStructureInherit(message))));
        return resource;
    }
}