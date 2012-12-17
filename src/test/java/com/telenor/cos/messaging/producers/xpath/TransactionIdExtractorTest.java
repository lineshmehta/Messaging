package com.telenor.cos.messaging.producers.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import com.telenor.cos.test.category.ServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/producerTestContext.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class TransactionIdExtractorTest extends AbstractSubscriptionXpathUnitTest {

    @Autowired
    private TransactionIdExtractor extractor;

    private Node message;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(XML_FULL);
    }

    @Test
    public void testGetTransactionId() throws Exception {
        String transactionId = extractor.getTransactionId(message);
        assertNotNull(transactionId);
        assertEquals("102:00000000000029d5000014eb0001000014ea002900009c8d00930ee20000000000020001", transactionId);
    }
}
