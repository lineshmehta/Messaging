package com.telenor.cos.messaging.jdbm;



/**
 * A persistent Map which contains {@link CachableResource} objects.
 * @author Babaprakash D.
 * 
 */
public class ResourceCache extends CommonCache<Long, CachableResource> {

    private static final String NAME = "resourceCache";

    /**
     * Constructor of ResourceCache.
     * @param pathToCache the path to the cache store
     */
    public ResourceCache(String pathToCache) {
        validatePathAndCreateDb(pathToCache, NAME);
    }

    /**
     * Returns a Resource with the specified resourceId.
     * @param resourceId The key, ResourceId
     * @return A CachableResource object
     */
    @Override
    public CachableResource get(Long resourceId) {
        return (getMap().get(resourceId) != null) ? getMap().get(resourceId).clone():null;
    }
}