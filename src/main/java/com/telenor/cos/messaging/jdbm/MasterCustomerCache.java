package com.telenor.cos.messaging.jdbm;


/**
 * A persistent Map which contains custUnitNumber and masterId. A Jdbm3 and a
 * map name must be spring injected
 *
 * @author Babaprakash D
 *
 */
public class MasterCustomerCache extends CommonCache<Long, Long> {

    private static final String NAME = "masterCustomerCache";

    /**
     * Constructor of MasterCustomerCache.
     * @param pathToCache the path to the cache store
     */
    public MasterCustomerCache(String pathToCache) {
        validatePathAndCreateDb(pathToCache, NAME);
    }
}
