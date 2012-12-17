package com.telenor.cos.messaging.jdbm;

import java.util.List;

/**
 * A persistent Map which contains List of csUserIds for resourceId.
 *
 * @author Babaprakash D
 */
public class UserResourceCache extends CommonCache<Long, List<String>> {

    private static final String NAME = "userResourceCache";

    /**
     * Constructor of UserResourceCache.
     * @param pathToCache the path to the cache store
     */
    public UserResourceCache(String pathToCache) {
        validatePathAndCreateDb(pathToCache, NAME);
    }
}