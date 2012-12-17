package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.UserResource;
import com.telenor.cos.messaging.event.userresource.UserResourceDeleteEvent;
import com.telenor.cos.messaging.handlers.UserResourceDeleteHandler;
import com.telenor.cos.messaging.producers.xpath.UserResourceDeleteXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;

/**
 * Producer for UserResourceDelete Event.
 *
 * @author Babaprakash D
 */
@Component
public class UserResourceDeleteProducer extends AbstractUserResourceProducer {

    @Autowired
    private UserResourceDeleteXpathExtractor userResourceDeleteXpathExtractor;

    @Autowired
    private UserResourceDeleteHandler userResourceDeleteHandler;

    /**
     * Translates UserResource delete message from Repserver to UserResourceDeleteEvent.
     *
     * @param message message from repServer.
     * @return Event UserResourceDeleteEvent.
     */
    @Override
    public List<Event> produceMessage(Node message) {
        List<Event> userresourceDeleteEventList = new ArrayList<Event>();
        UserResourceDeleteEvent userResourceDeleteEvent = new UserResourceDeleteEvent(createUserResource(message));
        userresourceDeleteEventList.add(userResourceDeleteEvent);
        userResourceDeleteHandler.handle(userResourceDeleteEvent);
        return userresourceDeleteEventList;
    }

    /**
     * Filters Message from ReplicationServer.
     *
     * @param message message from repServer.
     * @return true if message is applicable to producer.
     */
    @Override
    public Boolean isApplicable(Node message) {
        return userResourceDeleteXpathExtractor.getCsUserId(message) != null;
    }

    private UserResource createUserResource(Node message) {
        Long resourceId = XPathLong.getValue(userResourceDeleteXpathExtractor.getResourceId(message));
        String csUserId = userResourceDeleteXpathExtractor.getCsUserId(message).getValue();
        return createUserResource(resourceId,csUserId);
    }

}
