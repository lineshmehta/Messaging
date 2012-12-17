package com.telenor.cos.messaging.jdbm.batch;

import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Babaprakash D
 *
 */
public abstract class AbstractPopulateAndVerify {

    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractPopulateAndVerify.class);

    /**
     * Number of lines to random take from CSV file for verification
     */
    private static final int NUMBER_OF_LINES = 40;

    /**
     * Gets csvFile absolute path.
     * @param filePath csv file path.
     * @param fileName name of the csv file.
     * @return File.
     */
    protected File getCsvAbsPath(String filePath, String fileName) {
        File csvFile = new File(filePath, fileName);
        File csvAbsPath = new File(csvFile.getAbsolutePath());
        return csvAbsPath;
    }

    /**
     * Generates a random number (int) between start and end (both inclusive)
     * @param start start.
     * @param end end.
     * @param exclude exclude.
     * @return Set<Integer>.
     */
    protected Set<Integer> getRandomLineNumbers(int start, int end, int... exclude) {
        Random generator = new Random();
        Set<Integer> randomLines = new HashSet<Integer>();
        for (int idx = 1; idx <= NUMBER_OF_LINES; idx++) {
            int random = start + generator.nextInt(end - start + 1 - exclude.length);
            for (int ex : exclude) {
                if (random < ex) {
                    break;
                }
                random++;
            }
            randomLines.add(random);
        }
        LOGGER.debug("Random list created: " + randomLines);
        return randomLines;
    }

    /**
     * This method reads csv file from the directory path provided as input
     * and creates HashMap of Customers with customerId as key and executes
     * batchInsert of JDBM3.
     * @param filepath csv file directory path.
     * @param chunkSize chunkSize
     * @return noO
     * 
     */
    public abstract long populateCache(String filepath, int chunkSize);

    /**
     * This method verifies CustomerData inserted in JDBM3.
     * @param csvFilePath path to csv file.
     * @param noofLinesParsed no of lines inserted into JDBM3.
     */
    public abstract void verifyJdbmDataPopulation(String csvFilePath, long noofLinesParsed);
}
