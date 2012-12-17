package com.telenor.cos.messaging.handlers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.telenor.cos.messaging.event.userresource.UserResourceNewEvent;

/**
 * Test case for {@link UserResourceNewHandler}
 * @author Babaprakash D
 *
 */
public class UserResourceNewHandlerTest extends UserResourceCommonHandlerTest {

    private UserResourceNewHandler userResourceNewHandler;

    @Before
    public void setUp() {
        super.setUp();
        userResourceNewHandler = new UserResourceNewHandler();
        ReflectionTestUtils.setField(userResourceNewHandler, "userResourceCache", getUserResourceCache());
    }

    @Test
    public void addUserIdWithExistingResourceId() {
        List<String> csUserIdsList = createCsUserIdsList();
        when(getUserResourceCache().get(any(Long.class))).thenReturn(csUserIdsList);
        UserResourceNewEvent userResourceNewEvent = new UserResourceNewEvent(getUserResourceTestHelper().createUserResource(RESOURCE_ID,"test3"));
        userResourceNewHandler.handle(userResourceNewEvent);
        verify(getUserResourceCache()).insert(RESOURCE_ID,csUserIdsList);
    }

    @Test
    public void addUserIdWithNewResourceId() {
        when(getUserResourceCache().get(any(Long.class))).thenReturn(null);
        UserResourceNewEvent userResourceNewEvent = new UserResourceNewEvent(getUserResourceTestHelper().createUserResource(RESOURCE_ID,"test3"));
        userResourceNewHandler.handle(userResourceNewEvent);
        List<String> csUserIdsList = new ArrayList<String>();
        csUserIdsList.add("test3");
        verify(getUserResourceCache()).insert(RESOURCE_ID,csUserIdsList);
    }
}
