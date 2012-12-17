package com.telenor.cos.messaging.event;

import org.junit.Before;
import org.junit.Test;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.userresource.UserResourceCsUserIdUpdateEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceDeleteEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceNewEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceResourceIdUpdateEvent;
import com.telenor.cos.messaging.util.UserResourceTestHelper;

/**
 * Test case for {@link UserResourceNewEvent} and {@link UserResourceDeleteEvent} and {@link UserResourceResourceIdUpdateEvent}
 * @author Babaprakash D
 *
 */
public class UserResourceEventTest extends AbstractEventTest {

    public static final Long RESOURCE_ID = Long.valueOf(456);
    public static final String CS_USER_ID = "cosmaster";
    private static final Long OLD_RESOURCE_ID = Long.valueOf(123L);
    private static final String OLD_CSUSER_ID = "Test";
    private UserResourceTestHelper userResourceTestHelper;    
   
    @Before
    public void setUp() {
        userResourceTestHelper = new UserResourceTestHelper();
    }

    /**
     * Tests UserResourceNewEvent whether TYPE and ACTION set properly or not.
     */
    @Test
    public void testCreateUserResourceNewEvent() {
        Event event= new UserResourceNewEvent(userResourceTestHelper.createUserResource(RESOURCE_ID, CS_USER_ID));
        assertActionAndType(event,ACTION.CREATED,TYPE.USERRESOURCE);
    }

    /**
     * Tests UserResourceDeleteEvent whether TYPE and ACTION set properly or not.
     */
    @Test
    public void testCreateUserResourceDeleteEvent() {
        Event event= new UserResourceDeleteEvent(userResourceTestHelper.createUserResource(RESOURCE_ID, CS_USER_ID));
        assertActionAndType(event,ACTION.DELETE,TYPE.USERRESOURCE);
    }

    /**
     * Tests UserResourceCsUserIdUpdateEvent whether TYPE and ACTION set properly or not.
     */
    @Test
    public void testCreateUserResourceCsUserIdUpdateEvent() {
        Event event= new UserResourceCsUserIdUpdateEvent(userResourceTestHelper.createUserResource(RESOURCE_ID, CS_USER_ID),OLD_CSUSER_ID);
        assertActionAndType(event,ACTION.CS_USERID_CHANGE,TYPE.USERRESOURCE);
    }

    /**
     * Tests UserResourceResourceIdUpdateEvent whether TYPE and ACTION set properly or not.
     */
    @Test
    public void testCreateUserResourceResourceIdUpdateEvent() {
        Event event= new UserResourceResourceIdUpdateEvent(userResourceTestHelper.createUserResource(RESOURCE_ID, CS_USER_ID),userResourceTestHelper.createUserResource(OLD_RESOURCE_ID, CS_USER_ID));
        assertActionAndType(event,ACTION.RESOURCE_ID_CHANGE,TYPE.USERRESOURCE);
    }
}
