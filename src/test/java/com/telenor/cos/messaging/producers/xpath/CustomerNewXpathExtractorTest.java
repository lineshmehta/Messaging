package com.telenor.cos.messaging.producers.xpath;

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
public class CustomerNewXpathExtractorTest extends AbstractCustomerXpathUnitTest {

    @Autowired
    private CustomerInsertXpathExtractor extractor;
    private Node message;

    @Before
    public void setUp() throws Exception {
        message = stringToDom(XML_NEW_CUSTOMER);
    }

    @Test
    public void getCustomerId() throws Exception {
        assertEquals("Unexpected customerId", Long.valueOf("456"), extractor.getCustomerId(message).getValue());
    }

    @Test
    public void getCustomerName() throws Exception {
        assertEquals("Unexpected name", "Nils", extractor.getCustomerName(message).getValue());
    }

    @Test
    public void getCustomerMiddleName() throws Exception {
        assertEquals("Unexpected middle name", "Lasse Basse", extractor.getCustomerMidddleName(message).getValue());
    }

    @Test
    public void getCustomerLastName() throws Exception {
        assertEquals("Unexpected last name", "Olsen", extractor.getCustomerLastName(message).getValue());
    }

    @Test
    public void getMasterCustomerId() throws Exception {
        assertEquals("Unexpected MasterCustomer Id", Long.valueOf(103269943), extractor.getMasterCustomerId(message).getValue());
    }

    @Test
    public void getCustomerUnitNumber() throws Exception {
        assertEquals("Unexpted cust number", Long.valueOf("666999"), extractor.getCustomerUnitNumber(message).getValue());
    }

    @Test
    public void getPostcodeIdMain() throws Exception{
        assertEquals("Unexpted PostcodeIdMain", Long.valueOf("2900"), extractor.getPostcodeIdMain(message).getValue());
    }

    @Test
    public void getPostcodeNameMain() throws Exception{
        assertEquals("Unexpted PostcodeNameMain", "FAGERNES", extractor.getPostcodeNameMain(message).getValue());
    }

    @Test
    public void getAddressLineMain() throws Exception {
        assertEquals("Unexpted Address Line Main", "ELIASBAKKEN 7", extractor.getAddressLineMain(message).getValue());
    }
    @Test
    public void getAddressCOName() throws Exception {
        assertEquals("Unexpted AddressCo Name", "SALTEN KRAFTSAMBAND AS", extractor.getAddressCOName(message).getValue());
    }
    @Test
    public void getAddressStreetName() throws Exception {
        assertEquals("Unexpted AddressStreet Name", "ELIASBAKKEN", extractor.getAddressStreetName(message).getValue());  
    }
    @Test
    public void getAddressStreetNumber() throws Exception {
        assertEquals("Unexpted StreetNumber", "7", extractor.getAddressStreetNumber(message).getValue());
    }
}
