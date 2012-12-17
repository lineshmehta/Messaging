package com.telenor.cos.messaging.jdbm;

import java.io.File;
import java.util.Map;

import net.kotek.jdbm.DB;
import net.kotek.jdbm.DBMaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telenor.cos.messaging.CosMessagingException;

public abstract class CommonCache<T, K> {

    private static final Logger LOG = LoggerFactory.getLogger(CommonCache.class);

    private DB db;

    private Map<T, K> map;

    /**
     * This method validates Path to cache and creates DB and Map.
     * @param pathToCache to cache.
     * @param cacheName name of Cache.
     */
    protected void validatePathAndCreateDb(String pathToCache,String cacheName) {
        File file = new File(pathToCache);
        if (!file.exists() && !file.mkdirs()) {
            throw new CosMessagingException("Could not create the path : " + pathToCache, null);
        }
        db = DBMaker.openFile(pathToCache+cacheName).make();
        map = db.getHashMap(cacheName);
        if (map == null) {
            map = db.createHashMap(cacheName);
        }
        if (LOG.isDebugEnabled()){
            LOG.debug(cacheName + " initialized: at : [" + pathToCache + "]");
        }
    }

    public Map<T, K> getMap() {
        return map;
    }

    public void setMap(Map<T, K> map) {
        this.map = map;
    }

    /**
     * Provides cache Statistics
     * @return Statistics.
     *
     */
    public String stats() {
        return db.calculateStatistics();
    }

    /**
     * Closes jdbm3 db
     */
    public void close() {
        db.close();
    }

    /**
     * Returns size of the map
     * @return size.
     */
    public int size() {
        return map.size();
    }

    /**
     * Clear jdbm3 db
     */
    public void clear() {
        map.clear();
    }

    /**
     * Removes a value with the specified key from the map
     * @param idToRemove the key
     */
    public void remove(T idToRemove) {
        map.remove(idToRemove);
        db.commit();
    }

    /**
     * Inserts a Customer into the map
     * @param id key.
     * @param valueToInsert valueToInsert..
     */
    public void insert(T id, K valueToInsert) {
        map.put(id, valueToInsert);
        db.commit();
    }

    /**
     * Method for Batch Insert.
     * @param mapToInsert Map to insert.
     */
    public void batchInsert(Map<T, K> mapToInsert) {
        map.putAll(mapToInsert);
        db.commit();
    }

    /**
     * @param id to get details from Cache.
     * @return Data from Cache.
     */
    public K get(T id) {
        return (map.get(id) != null)? map.get(id):null;
    }
}