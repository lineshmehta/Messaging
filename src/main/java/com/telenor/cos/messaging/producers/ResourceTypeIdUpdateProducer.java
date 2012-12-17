package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceTypeIdUpdateEvent;
import com.telenor.cos.messaging.handlers.ResourceTypeIdUpdateHandler;
import com.telenor.cos.messaging.jdbm.UserResourceCache;
import com.telenor.cos.messaging.producers.xpath.type.XPathInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for {@link com.telenor.cos.messaging.event.resource.ResourceTypeIdUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class ResourceTypeIdUpdateProducer extends AbstractResourceProducer {

    @Autowired
    private UserResourceCache userResourceCache;

    @Autowired
    private ResourceTypeIdUpdateHandler resourceTypeIdUpdateHandler;

    @Override
    public Boolean isApplicable(Node message) {
        return getResourceUpdateXpathExtractor().getNewResourceTypeId(message)!= null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Resource resourceWithOldValues = createResourceWithOldValues(message);
        List<String> csUserIdsListForResourceId = userResourceCache.get(resourceWithOldValues.getResourceId());
        Integer newResourceTypeId = XPathInteger.getValue(getResourceUpdateXpathExtractor().getNewResourceTypeId(message));
        Integer resourceTypeId = getResourceTypeId(newResourceTypeId);
        ResourceTypeIdUpdateEvent resourceTypeIdUpdateEvent = new ResourceTypeIdUpdateEvent(resourceWithOldValues,csUserIdsListForResourceId,resourceTypeId);
        resourceTypeIdUpdateHandler.handle(resourceTypeIdUpdateEvent);
        return Collections.<Event>singletonList(resourceTypeIdUpdateEvent);
    }
}
