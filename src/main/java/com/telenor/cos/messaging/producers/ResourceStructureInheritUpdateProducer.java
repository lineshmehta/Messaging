package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceStructureInheritUpdateEvent;
import com.telenor.cos.messaging.handlers.ResourceStructureInheritUpdateHandler;
import com.telenor.cos.messaging.jdbm.UserResourceCache;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for {@link com.telenor.cos.messaging.event.resource.ResourceStructureInheritUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class ResourceStructureInheritUpdateProducer extends AbstractResourceProducer {

    @Autowired
    private UserResourceCache userResourceCache;

    @Autowired
    private ResourceStructureInheritUpdateHandler resourceStructureInheritUpdateHandler;

    @Override
    public Boolean isApplicable(Node message) {
        XPathString newResourceHasStructureInherit = getResourceUpdateXpathExtractor().getNewResourceHasStructureInherit(message);
        return newResourceHasStructureInherit != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Resource resourceWithOldValues = createResourceWithOldValues(message);
        List<String> csUserIdsListForResourceId = userResourceCache.get(resourceWithOldValues.getResourceId());
        boolean newResourceHasStructureInherit = "Y".equals(XPathString.getValue(getResourceUpdateXpathExtractor().getNewResourceHasStructureInherit(message)));
        ResourceStructureInheritUpdateEvent resourceStructureInheritUpdateEvent = new ResourceStructureInheritUpdateEvent(resourceWithOldValues,csUserIdsListForResourceId,newResourceHasStructureInherit);
        resourceStructureInheritUpdateHandler.handle(resourceStructureInheritUpdateEvent);
        return Collections.<Event>singletonList(resourceStructureInheritUpdateEvent);

    }
}
