package com.telenor.cos.messaging.handlers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.telenor.cos.messaging.event.UserResource;
import com.telenor.cos.messaging.event.userresource.UserResourceDeleteEvent;

/**
 * Test case for {@link UserResourceDeleteHandler}
 * @author Babaprakash D
 *
 */
public class UserResourceDeleteHandlerTest extends UserResourceCommonHandlerTest {

    private UserResourceDeleteHandler userResourceDeleteHandler;

    @Before
    public void setUp() {
        super.setUp();
        userResourceDeleteHandler = new UserResourceDeleteHandler();
        ReflectionTestUtils.setField(userResourceDeleteHandler, "userResourceCache", getUserResourceCache());
    }

    @Test
    public void testHandle() throws Exception {
        List<String> csUserIdsList = createCsUserIdsList();
        when(getUserResourceCache().get(any(Long.class))).thenReturn(csUserIdsList);
        UserResource userResource = getUserResourceTestHelper().createUserResource(RESOURCE_ID, "test2");
        UserResourceDeleteEvent userResourceDeleteEvent = new UserResourceDeleteEvent(userResource);
        userResourceDeleteHandler.handle(userResourceDeleteEvent);
        csUserIdsList.remove("test2");
        verify(getUserResourceCache()).insert(RESOURCE_ID,csUserIdsList);
    }
}
