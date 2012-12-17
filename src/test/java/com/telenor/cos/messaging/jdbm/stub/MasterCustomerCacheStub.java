package com.telenor.cos.messaging.jdbm.stub;

import java.util.HashMap;

import com.telenor.cos.messaging.jdbm.MasterCustomerCache;

public class MasterCustomerCacheStub extends MasterCustomerCache {

    /**
     * Constructor
     *
     * @param path
     *            the path
     */
    public MasterCustomerCacheStub(String path) {
        super(path);
        setMap(new HashMap<Long, Long>());
    }

}
