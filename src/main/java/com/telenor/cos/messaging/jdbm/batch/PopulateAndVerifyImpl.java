package com.telenor.cos.messaging.jdbm.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telenor.cos.messaging.CosMessagingException;

/**
 * Implementation class of PopulateAndVerify used to populate and verify data in JDBM3 cache.
 * @author Babaprakash D
 *
 */
public class PopulateAndVerifyImpl implements PopulateAndVerify {

    private static final Logger LOGGER = LoggerFactory.getLogger(PopulateAndVerifyImpl.class);

    private PopulateAndVerifyCustomerCache populateAndVerifyCustomerCache;

    private PopulateAndVerifyResourceCache populateAndVerifyResourceCache;

    private PopulateAndVerifySubscriptionTypeCache populateAndVerifySubscriptionTypeCache;

    private PopulateAndVerifyUserResourceCache populateAndVerifyUserResourceCache;

    private PopulateAndVerifyMasterCustomerCache populateAndVerifyMasterCustomerCache;

    private PopulateAndVerifyKurtIdCache populateAndVerifyKurtIdCache;

    private PopulateAndVerifyImsiCache populateAndVerifyImsiCache;

    private static final String[] FILES_REQUIRED_DURING_POPULATION = {
        JDBMUtils.CUSTOMER_DETAILS_CSV_FILENAME,
        JDBMUtils.MASTER_CUSTOMER_DETAILS_CSV_FILENAME,
        JDBMUtils.SUBSCRIPTION_TYPE_CSV_FILENAME,
        JDBMUtils.SUBSCRIPTION_IMSI_CSV_FILENAME,
        JDBMUtils.RESOURCE_DETAILS_CSV_FILENAME,
        JDBMUtils.USERRESOURCE_DETAILS_CSV_FILENAME,
        JDBMUtils.MASTER_CUSTOMER_KURT_DETAILS_CSV_FILENAME
    };

    @Override
    public void populateAndVerify(String pathToCsvFiles,String chunkSize) {
        LOGGER.info("Validating Input Arguments...PathToCsvFiles and ChunkSize");
        File csvFileDirecPath = new File(pathToCsvFiles);
        verifyDirectory(csvFileDirecPath);
        verifyFilesArePresent(csvFileDirecPath);
        int chunkSizeAfterValidation = validateChunkSize(chunkSize);

        LOGGER.info("Verified Input Arguments, Starting Data population...");
        populateAndVerifyCache(populateAndVerifyCustomerCache,csvFileDirecPath,JDBMUtils.CUSTOMER_DETAILS_CSV_FILENAME,chunkSizeAfterValidation);
        populateAndVerifyCache(populateAndVerifyMasterCustomerCache,csvFileDirecPath,JDBMUtils.MASTER_CUSTOMER_DETAILS_CSV_FILENAME,chunkSizeAfterValidation);
        populateAndVerifyCache(populateAndVerifySubscriptionTypeCache,csvFileDirecPath,JDBMUtils.SUBSCRIPTION_TYPE_CSV_FILENAME,chunkSizeAfterValidation);
        populateAndVerifyCache(populateAndVerifyImsiCache,csvFileDirecPath,JDBMUtils.SUBSCRIPTION_IMSI_CSV_FILENAME,chunkSizeAfterValidation);
        populateAndVerifyCache(populateAndVerifyResourceCache,csvFileDirecPath,JDBMUtils.RESOURCE_DETAILS_CSV_FILENAME,chunkSizeAfterValidation);
        populateAndVerifyCache(populateAndVerifyUserResourceCache,csvFileDirecPath,JDBMUtils.USERRESOURCE_DETAILS_CSV_FILENAME,chunkSizeAfterValidation);
        populateAndVerifyCache(populateAndVerifyKurtIdCache,csvFileDirecPath,JDBMUtils.MASTER_CUSTOMER_KURT_DETAILS_CSV_FILENAME,chunkSizeAfterValidation);
    }

    private void populateAndVerifyCache(AbstractPopulateAndVerify service,File csvFileDirecPath,String csvFileName, int chunkSize) {
        String csvFileAbsolutePath = new File(csvFileDirecPath,csvFileName).getAbsolutePath();
        LOGGER.info("Start processing file [" + csvFileName +"]");
        long noOfInsertedRecords = service.populateCache(csvFileAbsolutePath,chunkSize);
        service.verifyJdbmDataPopulation(csvFileAbsolutePath, noOfInsertedRecords);
    }

    private void verifyFilesArePresent(File directory) {
        List<String> missingFiles = new ArrayList<String>();
        for( String requiredFile : FILES_REQUIRED_DURING_POPULATION){
            if(!isAFile( directory, requiredFile)){
                missingFiles.add(requiredFile);
            }
        }
        if (missingFiles.size() > 0) {
            throw new CosMessagingException("There are files that cannot be found in the directory [" + missingFiles.toString() +"]",null);
        }
    }

    private void verifyDirectory(File csvFileDirecPath) {
        if (!csvFileDirecPath.isDirectory()) {
            throw new CosMessagingException("Filepath is not a directory [" + csvFileDirecPath.getAbsolutePath() + "]",null);
        }
    }

    private boolean isAFile(File dir, String file) {
        return (new File(dir, file)).isFile();
    }

    private int validateChunkSize(String chunkSize) {
        try {
            return Integer.parseInt(chunkSize);
        } catch (NumberFormatException e) {
            throw new CosMessagingException("ChunkSize is not valid",e);
        } catch (Exception e) {
            throw new CosMessagingException("ChunkSize is not valid",e);
        }
    }

    public void setPopulateAndVerifyCustomerCache(PopulateAndVerifyCustomerCache populateAndVerifyCustomerCache) {
        this.populateAndVerifyCustomerCache = populateAndVerifyCustomerCache;
    }

    public void setPopulateAndVerifyResourceCache(PopulateAndVerifyResourceCache populateAndVerifyResourceCache) {
        this.populateAndVerifyResourceCache = populateAndVerifyResourceCache;
    }

    public void setPopulateAndVerifySubscriptionTypeCache(PopulateAndVerifySubscriptionTypeCache populateAndVerifySubscriptionTypeCache) {
        this.populateAndVerifySubscriptionTypeCache = populateAndVerifySubscriptionTypeCache;
    }

    public void setPopulateAndVerifyImsiCache(PopulateAndVerifyImsiCache populateAndVerifyImsiCache) {
        this.populateAndVerifyImsiCache = populateAndVerifyImsiCache;
    }

    public void setPopulateAndVerifyUserResourceCache(PopulateAndVerifyUserResourceCache populateAndVerifyUserResourceCache) {
        this.populateAndVerifyUserResourceCache = populateAndVerifyUserResourceCache;
    }

    public void setPopulateAndVerifyMasterCustomerCache(PopulateAndVerifyMasterCustomerCache populateAndVerifyMasterCustomerCache) {
        this.populateAndVerifyMasterCustomerCache = populateAndVerifyMasterCustomerCache;
    }

    public void setPopulateAndVerifyKurtIdCache(PopulateAndVerifyKurtIdCache populateAndVerifyKurtIdCache) {
        this.populateAndVerifyKurtIdCache = populateAndVerifyKurtIdCache;
    }
}