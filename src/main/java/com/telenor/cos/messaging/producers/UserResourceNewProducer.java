package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.UserResource;
import com.telenor.cos.messaging.event.userresource.UserResourceNewEvent;
import com.telenor.cos.messaging.handlers.UserResourceNewHandler;
import com.telenor.cos.messaging.producers.xpath.UserResourceInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;

/**
 * Producer for UserResource New Event.
 *
 * @author Babaprakash D
 */
@Component
public class UserResourceNewProducer extends AbstractUserResourceProducer {

    @Autowired
    private UserResourceInsertXpathExtractor userResourceInsertXpathExtractor;

    @Autowired
    private UserResourceNewHandler userResourceNewHandler;

    /**
     * Translates UserResource Insert Message from Replication Server to UserResourceNewEvent.
     *
     * @param message message from repServer.
     * @return Event UserResourceNewEvent.
     */
    @Override
    public List<Event> produceMessage(Node message) {
        List<Event> newUserresourceEventList = new ArrayList<Event>();
        UserResourceNewEvent userResourceNewEvent = new UserResourceNewEvent(createUserResource(message));
        newUserresourceEventList.add(userResourceNewEvent);
        userResourceNewHandler.handle(userResourceNewEvent);
        return newUserresourceEventList;
    }

    /**
     * This methods checks for csUserId in UserResource Insert Message from ReplicationServer and returns true if
     * csUserId is not null.
     *
     * @param message message received from replicationServer.
     * @return true or false.
     */
    @Override
    public Boolean isApplicable(Node message) {
        return userResourceInsertXpathExtractor.getCsUserId(message) != null;
    }

    private UserResource createUserResource(Node message) {
        Long resourceId = XPathLong.getValue(userResourceInsertXpathExtractor.getResourceId(message));
        String csUserId = userResourceInsertXpathExtractor.getCsUserId(message).getValue();
        return createUserResource(resourceId,csUserId);
    }

}
