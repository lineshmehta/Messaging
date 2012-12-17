package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.UserResource;
import com.telenor.cos.messaging.event.userresource.UserResourceCsUserIdUpdateEvent;
import com.telenor.cos.messaging.handlers.UserResourceCsUserIdUpdateHandler;
import com.telenor.cos.messaging.producers.xpath.UserResourceUpdateXpathExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

@Component
public class UserResourceCsUserIdUpdateProducer extends AbstractUserResourceProducer {

    @Autowired
    private UserResourceUpdateXpathExtractor userResourceUpdateXpathExtractor;

    @Autowired
    private UserResourceCsUserIdUpdateHandler userResourceCsUserIdUpdateHandler;

    @Override
    public Boolean isApplicable(Node message) {
        return userResourceUpdateXpathExtractor.getCsUserId(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        String csUserIdOld = userResourceUpdateXpathExtractor.getCsUserIdOld(message).getValue();
        String csUserIdNew = userResourceUpdateXpathExtractor.getCsUserId(message).getValue();
        Long resourceIdOld = userResourceUpdateXpathExtractor.getResourceIdOld(message).getValue();
        UserResource userResource = createUserResource(resourceIdOld,csUserIdNew);
        UserResourceCsUserIdUpdateEvent userResourceCsUserIdUpdateEvent = new UserResourceCsUserIdUpdateEvent(userResource, csUserIdOld);
        userResourceCsUserIdUpdateHandler.handle(userResourceCsUserIdUpdateEvent);
        return Collections.<Event>singletonList(new UserResourceCsUserIdUpdateEvent(userResource, csUserIdOld));
    }
}
