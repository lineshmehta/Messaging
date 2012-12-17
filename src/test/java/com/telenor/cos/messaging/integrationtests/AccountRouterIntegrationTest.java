package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountInvoiceFormatUpdateEvent;
import com.telenor.cos.messaging.event.account.AccountLogicalDeleteEvent;
import com.telenor.cos.messaging.event.account.AccountNameChangeEvent;
import com.telenor.cos.messaging.event.account.AccountNewEvent;
import com.telenor.cos.messaging.event.account.AccountOwnerUpdateEvent;
import com.telenor.cos.messaging.event.account.AccountPayerUpdateEvent;
import com.telenor.cos.messaging.event.account.AccountPaymentStatusUpdateEvent;
import com.telenor.cos.messaging.event.account.AccountStatusUpdateEvent;
import com.telenor.cos.messaging.event.account.AccountTypeUpdateEvent;
import com.telenor.cos.test.category.IntegrationTest;

/**
 * Integration Test case for Account Events.
 * @author Babaprakash D
 *
 */
@Category(IntegrationTest.class)
public class AccountRouterIntegrationTest extends CommonIntegrationTest {

    private static final Long ACCOUNT_ID = Long.valueOf(9364740);
    private static final String NAME = "storaccount";
    private static final String TYPE = "NO";
    private static final String STATUS = "PA";
    private static final String PAYMENT_STATUS = "234";
    private static final String INVOICE_FORMAT = "LL";
    private static final Long CUST_ID_PAYER = Long.valueOf(6935066);
    private static final Long CUST_ID_OWNER = Long.valueOf(6935066);
    private static final String FIRST_NAME = "\"Nils\"";
    private static final String MIDDLE_NAME = "Lasse Basse";
    private static final String LAST_NAME = "Olsen";
    private static final Long ORG_NUMBER = Long.valueOf(45533199);
    private static final Long POST_CODE_ID_MAIN = Long.valueOf(6480);
    private static final String POST_CODE_NAMEMAIN = "AUKRA";
    private static final Long MASTER_ID = Long.valueOf(100018010);

    private static final String CUSTOMER_NEW = "dataset/customer_new.xml";
    private static final String NEW_ACC_XML = "dataset/account_new.xml";
    private static final String LOGICAL_DELETE_XML = "dataset/account_logical_delete.xml";
    private static final String NAME_AND_STATUS_CHANGE_XML = "dataset/account_name_status_change.xml";
    private static final String PAYER_CHANGE_XML = "dataset/account_payerchange.xml";
    private static final String OWNER_CHANGE_XML = "dataset/account_ownerchange.xml";
    private static final String PAYMENT_AND_TYPE_CHANGE_XML = "dataset/account_payment_type_change.xml";
    private static final String INVOICE_FORMAT_CHANGE_XML = "dataset/account_invoiceFormat_change.xml";

    @Before
    public void before() throws Exception {
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        getJms().receive(ExternalEndpoints.ACCOUNT_NEW);
        getJms().receive(ExternalEndpoints.ACCOUNT_LOGICAL_DELETE);
        getJms().receive(ExternalEndpoints.ACCOUNT_NAME_CHANGE);
        getJms().receive(ExternalEndpoints.ACCOUNT_STATUS_CHANGE);
        getJms().receive(ExternalEndpoints.ACCOUNT_INVOICE_FORMAT_CHANGE);
        getJms().receive(ExternalEndpoints.ACCOUNT_PAYER_CHANGE);
        getJms().receive(ExternalEndpoints.ACCOUNT_PAYMENTSTATUS_CHANGE);
        getJms().receive(ExternalEndpoints.ACCOUNT_TYPE_CHANGE);
        getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }

