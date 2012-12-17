package com.telenor.cos.messaging.jdbm.batch;

/**
 * @author Babaprakash D
 *
 */
public interface PopulateAndVerify {
    
    /**
     * Method to Populate And Verify Data In Cache.
     * @param pathToCsvFiles path to csv files.
     * @param chunkSize ChunkSize.
     */
    void populateAndVerify(String pathToCsvFiles,String chunkSize);
}
