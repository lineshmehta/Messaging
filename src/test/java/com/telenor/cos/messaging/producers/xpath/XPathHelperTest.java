package com.telenor.cos.messaging.producers.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.CosMessagingInvalidDataException;
import com.telenor.cos.messaging.util.TestHelper;
import com.telenor.cos.test.category.ServiceTest;

/**
 * Test class {@link XPathHelper}. 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class XPathHelperTest {

    private static final Long EXPECTED_ID = Long.valueOf(666);

    private static final XPath XPATH = XPathFactory.newInstance().newXPath();
    
    private XPathHelper xPathExtractor = new XPathHelper();; 
    private static final String SUBSCRIPTION_EXPIRED = "dataset/expired_subscription.xml";
    private static final String SUBSCRIPTION_EXPIRED_EMPTY_ID = "dataset/expired_subscription_empty_id.xml";
    private static final String SUBSCRIPTION_EXPIRED_NO_MATCH = "dataset/expired_subscription_no_tag.xml";
    private static final String SUBSCRIPTION_NULL_VALUES = "dataset/subscription_null_values.xml";
    private static final String SUBSCRIPTION_ID_NULL = "dataset/subscription_id_null.xml";
    private static final String SUBSCRIPTION_WHITESPACE_ID = "dataset/subscription_with_whitespace_in_id.xml";
    private static final String SUBSCRIPTION_ID_ILLEGAL_FORMAT = "dataset/subscription_id_illegal_format.xml";
    private static final String SUBSCRIPTION = "dataset/subscription.xml";

    private String subscriptionId = "//update[@schema='SUBSCRIPTION']//oldValues//cell[@name='SUBSCR_ID']";
    private XPathExpression subscrIdXPathExpr;
    
    private String validToDate = "//update[@schema='SUBSCRIPTION']//values//cell[@name='SUBSCR_VALID_TO_DATE']";
    private XPathExpression subscrValidToDateXPathExpr;
    
    private TestHelper testHelper = new TestHelper();
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");
    
    
    @PostConstruct
    public void initializeXpathCompile() throws Exception {
        subscrIdXPathExpr = XPATH.compile(subscriptionId);
        subscrValidToDateXPathExpr = XPATH.compile(validToDate);
    }
    
    @Test
    public void testGetString() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_EXPIRED);
        assertEquals("Unexpected value", "666", xPathExtractor.getString(subscrIdXPathExpr, node).getValue());
    }
    
    @Test
    public void testGetStringNodeIsNull() throws Exception {
        try {
            xPathExtractor.getString(subscrIdXPathExpr, null);
            fail("Expected CosMessagingInvalidDataException");
        } catch (CosMessagingInvalidDataException e) {
            assertTrue("Unexpected error message", e.getMessage().contains("Error when extracting value from xml"));
        }
    }

    @Test
    public void testGetStringValueEmptyString() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_EXPIRED_EMPTY_ID);
        assertEquals("Unexpected subscription id", "", xPathExtractor.getString(subscrIdXPathExpr, node).getValue());
    }

    @Test
    public void testGetStringValueNoMatchingExpression() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_EXPIRED_NO_MATCH);
        assertEquals("Unexpected subscription id", null, xPathExtractor.getString(subscrIdXPathExpr, node));
    }

    @Test
    public void testGetStringLiteralNullValue() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_NULL_VALUES);
        assertEquals("Expected to return empty string","", xPathExtractor.getString(subscrIdXPathExpr, node).getValue());
    }

    @Test
    public void testGetLong() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_EXPIRED);
        assertEquals("Unexpected subscription id", EXPECTED_ID, xPathExtractor.getLong(subscrIdXPathExpr, node).getValue());
    }
    
    @Test
    public void testGetLongNullValue() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_ID_NULL);
        assertNull("Expected to be null", xPathExtractor.getLong(subscrIdXPathExpr, node));
    }

    @Test
    public void testGetLongLiteralNullValue() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_NULL_VALUES);
        assertNull("Expected to be null", xPathExtractor.getLong(subscrIdXPathExpr, node).getValue());
    }

    @Test
    public void testGetLongWithWhitespace() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_WHITESPACE_ID);
        assertEquals("Unexpected subscription id", EXPECTED_ID, xPathExtractor.getLong(subscrIdXPathExpr, node).getValue());
    }
    
    @Test
    public void testGetLongIllegalNumberFormat() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_ID_ILLEGAL_FORMAT);
        try {
            xPathExtractor.getLong(subscrIdXPathExpr, node);
            fail("Expected CosMessagingInvalidDataException");
        } catch (CosMessagingInvalidDataException e) {
            assertTrue("Unexpected error message", e.getMessage().contains("Number format error"));
        }
    }
    
    @Test
    public void testGetDate() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_EXPIRED);
        Date expectedDate = dateTimeFormat.parse("20121010 00:00:00:000");
        assertEquals("Unexpected date", expectedDate, xPathExtractor.getDate(subscrValidToDateXPathExpr, node).getValue());
    }
    
    @Test
    public void testGetDateNullValue() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION);
        assertNull("Expected date to be null", xPathExtractor.getDate(subscrValidToDateXPathExpr, node));
    }

    @Test
    public void testGetDateLiteralNullValue() throws Exception {
        Node node = testHelper.fileToDom(SUBSCRIPTION_NULL_VALUES);
        assertNull("Expected date to be null", xPathExtractor.getDate(subscrValidToDateXPathExpr, node).getValue());
    }
}
