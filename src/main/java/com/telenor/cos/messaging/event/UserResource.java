package com.telenor.cos.messaging.event;

import java.io.Serializable;

/**
 * Domain Object of UserResource.
 * @author Babaprakash D
 *
 */
public class UserResource implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8739238523620569254L;

    private String userId;

    private Resource resource;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
