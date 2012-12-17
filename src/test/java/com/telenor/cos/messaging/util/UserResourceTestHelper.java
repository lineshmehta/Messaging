package com.telenor.cos.messaging.util;

import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.UserResource;

public class UserResourceTestHelper {

    public UserResource createUserResource(Long resourceId,String csUserId) {
        UserResource userResource = new UserResource();
        Resource resource = new Resource(resourceId);
        userResource.setResource(resource);
        userResource.setUserId(csUserId);
        return userResource;
    }
}
