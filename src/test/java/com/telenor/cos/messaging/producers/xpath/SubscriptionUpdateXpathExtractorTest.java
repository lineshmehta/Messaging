package com.telenor.cos.messaging.producers.xpath;

import com.telenor.cos.messaging.util.TestHelper;
import com.telenor.cos.test.category.ServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;


import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class SubscriptionUpdateXpathExtractorTest extends AbstractSubscriptionXpathUnitTest {

    @Autowired
    private SubscriptionUpdateXpathExtractor extractor;
    private Node message;

    private static final String SUBSCRIPTION_UPDATE_XML = "dataset/subscription_complete.xml";

    @Before
    public void setUp() throws Exception {
        message = new TestHelper().fileToDom(SUBSCRIPTION_UPDATE_XML);
    }

    @Test
    public void getNewSusbcrId() throws Exception {
        assertEquals(Long.valueOf("30870131"), extractor.getNewSubscrId(message).getValue());
    }

    @Test
    public void getNewContractId() throws Exception {
        assertEquals("11384657", extractor.getNewContractId(message).getValue());
    }

    @Test
    public void getNewProductId() throws Exception {
        assertEquals("55", extractor.getNewProductId(message).getValue());
    }

    @Test
    public void getNewS212ProductId() throws Exception {
        assertEquals("55S12", extractor.getNewS212ProductId(message).getValue());
    }

    @Test
    public void getNewDirectoryNumberId() throws Exception {
        assertEquals("99114533", extractor.getNewDirNumberId(message).getValue());
    }

    @Test
    public void getCustIdResp() throws Exception {
        assertEquals(Long.valueOf("8193478"), extractor.getNewCustIdUser(message).getValue());
    }

    @Test
    public void getNewSubscrValidFromDate() throws Exception {
        assertEquals(createDate("20120510 00:00:00:000"), extractor.getNewSubscrValidFromDate(message).getValue());
    }

    @Test
    public void getNewSubscrValidToDate() throws Exception {
        assertEquals(null, extractor.getNewSubscrValidToDate(message).getValue());
    }

    @Test
    public void getNewSubscrHasSecretNumber() throws Exception {
        assertEquals("Y", extractor.getNewSubscrHasSecretNumber(message).getValue());
    }

    @Test
    public void getNewAccId() throws Exception {
        assertEquals(Long.valueOf("8636339"), extractor.getNewAccId(message).getValue());
    }

    @Test
    public void getNewInfoIsDeleted() throws Exception {
        assertEquals("N", extractor.getNewInfoIsDeleted(message).getValue());
    }

    @Test
    public void getOldContractId() throws Exception {
        assertEquals("11384657", extractor.getOldContractId(message).getValue());
    }

    @Test
    public void getOldProductId() throws Exception {
        assertEquals("22", extractor.getOldProductId(message).getValue());
    }

    @Test
    public void getOldDirNumberId() throws Exception {
        assertEquals("991145323", extractor.getOldDirNumberId(message).getValue());
    }

    @Test
    public void getOldCustIdUser() throws Exception {
        assertEquals(Long.valueOf("8193478"), extractor.getOldCustIdUser(message).getValue());
    }

    @Test
    public void getOldSubscrValidFromDate() throws Exception {
        assertEquals(createDate("20120510 00:00:00:000"), extractor.getOldSubscrValidFromDate(message).getValue());
    }

    @Test
    public void getOldSubscrValidToDate() throws Exception {
        assertEquals(null, extractor.getOldSubscrValidToDate(message).getValue());
    }

    @Test
    public void getOldSubscrHasSecretNumber() throws Exception {
        assertEquals("Y", extractor.getOldSubscrHasSecretNumber(message).getValue());
    }

    @Test
    public void getOldAccId() throws Exception {
        assertEquals(Long.valueOf("8636339"), extractor.getOldAccId(message).getValue());
    }

    @Test
    public void getOldInfoIsDeleted() throws Exception {
        assertEquals("N", extractor.getOldInfoIsDeleted(message).getValue());
    }

    @Test
    public void getNewSubscrStatusId() throws Exception {
        assertEquals("", extractor.getNewSubscrStatusId(message).getValue());
    }

    @Test
    public void getOldSubscrId() throws Exception {
        assertEquals(Long.valueOf("30870131"), extractor.getOldSubscrId(message).getValue());
    }

    @Test
    public void getOldSubscrStatusId() throws Exception {
        assertEquals("LLO", extractor.getOldSubscrStatusId(message).getValue());
    }
    
    @Test
    public void getOldSubscrTypeId() throws Exception{
        assertEquals("DK", extractor.getOldSubscrTypeId(message).getValue());
    }

}
