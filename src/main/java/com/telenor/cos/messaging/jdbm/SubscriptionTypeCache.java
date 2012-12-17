package com.telenor.cos.messaging.jdbm;


/**
 * A persistent Map which contains S212productId(legacy_product_ref) and
 * ProductId(product_id_member).
 *
 */
public class SubscriptionTypeCache extends CommonCache<String, String> {

    private static final String NAME = "SubscriptionTypeCache";

    /**
     * Constructor of SubscriptionTypeCache.
     * @param pathToCache the path to store det database.
     */
    public SubscriptionTypeCache(String pathToCache) {
        validatePathAndCreateDb(pathToCache, NAME);
    }
}
