package com.telenor.cos.messaging.jdbm.stub;

import java.util.HashMap;

import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;

public class CustomerCacheStub extends CustomerCache {

    /**
     * constructor
     *
     * @param path
     *            the path
     */
    public CustomerCacheStub(String path) {
        super(path);
        setMap(new HashMap<Long, CachableCustomer>());
    }
}