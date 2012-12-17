package com.telenor.cos.messaging.event;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.usermapping.TnuIdUserMappingNewEvent;
import com.telenor.cos.test.suite.UnitTests;

/**
 * Test Case for @link {@link TnuIdUserMappingNewEvent}
 * @author Babaprakash D
 *
 */
@Category(UnitTests.class)
public class TnuIdUserMappingNewEventTest extends AbstractEventTest {
    
    @Test
    public void testUserMappingEvent() {
        TnuIdUserMapping userMapping = new TnuIdUserMapping();
        userMapping.setApplicationId(62);
        userMapping.setCosSecurityUserId("cos_test_large1");
        userMapping.setTelenorUserId("t808074");
        Event event = new TnuIdUserMappingNewEvent(userMapping);
        assertActionAndType(event,ACTION.CREATED,TYPE.USERMAPPING);
    }
}
