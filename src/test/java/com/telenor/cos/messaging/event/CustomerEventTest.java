package com.telenor.cos.messaging.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.Event.TYPE;
import com.telenor.cos.messaging.event.customer.CustomerAddressChangeEvent;
import com.telenor.cos.messaging.event.customer.CustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.event.customer.CustomerNameChangeEvent;
import com.telenor.cos.messaging.event.customer.CustomerNewEvent;

public class CustomerEventTest extends AbstractEventTest {

    private static final Long CUSTOMER_ID = Long.valueOf(1234);
    private CustomerAddress customerAddress = new CustomerAddress(CUSTOMER_ID);
    private CustomerName customerName = new CustomerName(CUSTOMER_ID);

    @Test
    public void testCreatingNewEvent() {
        CustomerNewEvent customerNewEvent = new CustomerNewEvent(customerAddress,customerName);
        assertActionAndType(customerNewEvent,ACTION.CREATED,TYPE.CUSTOMER);
        assertEquals("Unexpected Customer Id in Customer Address",customerAddress.getCustomerId(), customerNewEvent.getCustomerAddress().getCustomerId());
        assertEquals("Unexpected Customer Id in Customer name", customerName.getCustomerId(),customerNewEvent.getCustomerName().getCustomerId());
    }

    @Test
    public void testCustomerAddressChangeEvent() {
        CustomerAddressChangeEvent custAddrChangeEvent = new CustomerAddressChangeEvent(CUSTOMER_ID, customerAddress);
        assertActionAndType(custAddrChangeEvent,ACTION.ADRESS_CHANGE,TYPE.CUSTOMER);
        assertEquals("Unexpected Customer Id in Customer Address",customerAddress.getCustomerId(), custAddrChangeEvent.getCustomerAdress().getCustomerId());
    }

    @Test
    public void testCustomerNameChangeEvent() {
        CustomerNameChangeEvent custNameChangeEvent = new CustomerNameChangeEvent(CUSTOMER_ID, customerName);
        assertActionAndType(custNameChangeEvent,ACTION.NAME_CHANGE,TYPE.CUSTOMER);
        assertEquals("Unexpected Customer Id in Customer name", customerName.getCustomerId(),custNameChangeEvent.getCustomerName().getCustomerId());
    }

    @Test
    public void testCustomerLogicalDeleteEvent() {
        CustomerLogicalDeleteEvent customerLogicalDeleteEvent = new CustomerLogicalDeleteEvent(CUSTOMER_ID);
        assertActionAndType(customerLogicalDeleteEvent,ACTION.LOGICAL_DELETE,TYPE.CUSTOMER);
    }
}
