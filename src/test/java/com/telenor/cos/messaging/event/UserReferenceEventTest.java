package com.telenor.cos.messaging.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.userref.InvoiceReferenceUpdateEvent;
import com.telenor.cos.messaging.event.userref.UserReferenceDescriptionUpdateEvent;
import com.telenor.cos.messaging.event.userref.UserReferenceLogicalDeleteEvent;
import com.telenor.cos.messaging.event.userref.UserReferenceNewEvent;
import com.telenor.cos.messaging.util.UserReferenceTestHelper;

/**
 * Test case for UserReference Update Events.
 * @author Babaprakash D
 *
 */
public class UserReferenceEventTest extends AbstractEventTest {

    private static final Long SUBSCRIPTION_ID = Long.valueOf(123);
    private static final String INVOICE_REF = "test";
    private static final String USER_REF_DESCR = "test";
    private static final String NUMBER_TYPE = "ES";

    @Test
    public void testUserReferenceNewEvent() {
        UserReferenceNewEvent userReferenceNewEvent = new UserReferenceNewEvent(SUBSCRIPTION_ID, UserReferenceTestHelper.createUserReference());
        assertActionAndType(userReferenceNewEvent,ACTION.CREATED,TYPE.USER_REFERENCE);
    }

    @Test
    public void testInvoiceReferenceUpdateEvent() {
        InvoiceReferenceUpdateEvent invoiceReferenceUpdateEvent = new InvoiceReferenceUpdateEvent(SUBSCRIPTION_ID,INVOICE_REF);
        assertActionAndType(invoiceReferenceUpdateEvent,ACTION.INVOICE_CHANGE,TYPE.USER_REFERENCE);
        assertEquals("Unexpected DomainId",SUBSCRIPTION_ID,invoiceReferenceUpdateEvent.getDomainId());
        assertEquals("Unexpected Invoice Ref",INVOICE_REF,invoiceReferenceUpdateEvent.getInvoiceRef());
    }

    @Test
    public void testUserReferenceDescrUpdateEvent() {
        UserReferenceDescriptionUpdateEvent userReferenceDescriptionUpdateEvent = new UserReferenceDescriptionUpdateEvent(SUBSCRIPTION_ID,USER_REF_DESCR,NUMBER_TYPE);
        assertActionAndType(userReferenceDescriptionUpdateEvent,ACTION.USERREF_DESC_CHG,TYPE.USER_REFERENCE);
        assertEquals("Unexpected DomainId",SUBSCRIPTION_ID,userReferenceDescriptionUpdateEvent.getDomainId());
        assertEquals("Unexpected UserRef Descr",USER_REF_DESCR,userReferenceDescriptionUpdateEvent.getUserRefDescr());
    }

    @Test
    public void testUserReferenceLogicalDeleteEventTest() {
        UserReferenceLogicalDeleteEvent userReferenceLogicalDeleteEvent = new UserReferenceLogicalDeleteEvent(SUBSCRIPTION_ID,NUMBER_TYPE);
        assertActionAndType(userReferenceLogicalDeleteEvent,ACTION.LOGICAL_DELETE,TYPE.USER_REFERENCE);
        assertEquals("Unexpected NumberType",NUMBER_TYPE,userReferenceLogicalDeleteEvent.getNumberType());
    }
}