package com.telenor.cos.messaging.web.controller.userresource;

import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.UserResource;
import com.telenor.cos.messaging.web.form.UserResourceForm;

public class UserResourceCommonControllerTest {

    protected UserResourceForm createUserResourceForm() {
        UserResourceForm userResource = new UserResourceForm();
        userResource.setCsUserId("cosmaster");
        userResource.setResourceId(Long.valueOf(1));
        return userResource;
    }

    protected UserResource createUserResource(Long resourceId) {
        UserResource userResource = new UserResource();
        Resource resource = new Resource(resourceId);
        userResource.setResource(resource);
        userResource.setUserId("cosmaster");
        resource.setResourceTypeId(0);
        resource.setResourceHasContentInherit(true);
        resource.setResourceHasStructureInherit(true);
        return userResource;
    }
}
