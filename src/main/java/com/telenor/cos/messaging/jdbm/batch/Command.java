package com.telenor.cos.messaging.jdbm.batch;

import java.util.Map;

import com.google.common.collect.Maps;

public abstract class Command<T, K>  {

    private Map<T, K> map = Maps.newHashMap();

    /**
     * Method to parse line read from Csv.
     * @param lineFromCsvFile from csvFile.
     *
     * @return the key for the line
     */
    abstract T parse(String lineFromCsvFile);

    /**
     * Method to verify DomainObject.
     * @param domainObject domainObject.
     * @param lineNumber lineNumber.
     */
    abstract void verify(T key, long lineNumber);

    public Map<T, K> getMap() {
        return map;
    }
}