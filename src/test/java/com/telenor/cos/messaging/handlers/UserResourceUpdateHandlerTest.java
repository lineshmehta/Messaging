package com.telenor.cos.messaging.handlers;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.telenor.cos.messaging.event.UserResource;
import com.telenor.cos.messaging.event.userresource.UserResourceCsUserIdUpdateEvent;
import com.telenor.cos.messaging.event.userresource.UserResourceResourceIdUpdateEvent;

/**
 * Test case for {@link UserResourceResourceIdUpdateHandler} and {@link UserResourceCsUserIdUpdateHandler}
 * @author Babaprakash D
 *
 */
public class UserResourceUpdateHandlerTest extends UserResourceCommonHandlerTest {

    private UserResourceResourceIdUpdateHandler userResourceResourceIdUpdateHandler;

    private UserResourceCsUserIdUpdateHandler userResourceCsUserIdUpdateHandler;

    @Before
    public void setUp() {
        super.setUp();
        userResourceResourceIdUpdateHandler = new UserResourceResourceIdUpdateHandler();
        userResourceCsUserIdUpdateHandler = new UserResourceCsUserIdUpdateHandler();
        ReflectionTestUtils.setField(userResourceResourceIdUpdateHandler, "userResourceCache", getUserResourceCache());
        ReflectionTestUtils.setField(userResourceCsUserIdUpdateHandler, "userResourceCache", getUserResourceCache());
    }

    @Test
    public void testResourceIdUpdate() {
        when(getUserResourceCache().get(any(Long.class))).thenReturn(null);
        UserResource newUserResource = getUserResourceTestHelper().createUserResource(RESOURCE_ID2,"test2");
        UserResource oldUserResource = getUserResourceTestHelper().createUserResource(RESOURCE_ID,null);
        UserResourceResourceIdUpdateEvent userResourceResourceIdUpdateEvent = new UserResourceResourceIdUpdateEvent(newUserResource,oldUserResource);
        userResourceResourceIdUpdateHandler.handle(userResourceResourceIdUpdateEvent);
        List<String> csUserIdsList = new ArrayList<String>();
        csUserIdsList.add("test2");
        verify(getUserResourceCache()).insert(RESOURCE_ID2, csUserIdsList);
    }

    @Test
    public void testResourceIdUpdateWithCsUserIds() {
        List<String> csUserIdsList = createCsUserIdsList();
        when(getUserResourceCache().get(RESOURCE_ID)).thenReturn(createCsUserIdsList());
        List<String> csUserIdsList2 = new ArrayList<String>();
        csUserIdsList2.add("test4");
        csUserIdsList2.add("test5");
        csUserIdsList2.add("test6");
        when(getUserResourceCache().get(RESOURCE_ID2)).thenReturn(csUserIdsList2);
        UserResource newUserResource = getUserResourceTestHelper().createUserResource(RESOURCE_ID2,"test2");
        UserResource oldUserResource = getUserResourceTestHelper().createUserResource(RESOURCE_ID,null);
        UserResourceResourceIdUpdateEvent userResourceResourceIdUpdateEvent = new UserResourceResourceIdUpdateEvent(newUserResource,oldUserResource);
        userResourceResourceIdUpdateHandler.handle(userResourceResourceIdUpdateEvent);
        csUserIdsList.remove("test2");
        verify(getUserResourceCache(),times(1)).insert(RESOURCE_ID, csUserIdsList);
        verify(getUserResourceCache(),times(1)).insert(RESOURCE_ID2, csUserIdsList2);
    }

    @Test
    public void testCsUserIdUpdate() {
        when(getUserResourceCache().get(RESOURCE_ID)).thenReturn(createCsUserIdsList());
        UserResource userResource = getUserResourceTestHelper().createUserResource(RESOURCE_ID,"test6");
        UserResourceCsUserIdUpdateEvent userResourceCsUserIdUpdateEvent = new UserResourceCsUserIdUpdateEvent(userResource, "test1");
        userResourceCsUserIdUpdateHandler.handle(userResourceCsUserIdUpdateEvent);
        List<String> csUserIdsListForResourceId = getUserResourceCache().get(RESOURCE_ID);
        assertFalse(csUserIdsListForResourceId.contains("test1"));
    }
}
