package com.telenor.cos.messaging.jdbm.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.telenor.cos.messaging.CosMessagingException;

/**
 * Main Class for JDBM3 data Batch population.
 *
 */
public final class JDBMMain {

    private static final Logger LOG = LoggerFactory.getLogger(JDBMMain.class);

    /**
     * No arg Constructor
     */
    private JDBMMain() {

    }

    /**
     * Main method to be called by Populate script.
     * @param args Input Arguments.
     * args[0] path to csv files.
     * args[1] ChunkSize.
     */
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/jdbm3-batch.xml");
        PopulateAndVerify populateAndVerify = applicationContext.getBean(PopulateAndVerifyImpl.class);
        if (args.length < 2) {
            String errorMessage = "Input arguments are missing! Expected : Path to CSV File & ChunkSize";
            LOG.error(errorMessage);
            throw new CosMessagingException(errorMessage, null);
        } else {
            LOG.info("Input arguments : Path to CSV Files [" + args[0] + "] -- ChunkSize [" + args[1] +"]");
            populateAndVerify.populateAndVerify(args[0],args[1]);
        }
    }
}