    @Test(timeout = 10000)
    public void accountNewEventIntegrationTest() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(CUSTOMER_NEW);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(NEW_ACC_XML);
        AccountNewEvent consumedAccountEvent = (AccountNewEvent) getJms().receive(ExternalEndpoints.ACCOUNT_NEW, correlationId);
        assertNewAccountEvent(consumedAccountEvent,consumedAccountEvent.getAction());
    }

    @Test(timeout = 10000)
    public void logicalDeleteEventIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(LOGICAL_DELETE_XML);
        AccountLogicalDeleteEvent logicalDeleteEvent = (AccountLogicalDeleteEvent)getJms().receive(ExternalEndpoints.ACCOUNT_LOGICAL_DELETE, correlationId);
        assertAccountIdAndAction(logicalDeleteEvent,ACTION.LOGICAL_DELETE);
    }

    @Test(timeout = 10000)
    public void nameAndStatusChangeIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(NAME_AND_STATUS_CHANGE_XML);
        AccountStatusUpdateEvent statusChangeEvent = (AccountStatusUpdateEvent)getJms().receive(ExternalEndpoints.ACCOUNT_STATUS_CHANGE, correlationId);
        assertAccountIdAndAction(statusChangeEvent,ACTION.STATUS_UPDATE);
        assertEquals("Unexpected Status","ÅP",statusChangeEvent.getStatus());
        AccountNameChangeEvent nameChangeEvent = (AccountNameChangeEvent)getJms().receive(ExternalEndpoints.ACCOUNT_NAME_CHANGE, correlationId);
        assertAccountIdAndAction(nameChangeEvent,ACTION.NAME_CHANGE);
        assertEquals("Unexpected action", "TREE", nameChangeEvent.getName());
    }

    @Test(timeout = 10000)
    public void payerChangeIntegrationTest() throws Exception {
        sendXmlToRepServerQueueAndReturnCorrelationId(CUSTOMER_NEW);
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(PAYER_CHANGE_XML);
        AccountPayerUpdateEvent accountPayerUpdateEvent = (AccountPayerUpdateEvent)getJms().receive(ExternalEndpoints.ACCOUNT_PAYER_CHANGE, correlationId);
        CustomerName newPayerCustomerName = accountPayerUpdateEvent.getNewPayerCustomerName();
        CustomerAddress newPayerCustomerAddress = accountPayerUpdateEvent.getNewPayerCustomerAddress();

        assertAccountIdAndAction(accountPayerUpdateEvent,ACTION.PAYER_CHANGE);
        assertCustomerNameAndId(newPayerCustomerName.getCustomerId(),newPayerCustomerName.getFirstName(),newPayerCustomerName.getMiddleName(),newPayerCustomerName.getLastName());
        assertMasterIdAndOrgNumber(newPayerCustomerName.getMasterCustomerId(),newPayerCustomerName.getCustUnitNumber());
        assertEquals("Unexpected new Payer Customer Id", CUST_ID_PAYER, newPayerCustomerAddress.getCustomerId());
        assertInvoiceAdress(newPayerCustomerAddress.getPostcodeIdMain()+" "+newPayerCustomerAddress.getPostcodeNameMain());
    }

    @Test(timeout = 10000)
    public void ownerChangeIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(OWNER_CHANGE_XML);
        AccountOwnerUpdateEvent accountOwnerUpdateEvent = (AccountOwnerUpdateEvent)getJms().receive(ExternalEndpoints.ACCOUNT_OWNER_CHANGE, correlationId);
        assertAccountIdAndAction(accountOwnerUpdateEvent,ACTION.OWNER_CHANGE);
        assertEquals("Unexpected newCustomerId Owner+", Long.valueOf(6666666), accountOwnerUpdateEvent.getNewOwnerCustomerName().getCustomerId());
    }

    @Test(timeout = 10000)
    public void invoiceFormatIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(INVOICE_FORMAT_CHANGE_XML);
        AccountInvoiceFormatUpdateEvent accountInvoiceFormatEvent = (AccountInvoiceFormatUpdateEvent)getJms().receive(ExternalEndpoints.ACCOUNT_INVOICE_FORMAT_CHANGE, correlationId);
        assertAccountIdAndAction(accountInvoiceFormatEvent,ACTION.INVOICE_FORMAT_CHANGE);
        assertEquals("Unexpected Account Invoice Format", "AP", accountInvoiceFormatEvent.getInvoiceFormat());
    }

    @Test(timeout = 10000)
    public void paymentStatusAndAccTypeChangeIntegrationTest() throws Exception {
        String correlationId = sendXmlToRepServerQueueAndReturnCorrelationId(PAYMENT_AND_TYPE_CHANGE_XML);
        AccountPaymentStatusUpdateEvent accountPayementStatusUpdateEvent = (AccountPaymentStatusUpdateEvent)getJms().receive(ExternalEndpoints.ACCOUNT_PAYMENTSTATUS_CHANGE, correlationId);
        assertAccountIdAndAction(accountPayementStatusUpdateEvent,ACTION.PAYMENT_STATUS_CHANGE);
        assertEquals("Unexpected Account PaymentStatus", "PÅ", accountPayementStatusUpdateEvent.getPaymentStatus());
        AccountTypeUpdateEvent accountTypeChangeEvent = (AccountTypeUpdateEvent)getJms().receive(ExternalEndpoints.ACCOUNT_TYPE_CHANGE, correlationId);
        assertAccountIdAndAction(accountTypeChangeEvent,ACTION.TYPE_CHANGE);
        assertEquals("Unexpected Account Type", "NA", accountTypeChangeEvent.getAccountType());
    }

    private void assertNewAccountEvent(AccountNewEvent consumedAccountEvent, ACTION action) {
        assertAccountIdAndAction(consumedAccountEvent,action);
        assertEquals("Unexpected Account name",NAME,consumedAccountEvent.getAccount().getName());
        assertEquals("Unexpected Account Type",TYPE,consumedAccountEvent.getAccount().getType());
        assertEquals("Unexpected Account status",STATUS,consumedAccountEvent.getAccount().getStatus());
        assertEquals("Unexpected Account Payment status",PAYMENT_STATUS,consumedAccountEvent.getAccount().getPaymentStatus());
        assertEquals("Unexpected Account Invoice Format",INVOICE_FORMAT,consumedAccountEvent.getAccount().getInvoiceFormat());
        assertEquals("Unexpected Account Cust Id owner",CUST_ID_OWNER,consumedAccountEvent.getAccount().getCustIdResp());
        assertInvoiceAdress(consumedAccountEvent.getAccount().getInvoiceAddress());
        assertMasterIdAndOrgNumber(consumedAccountEvent.getAccount().getMasterIdPayer(),consumedAccountEvent.getAccount().getOrganizationNumberPayer());
        assertCustomerNameAndId(consumedAccountEvent.getAccount().getCustIdPayer(),
                consumedAccountEvent.getAccount().getCustFirstNamePayer(),
                consumedAccountEvent.getAccount().getCustMiddleNamePayer(),
                consumedAccountEvent.getAccount().getCustLastNamePayer());
    }
    private void assertCustomerNameAndId(Long customerId, String firstName, String middleName, String lastName) {
        assertEquals("Unexpected new Payer Customer Id", CUST_ID_PAYER, customerId);
        assertEquals("Unexpected New Payer Customer FirstName", FIRST_NAME, firstName);
        assertEquals("Unexpected New Payer Customer MiddleName", MIDDLE_NAME, middleName);
        assertEquals("Unexpected New Payer Customer LastName", LAST_NAME, lastName);
    }
    private void assertMasterIdAndOrgNumber(Long masterId, Long orgNumber) {
        assertEquals("Unexpected New Payer Customer MasterId", MASTER_ID, masterId);
        assertEquals("Unexpected New Payer Customer OrganisationNumber", ORG_NUMBER, orgNumber); 
    }
    private void assertAccountIdAndAction(Event event,ACTION action) {
        assertAction(event,action);
        assertEquals("Unexpected Account Id",ACCOUNT_ID,event.getDomainId());
    }
    private void assertInvoiceAdress(String actualInvoiceAddress) {
        assertEquals("Unexpected Invoice Address",POST_CODE_ID_MAIN+" "+POST_CODE_NAMEMAIN,actualInvoiceAddress);
    }
}
