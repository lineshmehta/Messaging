package com.telenor.cos.messaging.handlers;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.telenor.cos.messaging.jdbm.UserResourceCache;
import com.telenor.cos.messaging.util.UserResourceTestHelper;

public class UserResourceCommonHandlerTest {

    public static final Long RESOURCE_ID = Long.valueOf(999);
    public static final Long RESOURCE_ID2 = Long.valueOf(998);

    @Mock
    private UserResourceCache userResourceCache;

    private UserResourceTestHelper userResourceTestHelper;

    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userResourceTestHelper = new UserResourceTestHelper();
    }

    public UserResourceCache getUserResourceCache() {
        return userResourceCache;
    }

    public UserResourceTestHelper getUserResourceTestHelper() {
        return userResourceTestHelper;
    }

    protected List<String> createCsUserIdsList() {
        List<String> csUserIdsList = new ArrayList<String>();
        csUserIdsList.add("test");
        csUserIdsList.add("test1");
        csUserIdsList.add("test2");
        return csUserIdsList;
    }
}
