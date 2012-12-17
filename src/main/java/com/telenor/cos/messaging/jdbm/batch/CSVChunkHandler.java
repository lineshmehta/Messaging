package com.telenor.cos.messaging.jdbm.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.CosMessagingInvalidDataException;


/**
 * This Class Helps to Handle Data in Chunks.
 */
public class CSVChunkHandler<T, K> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVChunkHandler.class);

    private boolean isFinished;

    /**
     * Parse Cachable Data in Chuncks.
     * @param chunkSize chunkSize from CommandLine.
     * @param bufferedReader BufferedReader.
     * @param command Command.
     * @return Map.
     * @throws IOException Exception.
     */
    public Map<T, K> parseCachableData(int chunkSize, BufferedReader bufferedReader, Command<T, K> command) throws IOException {
        int counter = 0;
        String data = null;
        do {
            try {
                data = bufferedReader.readLine();
                if (data != null) {
                    command.parse(data);
                } else {
                    isFinished = true;
                    break;
                }
            } catch (CosMessagingInvalidDataException e) {
                LOGGER.error(e.getMessage());
            } catch (NumberFormatException e) {
                LOGGER.error("Not able to insert following Data to Cache [" + data +"] This might be Invalid Data");
                continue;
            } catch (IOException e) {
                isFinished = true;
                throw new CosMessagingException("Failed to read from line [" + counter + "]", e);
            } finally {
                if (isFinished()) {
                    bufferedReader.close();
                }
            }
            counter++;
        } while (counter < chunkSize);
        return command.getMap();
    }

    public boolean isFinished() {
        return isFinished;
    }
}
