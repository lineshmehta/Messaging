package com.telenor.cos.messaging.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.springframework.ui.Model;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;

/**
 * Helper class for web controller tests
 *
 * @author t828553
 *
 */
public final class ControllerAssertUtil {

    private ControllerAssertUtil(){
    }

    /**
     * Gets the event from the model and checks on standard fields.
     *
     * @param domainId
     *            the domainId
     * @param action
     *            teh ACTION
     * @param model
     *            the model
     * @return the event
     */
    public static Event checkControllerOutput(Long domainId, ACTION action, Model model) {
        assertTrue("Model did not contain attribute result", model.containsAttribute("result"));
        Event event = (Event) model.asMap().get("result");
        assertNotNull(event);
        assertEquals(action, event.getAction());
        assertEquals(domainId, event.getDomainId());
        return event;
    }

}
