package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceContentInheritUpdateEvent;
import com.telenor.cos.messaging.handlers.ResourceContentInheritUpdateHandler;
import com.telenor.cos.messaging.jdbm.UserResourceCache;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for {@link com.telenor.cos.messaging.event.resource.ResourceContentInheritUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class ResourceContentInheritUpdateProducer extends AbstractResourceProducer {

    @Autowired
    private UserResourceCache userResourceCache;

    @Autowired
    private ResourceContentInheritUpdateHandler resourceContentInheritUpdateHandler;

    @Override
    public Boolean isApplicable(Node message) {
        XPathString newResourceHasContentInherit = getResourceUpdateXpathExtractor().getNewResourceHasContentInherit(message);
        return newResourceHasContentInherit != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Resource resourceWithOldValues = createResourceWithOldValues(message);
        List<String> csUserIdsListForResourceId = userResourceCache.get(resourceWithOldValues.getResourceId());
        boolean newResourceHasContentInherit = "Y".equals(XPathString.getValue(getResourceUpdateXpathExtractor().getNewResourceHasContentInherit(message)));
        ResourceContentInheritUpdateEvent resourceContentInheritUpdateEvent = new ResourceContentInheritUpdateEvent(resourceWithOldValues,csUserIdsListForResourceId,newResourceHasContentInherit);
        resourceContentInheritUpdateHandler.handle(resourceContentInheritUpdateEvent);
        return Collections.<Event>singletonList(resourceContentInheritUpdateEvent);
    }
}
