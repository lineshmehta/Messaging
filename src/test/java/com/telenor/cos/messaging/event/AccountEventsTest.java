package com.telenor.cos.messaging.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.account.AccountInvoiceFormatUpdateEvent;
import com.telenor.cos.messaging.event.account.AccountLogicalDeleteEvent;
import com.telenor.cos.messaging.event.account.AccountNameChangeEvent;
import com.telenor.cos.messaging.event.account.AccountNewEvent;
import com.telenor.cos.messaging.event.account.AccountOwnerUpdateEvent;
import com.telenor.cos.messaging.event.account.AccountPayerUpdateEvent;
import com.telenor.cos.messaging.event.account.AccountPaymentStatusUpdateEvent;
import com.telenor.cos.messaging.event.account.AccountStatusUpdateEvent;
import com.telenor.cos.messaging.event.account.AccountTypeUpdateEvent;

/**
 * Test case for Account Events.
 * @author Babaprakash D
 *
 */
public class AccountEventsTest extends AbstractEventTest {

    private static final Long ACCOUNT_ID = Long.valueOf(1234);
    private static final String INVOICE_FORMAT = "";
    private static final String NAME = "176544\\David345";
    private static final Long CUST_ID_PAYER= Long.valueOf(6951817);
    private static final Long CUST_ID_OWNER= Long.valueOf(6951888);
    private static final String PAYMENT_STATUS = "";
    private static final String STATUS = "ÅP";
    private static final String ACCOUNT_TYPE = "NO";

    @Test
    public void testAccountNewEvent() throws Exception {
        Account account = new Account();
        account.setAccountId(ACCOUNT_ID);
        AccountNewEvent accountNewEvent = new AccountNewEvent(account);
        assertEquals("Unexpected Account Id",ACCOUNT_ID,accountNewEvent.getAccount().getAccountId());
        assertActionAndType(accountNewEvent,ACTION.CREATED,TYPE.ACCOUNT);
    }

    @Test
    public void testAccountLogicalDeleteEvent() throws Exception {
        AccountLogicalDeleteEvent accountLogicalDeleteEvent = new AccountLogicalDeleteEvent(ACCOUNT_ID);
        assertActionAndType(accountLogicalDeleteEvent,ACTION.LOGICAL_DELETE,TYPE.ACCOUNT);
    }

    @Test
    public void testAccountInvoiceFormatUpdateEvent() throws Exception {
        AccountInvoiceFormatUpdateEvent accountInvoiceFormatUpdateEvent = new AccountInvoiceFormatUpdateEvent(ACCOUNT_ID,INVOICE_FORMAT);
        assertEquals("Unexpected Account Invoice Format",INVOICE_FORMAT,accountInvoiceFormatUpdateEvent.getInvoiceFormat());
        assertActionAndType(accountInvoiceFormatUpdateEvent,ACTION.INVOICE_FORMAT_CHANGE,TYPE.ACCOUNT);
    }

    @Test
    public void testAccountNameChangeEvent() throws Exception {
        AccountNameChangeEvent accountNameChangeEvent = new AccountNameChangeEvent(ACCOUNT_ID,NAME);
        assertEquals("Unexpected Account Name",NAME,accountNameChangeEvent.getName());
        assertActionAndType(accountNameChangeEvent,ACTION.NAME_CHANGE,TYPE.ACCOUNT);
    }

    @Test
    public void testAccountPayerUpdateEvent() throws Exception {
        AccountPayerUpdateEvent accountPayerUpdateEvent = new AccountPayerUpdateEvent(ACCOUNT_ID,new CustomerName(CUST_ID_PAYER),new CustomerAddress(CUST_ID_PAYER));
        assertEquals("Unexpected Account Id",CUST_ID_PAYER,accountPayerUpdateEvent.getNewPayerCustomerName().getCustomerId());
        assertEquals("Unexpected Account Id",CUST_ID_PAYER,accountPayerUpdateEvent.getNewPayerCustomerAddress().getCustomerId());
        assertActionAndType(accountPayerUpdateEvent,ACTION.PAYER_CHANGE,TYPE.ACCOUNT);
    }

    @Test
    public void testAccountOwnerUpdateEvent() throws Exception {
        AccountOwnerUpdateEvent accountOwnerUpdateEvent = new AccountOwnerUpdateEvent(ACCOUNT_ID,new CustomerName(CUST_ID_OWNER),new CustomerAddress(CUST_ID_OWNER));
        assertEquals("Unexpected Account Id",CUST_ID_OWNER,accountOwnerUpdateEvent.getNewOwnerCustomerName().getCustomerId());
        assertEquals("Unexpected Account Id",CUST_ID_OWNER,accountOwnerUpdateEvent.getNewOwnerCustomerAddress().getCustomerId());
        assertActionAndType(accountOwnerUpdateEvent,ACTION.OWNER_CHANGE,TYPE.ACCOUNT);
    }

    @Test
    public void testAccountPaymentStatusUpdateEvent() throws Exception {
        AccountPaymentStatusUpdateEvent accountPaymentStatusUpdateEvent = new AccountPaymentStatusUpdateEvent(ACCOUNT_ID,PAYMENT_STATUS);
        assertEquals("Unexpected Account Payment status",PAYMENT_STATUS,accountPaymentStatusUpdateEvent.getPaymentStatus());
        assertActionAndType(accountPaymentStatusUpdateEvent,ACTION.PAYMENT_STATUS_CHANGE,TYPE.ACCOUNT);
    }

    @Test
    public void testAccountStatusUpdateEvent() throws Exception {
        AccountStatusUpdateEvent accountStatusUpdateEvent = new AccountStatusUpdateEvent(ACCOUNT_ID,STATUS);
        assertEquals("Unexpected Account status",STATUS,accountStatusUpdateEvent.getStatus());
        assertActionAndType(accountStatusUpdateEvent,ACTION.STATUS_UPDATE,TYPE.ACCOUNT);
    }

    @Test
    public void testAccountTypeUpdateEvent() throws Exception {
        AccountTypeUpdateEvent accountTypeUpdateEvent = new AccountTypeUpdateEvent(ACCOUNT_ID, ACCOUNT_TYPE);
        assertEquals("Unexpected Account Type",ACCOUNT_TYPE,accountTypeUpdateEvent.getAccountType());
        assertActionAndType(accountTypeUpdateEvent,ACTION.TYPE_CHANGE,TYPE.ACCOUNT);
    }
}
