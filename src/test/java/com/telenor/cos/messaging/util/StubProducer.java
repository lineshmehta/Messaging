package com.telenor.cos.messaging.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.producers.AbstractProducer;

public class StubProducer extends AbstractProducer {
    @Override
    public List<Event> produceMessage(Node message) {
        List<Event> eventsList = new ArrayList<Event>();
        eventsList.add(new StubEvent());
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return true;
    }

}
