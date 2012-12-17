package com.telenor.cos.messaging.jdbm;

/**
 * Cache from subscriptionId to IMSI
 *
 * @author Espen Krogh-Moe
 */
public class ImsiCache extends CommonCache<Long, String> {

    private static final String CACHE_NAME = "subscriptionImsi";

    /**
     * Constructor.
     *
     * @param pathToCache the path to the cache store
     */
    public ImsiCache(String pathToCache) {
        validatePathAndCreateDb(pathToCache,CACHE_NAME);
    }
}
