package com.telenor.cos.messaging.jdbm.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.jdbm.UserResourceCache;

public class PopulateAndVerifyUserResourceCache extends AbstractPopulateAndVerify {

    private UserResourceCache userResourceCache;

    /**
     * This method reads csv file from the directory path provided as input 
     * and creates HashMap of csUserIdsList with resourceId as key and executes 
     * batchInsert of JDBM3.
     * 
     * @param csvFilepath csv file directory path.
     * @param chunkSize chunkSize
     * @return noOfInserted Records.
     * 
     */
    @Override
    public long populateCache(String csvFilepath, int chunkSize) {
        BufferedReader bufferedReader = null;
        Map<Long, List<String>> userResourceMap = new HashMap<Long, List<String>>();
        try {
            bufferedReader = JDBMUtils.createBufferedReader(csvFilepath);
            userResourceMap = parseUserResource(bufferedReader);
            userResourceCache.batchInsert(userResourceMap);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            throw new CosMessagingException("Exception thrown while reading csv file", e);
        } finally {
            if (bufferedReader != null) {
                try {
                    userResourceMap.clear();
                    bufferedReader.close();
                } catch (IOException e) {
                    LOGGER.info(e.getMessage());
                    throw new CosMessagingException("Exception thrown while closing the reader", e);
                }
            }
        }
        if (userResourceCache.size() > 0) {
            LOGGER.debug("UserResource Cache Size [" + userResourceCache.size() +"]");
            return userResourceCache.size();
        } else {
            LOGGER.debug("No data is inserted in to JDBM3");
            throw new CosMessagingException("No data is inserted in to JDBM3", null);
        }
    }

    /**
     * This method verifies data inserted in JDBM3.
     * 
     * @param csvFilePath path to csv file.
     * @param noofLinesParsed no of lines inserted into JDBM3.
     */
    @Override
    public void verifyJdbmDataPopulation(String csvFilePath, long noofLinesParsed) {
        long counter = 1;
        if (noofLinesParsed < 1) {
            LOGGER.info("No lines were parsed. Skipping verification.");
            return;
        }
        Set<Integer> randomLineNumbers = getRandomLineNumbers(0, (int) noofLinesParsed, 0);
        try {
            BufferedReader bufferedReader = JDBMUtils.createBufferedReader(csvFilePath);
            String data = null;
            String[] stringArray = null;
            do {
                data = bufferedReader.readLine();
                if (data != null) {
                   stringArray = StringUtils.splitPreserveAllTokens(data, "|");
                }
                if (randomLineNumbers.contains(Integer.valueOf((int) counter))) {
                    verifyUserResourceData(stringArray[0],stringArray[1],counter);
                }
                counter++;
            } while (counter <= noofLinesParsed);
            bufferedReader.close();
        } catch (IOException e) {
            throw new CosMessagingException("Exception thrown while reading csv file", e);
        }
        LOGGER.info("UserResourceCache Loaded [" + userResourceCache.stats() +"]");
        userResourceCache.close();
    }

    public void setUserResourceCache(UserResourceCache userResourceCache) {
        this.userResourceCache = userResourceCache;
    }

    private void verifyUserResourceData(String resourceId,String csUserId,long lineNumber) {
        if(resourceId != null) {
            List<String> csUserIdsListForResourceId = userResourceCache.get(Long.valueOf(resourceId));
            if (csUserIdsListForResourceId == null || csUserIdsListForResourceId.isEmpty()) {
                throw new CosMessagingException("UserResource with new resourceId ["+ resourceId +"] was not inserted in JDBM Cache, but exists in the data dump (csv file)", null);
            }else {
               if (!(csUserIdsListForResourceId.contains(csUserId))) {
                   throw new CosMessagingException("UserResource with csUserId ["+ csUserId +"] was not added to the resourceId [" + resourceId +"] but exists in the data dump (csv file)", null);
               }
            }
        } else {
            throw new CosMessagingException(("Row number [" + lineNumber + "] has no resource Id "), null);
        }
    }

    private Map<Long, List<String>> parseUserResource(BufferedReader bufferedReader) throws IOException {
        Map<Long, List<String>> userResourceMap = new HashMap<Long, List<String>>();
        CSVReader csvReader = new CSVReader(bufferedReader, '|');
        String[] strings = csvReader.readNext();
        do {
            String resourceId = strings[0];
            String csUserId = strings[1];

            Long resourceIdLong = Long.valueOf(resourceId);
            List<String> csUsersList = userResourceMap.get(resourceIdLong);
            if(csUsersList == null) {
                csUsersList = new ArrayList<String>();
                userResourceMap.put(resourceIdLong, csUsersList);
            }

           csUsersList.add(csUserId);
           strings = csvReader.readNext();
        } while (strings != null);
        return userResourceMap;
    }
}