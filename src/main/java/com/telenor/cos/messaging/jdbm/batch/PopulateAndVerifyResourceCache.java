package com.telenor.cos.messaging.jdbm.batch;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.jdbm.CachableResource;

public class PopulateAndVerifyResourceCache extends PopulateAndVerifyCache<Long, CachableResource> {

    /**
     * setResourceCacheCommand.
     */
    public PopulateAndVerifyResourceCache() {
        setCommand(new ResourceCacheCommand());
    }

    private class ResourceCacheCommand extends Command<Long, CachableResource> {

        @Override
        Long parse(String resourceData) {
            CachableResource resource = JDBMUtils.createResourceObject(resourceData);
            if (resource == null){
                return null;
            }

            Long resourceId = resource.getResourceId();
            getMap().put(resourceId, resource);
            return resourceId;
        }

        @Override
        void verify(Long resourceId, long lineNumber) {
            CachableResource resourceInCache = getCache().get(resourceId);
            if (resourceInCache == null) {
                throw new CosMessagingException("Resource with ID ["+ resourceId +"] was not inserted in JDBM Cache, but exists in the data dump (csv file)", null);
            }
        }
    }
}
