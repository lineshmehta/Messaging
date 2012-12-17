package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountPayerUpdateEvent;
import com.telenor.cos.messaging.event.customer.CustomerAddressChangeEvent;
import com.telenor.cos.messaging.event.customer.CustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.event.customer.CustomerNameChangeEvent;
import com.telenor.cos.messaging.event.customer.CustomerNewEvent;
import com.telenor.cos.test.category.IntegrationTest;

@Category(IntegrationTest.class)
public class CustomerRouterIntegrationTest extends CommonIntegrationTest {

    private static final Long CUST_ID = Long.valueOf(6935066);
    private static final String FIRST_NAME = "\"Nils\"";
    private static final String MIDDLE_NAME = "Lasse Basse";
    private static final String LAST_NAME = "Olsen";
    private static final Long MASTER_ID = Long.valueOf(100018010);
    private static final Long ORG_NUMBER = Long.valueOf(45533199);
    private static final Long POST_CODE_ID_MAIN = Long.valueOf(6480);
    private static final String POST_CODE_NAME_MAIN = "AUKRA";

    private static final String NEW_CUSTOMER = "dataset/customer_new.xml";
    private static final String CUSTOMER_LOGICAL_DELETE = "dataset/customer_logicaldelete.xml";
    private static final String CUSTOMER_NAME_CHANGE = "dataset/customer_namechange.xml";
    private static final String CUSTOMER_ADDRESS_CHANGE = "dataset/customer_addresschange.xml";
    private static final String PAYER_CHANGE_XML = "dataset/account_payerchange.xml";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.CUSTOMER_NEW);
        getJms().receive(ExternalEndpoints.CUSTOMER_LOGICAL_DELETE);
        getJms().receive(ExternalEndpoints.CUSTOMER_NAME_CHANGE);
        getJms().receive(ExternalEndpoints.CUSTOMER_ADRESS_CHANGE);
        getJms().receive(ExternalEndpoints.ACCOUNT_PAYER_CHANGE);
        getJms().setReceiveTimeout(3000L);
    }

    @Test(timeout = 30000)
    public void customerNewEvent() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(NEW_CUSTOMER);
        CustomerNewEvent consumedCustomerNewEvent = (CustomerNewEvent) getJms().receive(ExternalEndpoints.CUSTOMER_NEW, correlationId);
        assertCustomerEvent(consumedCustomerNewEvent, ACTION.CREATED, CUST_ID);
        assertCustomerName(consumedCustomerNewEvent.getCustomerName());
        assertInvoiceAdress(POST_CODE_ID_MAIN+POST_CODE_NAME_MAIN, consumedCustomerNewEvent.getCustomerAddress().getPostcodeIdMain()+consumedCustomerNewEvent.getCustomerAddress().getPostcodeNameMain());

        CustomerName newPayerCustomer = getAccountPayerUpdateEvent().getNewPayerCustomerName();
        CustomerAddress newPayerCustomerAddress = getAccountPayerUpdateEvent().getNewPayerCustomerAddress();
        assertNotNull(newPayerCustomer);
        assertCustomerName(newPayerCustomer,FIRST_NAME,MIDDLE_NAME,LAST_NAME);
        assertMasterIdOrgNumber(newPayerCustomer,ORG_NUMBER,MASTER_ID);
        assertCustomerAddress(newPayerCustomerAddress, POST_CODE_ID_MAIN, POST_CODE_NAME_MAIN);
    }

    @Test(timeout = 10000)
    public void customerLogicalDeleteEvent() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(NEW_CUSTOMER);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(CUSTOMER_LOGICAL_DELETE);
        CustomerLogicalDeleteEvent consumedCustomerLogicalDeleteEvent = (CustomerLogicalDeleteEvent) getJms().receive(ExternalEndpoints.CUSTOMER_LOGICAL_DELETE, correlationId);
        assertCustomerEvent(consumedCustomerLogicalDeleteEvent, ACTION.LOGICAL_DELETE, CUST_ID);

        CustomerName newPayerCustomer = getAccountPayerUpdateEvent().getNewPayerCustomerName();
        CustomerAddress newPayerCustomerAddress = getAccountPayerUpdateEvent().getNewPayerCustomerAddress();
        assertNotNull(newPayerCustomer);
        assertCustomerName(newPayerCustomer,null,null,null);
        assertMasterIdOrgNumber(newPayerCustomer,null,null);
        assertCustomerAddress(newPayerCustomerAddress, null, null);
    }

    @Test(timeout = 10000)
    public void customerNameChangeEvent() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(NEW_CUSTOMER);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(CUSTOMER_NAME_CHANGE);
        CustomerNameChangeEvent consumedCustomerNameChangeEvent = (CustomerNameChangeEvent) getJms().receive(ExternalEndpoints.CUSTOMER_NAME_CHANGE, correlationId);
        assertCustomerEvent(consumedCustomerNameChangeEvent, ACTION.NAME_CHANGE, CUST_ID);
        assertCustomerName(consumedCustomerNameChangeEvent.getCustomerName());

        CustomerName newPayerCustomer = getAccountPayerUpdateEvent().getNewPayerCustomerName();
        CustomerAddress newPayerCustomerAddress = getAccountPayerUpdateEvent().getNewPayerCustomerAddress();
        assertNotNull(newPayerCustomer);
        assertCustomerName(newPayerCustomer,FIRST_NAME,MIDDLE_NAME,"Olsen");
        assertMasterIdOrgNumber(newPayerCustomer,ORG_NUMBER,MASTER_ID);
        assertCustomerAddress(newPayerCustomerAddress, POST_CODE_ID_MAIN, POST_CODE_NAME_MAIN);
    }

    @Test(timeout = 10000)
    public void customerAddressChangeEvent() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(NEW_CUSTOMER);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(CUSTOMER_ADDRESS_CHANGE);
        CustomerAddressChangeEvent consumedCustomerAdressChangeEvent = (CustomerAddressChangeEvent) getJms().receive(ExternalEndpoints.CUSTOMER_ADRESS_CHANGE, correlationId);
        assertCustomerEvent(consumedCustomerAdressChangeEvent, ACTION.ADRESS_CHANGE, CUST_ID);
        assertInvoiceAdress(Long.valueOf(6481)+"AUKRAChanged", consumedCustomerAdressChangeEvent.getCustomerAdress().getPostcodeIdMain()+consumedCustomerAdressChangeEvent.getCustomerAdress().getPostcodeNameMain());

        CustomerName newPayerCustomer = getAccountPayerUpdateEvent().getNewPayerCustomerName();
        CustomerAddress newPayerCustomerAddress = getAccountPayerUpdateEvent().getNewPayerCustomerAddress();
        assertNotNull(newPayerCustomer);
        assertCustomerName(newPayerCustomer,FIRST_NAME,MIDDLE_NAME,LAST_NAME);
        assertMasterIdOrgNumber(newPayerCustomer,ORG_NUMBER,MASTER_ID);
        assertCustomerAddress(newPayerCustomerAddress, Long.valueOf(6481), "AUKRAChanged");
    }

    private AccountPayerUpdateEvent getAccountPayerUpdateEvent() throws Exception {
        String correlationIdForAccountNewEvent = sendXmlToRepServerQueueAndReturnCorrelationId(PAYER_CHANGE_XML);
        AccountPayerUpdateEvent accountPayerUpdateEvent = (AccountPayerUpdateEvent) getJms().receive(ExternalEndpoints.ACCOUNT_PAYER_CHANGE, correlationIdForAccountNewEvent);
        return accountPayerUpdateEvent;
    }

    private void assertCustomerEvent(Event event, ACTION expectedAction, Long expectedCustomerId) {
        assertAction(event,expectedAction);
        assertEquals("Unexpected id", expectedCustomerId, event.getDomainId());
    }
    private void assertCustomerName(CustomerName customerName) {
        assertCustomerFirstName(FIRST_NAME,customerName.getFirstName());
        assertCustomerMiddleName(MIDDLE_NAME,customerName.getMiddleName());
        assertCustomerLastName(LAST_NAME,customerName.getLastName());
        assertOrgNumber(ORG_NUMBER,customerName.getCustUnitNumber());
        assertMasterId(MASTER_ID,customerName.getMasterCustomerId());
    }
    private void assertCustomerName(CustomerName newPayerCustomer,String firstName,String middleName,String lastName) {
        assertCustomerFirstName(firstName, newPayerCustomer.getFirstName());
        assertCustomerLastName(lastName, newPayerCustomer.getLastName());
        assertCustomerMiddleName(middleName, newPayerCustomer.getMiddleName());
    }
    private void assertCustomerAddress(CustomerAddress newPayerCustomerAddress,Long postCodeIdMain, String postCodeNameMain) {
        String actualInvoiceAddress = newPayerCustomerAddress.getPostcodeIdMain()+newPayerCustomerAddress.getPostcodeNameMain();
        String expectedInvoiceAddress = postCodeIdMain+postCodeNameMain;
        assertInvoiceAdress(expectedInvoiceAddress,actualInvoiceAddress);
    }
    private void assertMasterIdOrgNumber(CustomerName newPayerCustomer,Long orgNumber,Long masterId) {
        assertMasterId(masterId,newPayerCustomer.getMasterCustomerId());
        assertOrgNumber(orgNumber, newPayerCustomer.getCustUnitNumber());
    }
    private void assertCustomerFirstName(String expectedFirstName,String actualFirstName) {
        assertEquals("Unexpected FirstName",expectedFirstName,actualFirstName);
    }
    private void assertCustomerLastName(String expectedLastName,String actualLastName) {
        assertEquals("Unexpected LastName",expectedLastName,actualLastName);
    }
    private void assertCustomerMiddleName(String expectedMiddleName,String actualMiddleName) {
        assertEquals("Unexpected MiddleName",expectedMiddleName,expectedMiddleName);
    }
    private void assertMasterId(Long expectedMasterId,Long actualMasterId) {
        assertEquals("Unexpected MasterId",expectedMasterId,actualMasterId);
    }
    private void assertInvoiceAdress(String expectedInvoiceAddress,String actualInvoiceAddress) {
        assertEquals("Unexpected Invoice Address",expectedInvoiceAddress,actualInvoiceAddress);
    }
    private void assertOrgNumber(Long expectedOrgNumber,Long actualOrgNumber) {
        assertEquals("Unexpected OrgNumber",expectedOrgNumber,actualOrgNumber);
    }
}
