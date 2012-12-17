package com.telenor.cos.messaging.event;

import org.junit.Test;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNewEvent;

/**
 * Test Case for @link {@link MasterCustomerNewEvent}
 * @author Babaprakash D
 *
 */
public class MasterCustomerEventNewTest extends AbstractEventTest {
    
    /**
     * Tests whether TYPE and ACTION set properly or not.
     */
    @Test
    public void testCreateMCNewEvent() {
        MasterCustomer masterCustomer = new MasterCustomer();
        masterCustomer.setMasterId(1234L);
        Event event= new MasterCustomerNewEvent(masterCustomer);
        assertActionAndType(event,ACTION.CREATED,TYPE.MASTERCUSTOMER);
    }
}
