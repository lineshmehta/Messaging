package com.telenor.cos.messaging.producers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNewEvent;
import com.telenor.cos.messaging.handlers.MasterCustomerNewHandler;
import com.telenor.cos.messaging.producers.xpath.MasterCustomerInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;

/**
 * Test case for {@link MasterCustomerNewProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class MasterCustomerNewProducerTest {

    private static final XPathLong MASTER_ID = XPathLong.valueOf(369);
    private static final XPathString FIRST_NAME = XPathString.valueOf("ELLEN ANNE");
    private static final XPathString MIDDLE_NAME = XPathString.valueOf("TORI");
    private static final XPathString LAST_NAME = XPathString.valueOf("TOLLAN");
    private static final XPathLong ORGANISATION_NUMBER = XPathLong.valueOf(971261730);
    private static final XPathLong KURT_ID = XPathLong.valueOf(971261730);

    private MasterCustomerNewProducer masterCustomerNewProducer;

    @Mock
    private MasterCustomerInsertXpathExtractor masterCustomerInsertXpathExtractor;

    @Mock
    private MasterCustomerNewHandler masterCustomerNewHandler;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        masterCustomerNewProducer = new MasterCustomerNewProducer();
        ReflectionTestUtils.setField(masterCustomerNewProducer, "masterCustomerInsertXpathExtractor", masterCustomerInsertXpathExtractor);
        ReflectionTestUtils.setField(masterCustomerNewProducer, "masterCustomerNewHandler",masterCustomerNewHandler);
    }
    @Test
    public void testIsApplicable() throws Exception {
        when(masterCustomerInsertXpathExtractor.getMasterCustomerId(any(Node.class))).thenReturn(XPathLong.valueOf(33L));
        Boolean isApplicable = masterCustomerNewProducer.isApplicable(any(Node.class));
        assertTrue(isApplicable);
    }

    @Test
    public void testIsNotApplicable() throws Exception {
        when(masterCustomerInsertXpathExtractor.getMasterCustomerId(any(Node.class))).thenReturn(null);
        Boolean isApplicable = masterCustomerNewProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test
    public void testNewMasterCustomerEvent() throws Exception {
        when(masterCustomerInsertXpathExtractor.getMasterCustomerId(any(Node.class))).thenReturn(MASTER_ID);
        when(masterCustomerInsertXpathExtractor.getFirstName(any(Node.class))).thenReturn(FIRST_NAME);
        when(masterCustomerInsertXpathExtractor.getMiddleName(any(Node.class))).thenReturn(MIDDLE_NAME);
        when(masterCustomerInsertXpathExtractor.getLastName(any(Node.class))).thenReturn(LAST_NAME);
        when(masterCustomerInsertXpathExtractor.getOrganizationNumber(any(Node.class))).thenReturn(ORGANISATION_NUMBER);
        when(masterCustomerInsertXpathExtractor.getKurtId(any(Node.class))).thenReturn(KURT_ID);

        List<Event> eventsList = masterCustomerNewProducer.produceMessage(any(Node.class));
        MasterCustomerNewEvent masterCustomerNewEvent=(MasterCustomerNewEvent)eventsList.get(0);

        assertEquals("Unexpected MasterId", MASTER_ID.getValue(), masterCustomerNewEvent.getDomainId());
        assertEquals("Unexpected MasterCustomer FirstName", FIRST_NAME.getValue(), masterCustomerNewEvent.getMasterCustomer().getFirstName());
        assertEquals("Unexpected MasterCustomer MiddleName", MIDDLE_NAME.getValue(), masterCustomerNewEvent.getMasterCustomer().getMiddleName());
        assertEquals("Unexpected MasterCustomer LastName", LAST_NAME.getValue(), masterCustomerNewEvent.getMasterCustomer().getLastName());
        assertEquals("Unexpected MasterCustomer OrganisationNumber", ORGANISATION_NUMBER.getValue(), masterCustomerNewEvent.getMasterCustomer().getOrganizationNumber());
        assertEquals("Unexpected MasterCustomer KurtId", KURT_ID.getValue(), masterCustomerNewEvent.getMasterCustomer().getKurtId());
    }
}
