package com.telenor.cos.messaging.jdbm.batch;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.test.suite.ServiceTests;

/**
 * @author t808074
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3-batch.xml")
@DirtiesContext
@Category(ServiceTests.class)
public class PopulateAndVerifyTest {

    private static final String filePath = "/csv/";

    @Autowired
    private PopulateAndVerify populateAndVerify;

    @Test(expected = CosMessagingException.class)
    public void testPopulateAndVerifyWithInvalidFilePathAndChunkSize() {
        populateAndVerify.populateAndVerify("test","");
    }

    @Test(expected = CosMessagingException.class)
    public void testPopulateAndVerifyWithCsvFilePath() {
        populateAndVerify.populateAndVerify(filePath,null);
    }

    @Test(expected = CosMessagingException.class)
    public void testPopulateAndVerifyWithChunkSize() {
        populateAndVerify.populateAndVerify("test","5000");
    }
}
