package com.telenor.cos.messaging.jdbm.stub;

import java.util.HashMap;

import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;

public class SubscriptionTypeCacheStub extends SubscriptionTypeCache {

    /**
     * Constructor
     *
     * @param path
     *            the path
     */
    public SubscriptionTypeCacheStub(String path) {
        super(path);
        setMap(new HashMap<String, String>());
    }

}
