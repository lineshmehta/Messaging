package com.telenor.cos.messaging.producers.xpath;

import static org.junit.Assert.assertEquals;

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
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class CustomerUpdatedXpathExtractorTest extends AbstractCustomerXpathUnitTest {

    @Autowired
    private CustomerUpdateXpathExtractor extractor;
    private Node message;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(XML_UPDATED_CUSTOMER);
    }
    @Test
    public void testGetMasterCustId() throws Exception {
        assertEquals(Long.valueOf(103269943), extractor.getNewMasterCustomerId(message).getValue());
    }
    @Test
    public void testGetCustomerName() throws Exception {
        assertEquals("Nils", extractor.getNewCustomerFirstName(message).getValue());
    }
    @Test
    public void testGetCustomerMiddleName() throws Exception {
        assertEquals("Lasse Basse", extractor.getNewCustomerMidddleName(message).getValue());
    }
    @Test
    public void testGetCustomerLastName() throws Exception {
        assertEquals("Olsen", extractor.getNewCustomerLastName(message).getValue());
    }
    @Test
    public void getCustomerUnitNumber() throws Exception {
        assertEquals("Unexpted cust number", Long.valueOf("666999"), extractor.getNewCustomerUnitNumber(message).getValue());
    }
    @Test
    public void getPostcodeIdMain() throws Exception {
        assertEquals("Unexpted PostcodeIdMain", Long.valueOf("2944"), extractor.getNewPostcodeIdMain(message).getValue());
    }
    @Test
    public void getPostcodeNameMain() throws Exception {
        assertEquals("Unexpted PostcodeNameMain", "Oslo", extractor.getNewPostcodeNameMain(message).getValue());
    }
    @Test
    public void getNewInfoIsDeleted() throws Exception {
        assertEquals("Unexpted NewInfoIsDeleted", "N", extractor.getNewInfoIsDeleted(message).getValue());
    }
    @Test
    public void getNewAddressLineName() throws Exception {
        assertEquals("line", extractor.getNewAdressLineMain(message).getValue());
    }
    @Test
    public void getNewAddressCOName() throws Exception {
        assertEquals("name", extractor.getNewAdressCOName(message).getValue());
    }
    @Test
    public void getNewAddressStreetName() throws Exception {
        assertEquals("street", extractor.getNewAdressStreetName(message).getValue());
    }
    @Test
    public void getNewAddressStreetNumber() throws Exception {
        assertEquals("78", extractor.getNewAdressStreetNumber(message).getValue());
    }
    @Test
    public void getOldCustomerId() throws Exception {
        assertEquals("Unexpted OldCustomerId", Long.valueOf("456"), extractor.getOldCustomerId(message).getValue());
    }
    @Test
    public void getOldMasterCustId() throws Exception {
        assertEquals("Unexpted Old MasterCustomerId", Long.valueOf(103269942), extractor.getOldMasterCustomerId(message).getValue());
    }
    @Test
    public void getOldCustomerName() throws Exception {
        assertEquals("Unexpted OldCustomerName", "Nils", extractor.getOldCustomerFirstName(message).getValue());
    }
    @Test
    public void getOldCustomerMiddleName() throws Exception {
        assertEquals("Unexpted OldCustomerMiddleName", "Lasse", extractor.getOldCustomerMidddleName(message).getValue());
    }
    @Test
    public void getOldCustomerLastName() throws Exception {
        assertEquals("Unexpted OldCustomerLastName", "Olsen", extractor.getOldCustomerLastName(message).getValue());
    }
    @Test
    public void getOldCustomerUnitNumner() throws Exception {
        assertEquals("Unexpted OldCustomerUnitNumner", Long.valueOf("666999"), extractor.getOldCustomerUnitNumber(message).getValue());
    }
    @Test
    public void getOldCustomerPostcodeIdMain() throws Exception {
        assertEquals("Unexpted OldCustomerPostcodeIdMain", Long.valueOf("2900"), extractor.getOldPostcodeIdMain(message).getValue());
    }
    @Test
    public void getOldCustomerPostcodeNameMain() throws Exception {
        assertEquals("Unexpted OldCustomerPostcodeNameMain", "FAGERNES", extractor.getOldPostcodeNameMain(message).getValue());
    }
    @Test
    public void getOldAddressLineMain() throws Exception {
        assertEquals("Unexpted Old Customer AddressLine Main", "line1", extractor.getOldAddressLineMain(message).getValue());
    }
    @Test
    public void getOldAddressCoName() throws Exception {
        assertEquals("Unexpted Old Customer Address COName", "name1", extractor.getOldAddressCOName(message).getValue());
    }
    @Test
    public void getOldAddressStreetName() throws Exception {
        assertEquals("Unexpted Old Customer Address Street Name", "street1", extractor.getOldAddressStreetName(message).getValue());
    }
    @Test
    public void getOldAddressStreetNumber() throws Exception {
        assertEquals("Unexpted Old Customer Address Street Number", "789", extractor.getOldAddressStreetNumber(message).getValue());
    }
}
