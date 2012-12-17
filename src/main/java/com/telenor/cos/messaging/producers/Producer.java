package com.telenor.cos.messaging.producers;

import java.util.List;

import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;

public interface Producer {

    /**
     * Should this producer handle this message
     *
     * @param message The message
     * @return true if producer should handle this message
     */
    Boolean isApplicable(Node message);

    /**
     * Create a domain specific event from this message
     *
     * @param message the message
     * @return the domain event
     */
    List<Event> produceMessage(Node message);

}