package com.telenor.cos.messaging.producers.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class SubscriptionNewXpathExtractorTest extends AbstractSubscriptionXpathUnitTest {

    @Autowired
    private SubscriptionInsertXpathExtractor extractor;
    private Node message;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(XML_NEW);
    }

    @Test
    public void shouldGetsubscriptionId() throws Exception {
        assertEquals("Unexpected subscriptionId", Long.valueOf("32143317"), extractor.getSubscrId(message).getValue());
    }

    @Test
    public void shouldGetAccountId() throws Exception {
        assertEquals("Unexpected accountId", Long.valueOf("999999001"), extractor.getAccId(message).getValue());
    }

    @Test
    public void getCustomerId() throws Exception {
        assertEquals("Unexpected customerId", Long.valueOf("6935066"), extractor.getCustId(message).getValue());
    }

    @Test
    public void getValidFromDate() throws Exception {
        Date expectedDate = createDate("20120510 00:00:00:000");
        assertEquals("Unexpected date", expectedDate, extractor.getSubscrValidFromDate(message).getValue());
    }

    @Test
    public void getValidToDate() throws Exception {
        assertNull("Expected to be null", extractor.getSubscrValidToDate(message).getValue());
    }

    @Test
    public void shouldGetIsSecretNumber() throws Exception {
        String isSecretNumber = extractor.getSubscrHasSecretNumber(message).getValue();
        assertEquals("N", isSecretNumber);
    }

    @Test
    public void shouldGetDirectoryIdNumber() throws Exception {
        assertEquals("Unexpected directory number", Long.valueOf("580000506010"), extractor.getDirectoryNumberId(message).getValue());
    }

    @Test
    public void shouldGetcontractId() throws Exception {
        assertEquals("Unexpected contract id", Long.valueOf("12716621"), extractor.getContractId(message).getValue());
    }

    @Test
    public void shouldGetS212ProductId() throws Exception {
        String s212ProductId = extractor.getS212ProductId(message).getValue();
        assertEquals("04713", s212ProductId);
    }

    @Test
    public void shouldSubscriptionStatusId() throws Exception {
        assertTrue("Expected status id to be blank", StringUtils.isBlank(XPathString.getValue(extractor.getStatusId(message))));
    }
}
