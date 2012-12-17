package com.telenor.cos.messaging.jdbm;


/**
 * A persistent Map which contains {@link CachableCustomer} objects. A Jdbm3 and
 * a map name must be spring injected
 *
 * @author Eirik Bergande (Capgemini)
 */
public class CustomerCache extends CommonCache<Long, CachableCustomer> {

    private static final String CACHE_NAME = "customerCache";

    /**
     * Constructor of CustomerCache.
     * @param pathToCache the path to the cache store
     */
    public CustomerCache(String pathToCache) {
        validatePathAndCreateDb(pathToCache,CACHE_NAME);
    }

    /**
     * Returns a Customer with the specified key
     * @param customerId The key, customerId
     * @return A Customers object
     */
    public CachableCustomer get(Long customerId) {
        return (getMap().get(customerId) != null) ? getMap().get(customerId).clone() : null;
    }
}
