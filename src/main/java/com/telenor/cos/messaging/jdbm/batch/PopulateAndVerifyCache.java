package com.telenor.cos.messaging.jdbm.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.jdbm.CommonCache;

public class PopulateAndVerifyCache<T, K> extends AbstractPopulateAndVerify {

    private static final String CSV_READER_ERR_MSG = "Exception thrown while reading csv file";

    private CommonCache<T, K> cache;

    private Command<T, K> command;

    @Override
    public long populateCache(String csvFileAbsolutepath, int chunkSize) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = JDBMUtils.createBufferedReader(csvFileAbsolutepath);
            CSVChunkHandler<T, K> csvChunkHandler = new CSVChunkHandler<T, K>();
            do {
                Map<T, K> map = csvChunkHandler.parseCachableData(chunkSize, bufferedReader, command);
                cache.batchInsert(map);
                map.clear();
            } while (!csvChunkHandler.isFinished());
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
            throw new CosMessagingException(CSV_READER_ERR_MSG, e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    LOGGER.info(e.getMessage());
                    throw new CosMessagingException(CSV_READER_ERR_MSG, e);
                }
            }
        }
        long size = cache.size();
        if (size > 0) {
            LOGGER.debug(cache.getClass().getName() + " Size [" + size +"]");
            return size;
        } else {
            String errorMessage = "No data is inserted in to JDBM3";
            LOGGER.debug(errorMessage);
            throw new CosMessagingException(errorMessage, null);
        }
    }

    @Override
    public void verifyJdbmDataPopulation(String csvFilePath, long noofLinesParsed) {
        long counter = 1;
        boolean isInValidData = false;
        if (noofLinesParsed < 1) {
            LOGGER.info("No lines were parsed. Skipping verification.");
            return;
        }
        Set<Integer> randomLineNumbers = getRandomLineNumbers(0, (int) noofLinesParsed, 0);
        try {
            BufferedReader bufferedReader = JDBMUtils.createBufferedReader(csvFilePath);
            String line = null;
            do {
                line = bufferedReader.readLine();
                T domaiId = null;
                try {
                    if (line != null) {
                        domaiId = command.parse(line);
                    }
                } catch (Exception e) {
                    isInValidData = true;
                    LOGGER.error("Not able to parse and verify " + line, e);
                    continue;
                }
                if (randomLineNumbers.contains(Integer.valueOf((int) counter)) && !(isInValidData)) {
                    command.verify(domaiId, counter);
                }
                counter++;
            } while (counter <= noofLinesParsed);
            bufferedReader.close();
        } catch (IOException e) {
            throw new CosMessagingException(CSV_READER_ERR_MSG, e);
        }

        LOGGER.info(cache.getClass().getName() + " Loaded [" + cache.stats() +"]");
        cache.close();
    }

    public CommonCache<T, K> getCache() {
        return cache;
    }

    public void setCommand(Command<T, K> command) {
        this.command = command;
    }

    public void setCache(CommonCache<T, K> cache) {
        this.cache = cache;
    }
}
