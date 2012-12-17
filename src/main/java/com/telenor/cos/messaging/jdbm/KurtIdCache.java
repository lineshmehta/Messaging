package com.telenor.cos.messaging.jdbm;


/**
 * A persistent Map which contains KurtId and masterId(CMMaster-->MasterCustomer Table).
 * @author Babaprakash D
 *
 */
public class KurtIdCache extends CommonCache<Long, Long> {

    private static final String NAME = "masterKurtIdCache";

    /**
     * Constructor of KurtIdCache.
     * @param pathToCache the path to the cache store
     */
    public KurtIdCache(String pathToCache) {
        validatePathAndCreateDb(pathToCache, NAME);
    }
}
