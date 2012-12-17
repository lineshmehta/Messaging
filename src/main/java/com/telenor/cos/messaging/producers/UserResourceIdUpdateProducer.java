package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.UserResource;
import com.telenor.cos.messaging.event.userresource.UserResourceResourceIdUpdateEvent;
import com.telenor.cos.messaging.handlers.UserResourceResourceIdUpdateHandler;
import com.telenor.cos.messaging.producers.xpath.UserResourceUpdateXpathExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Per Jørgen Walstrøm
 */
@Component
public class UserResourceIdUpdateProducer extends AbstractUserResourceProducer  {

    @Autowired
    private UserResourceUpdateXpathExtractor userResourceUpdateXpathExtractor;
    
    @Autowired
    private UserResourceResourceIdUpdateHandler userResourceResourceIdUpdateHandler;

    @Override
    public Boolean isApplicable(Node message) {
        return userResourceUpdateXpathExtractor.getResourceId(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        String csUserIdOld = userResourceUpdateXpathExtractor.getCsUserIdOld(message).getValue();
        Long resourceIdOld = userResourceUpdateXpathExtractor.getResourceIdOld(message).getValue();
        Long resourceIdNew = userResourceUpdateXpathExtractor.getResourceId(message).getValue();

        UserResource newUserResource = createUserResource(resourceIdNew, csUserIdOld);
        UserResource oldUserResource = createUserResource(resourceIdOld,csUserIdOld);

        UserResourceResourceIdUpdateEvent userResourceResourceIdUpdateEvent = new UserResourceResourceIdUpdateEvent(newUserResource,oldUserResource);
        userResourceResourceIdUpdateHandler.handle(userResourceResourceIdUpdateEvent);
        return Collections.<Event>singletonList(new UserResourceResourceIdUpdateEvent(newUserResource,oldUserResource));
    }
}
