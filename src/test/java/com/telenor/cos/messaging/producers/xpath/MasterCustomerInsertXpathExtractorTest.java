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

/**
 * Test case for {@link MasterCustomerInsertXpathExtractor}
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
@Category(ServiceTest.class)
@DirtiesContext
public class MasterCustomerInsertXpathExtractorTest extends AbstractMasterCustomerXpathUnitTest {
    
    private Node message;
    
    @Autowired
    private MasterCustomerInsertXpathExtractor mcInsertXpathExtrac;
    
    @Before
    public void setUp() throws Exception {
        message = stringToDom(NEW_MASTER_CUSTOMER_XML);
    }
    
    @Test
    public void testMasterId() throws Exception {
        assertEquals("Unexpected Master customerId", Long.valueOf("369"), mcInsertXpathExtrac.getMasterCustomerId(message).getValue());
    }
    
    @Test
    public void testMCFirstName() throws Exception {
        assertEquals("Unexpected masterCustomer FirstName", "ELLEN ANNE", mcInsertXpathExtrac.getFirstName(message).getValue());
    }
    
    @Test
    public void testMCMiddleName() throws Exception {
        assertEquals("Unexpected masterCustomer MiddleName", "", mcInsertXpathExtrac.getMiddleName(message).getValue());
    }
    
    @Test
    public void testMCLastName() throws Exception {
        assertEquals("Unexpected masterCustomer lastName", "TOLLAN", mcInsertXpathExtrac.getLastName(message).getValue());
    }
    @Test
    public void testMCOrganisationNumber() throws Exception {
        assertEquals("Unexpected customer unit number", Long.valueOf(971261730), mcInsertXpathExtrac.getOrganizationNumber(message).getValue());
    }
    @Test
    public void testMCKurtId() throws Exception {
        assertEquals("Unexpected KurtId", Long.valueOf(3978455), mcInsertXpathExtrac.getKurtId(message).getValue());
    }
}
