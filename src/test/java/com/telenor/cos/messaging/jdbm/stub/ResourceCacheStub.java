package com.telenor.cos.messaging.jdbm.stub;

import java.util.HashMap;

import com.telenor.cos.messaging.jdbm.CachableResource;
import com.telenor.cos.messaging.jdbm.ResourceCache;

public class ResourceCacheStub extends ResourceCache {

    /**
     * Constructor
     *
     * @param path
     *            the path
     */
    public ResourceCacheStub(String path) {
        super(path);
        setMap(new HashMap<Long, CachableResource>());
    }

}
