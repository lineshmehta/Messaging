package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceTypeIdKeyUpdateEvent;
import com.telenor.cos.messaging.handlers.ResourceTypeIdKeyUpdateHandler;
import com.telenor.cos.messaging.jdbm.UserResourceCache;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for {@link com.telenor.cos.messaging.event.resource.ResourceTypeIdKeyUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class ResourceTypeIdKeyUpdateProducer extends AbstractResourceProducer {

    @Autowired
    private UserResourceCache userResourceCache;

    @Autowired
    private ResourceTypeIdKeyUpdateHandler resourceTypeIdKeyUpdateHandler;

    @Override
    public Boolean isApplicable(Node message) {
        return getResourceUpdateXpathExtractor().getNewResourceTypeIdKey(message)!= null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Resource resourceWithOldValues = createResourceWithOldValues(message);
        List<String> csUserIdsListForResourceId = userResourceCache.get(resourceWithOldValues.getResourceId());
        String newResourceTypeIdKey = XPathString.getValue(getResourceUpdateXpathExtractor().getNewResourceTypeIdKey(message));
        ResourceTypeIdKeyUpdateEvent resourceTypeIdKeyUpdateEvent = new ResourceTypeIdKeyUpdateEvent(resourceWithOldValues,csUserIdsListForResourceId,newResourceTypeIdKey);
        resourceTypeIdKeyUpdateHandler.handle(resourceTypeIdKeyUpdateEvent);
        return Collections.<Event>singletonList(resourceTypeIdKeyUpdateEvent);
    }
}