package com.telenor.cos.messaging.jdbm.stub;

import java.util.HashMap;
import java.util.List;

import com.telenor.cos.messaging.jdbm.UserResourceCache;

public class UserResourceCacheStub extends UserResourceCache {

    /**
     * Constructor
     *
     * @param path
     *            the path
     */
    public UserResourceCacheStub(String path) {
        super(path);
        setMap(new HashMap<Long, List<String>>());
    }

}
