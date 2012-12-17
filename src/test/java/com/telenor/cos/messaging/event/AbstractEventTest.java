package com.telenor.cos.messaging.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;

public abstract class AbstractEventTest {

    public void assertActionAndType(Event event,ACTION action,TYPE type) {
        assertNotNull(event);
        assertEquals("Unexpected Action",action,event.getAction());
        assertEquals("Unexpected Type",type,event.getType());
    }
}
