package com.telenor.cos.messaging.handlers;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.customer.CustomerNewEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;

public class CustomerNewHandlerTest extends CustomerCommonHandlerTest {

    private CustomerNewHandler customerNewHandler;

    @Before
    public void setUp(){
        super.setUp();
        customerNewHandler = new CustomerNewHandler();
        ReflectionTestUtils.setField(customerNewHandler, "customerCache", getCustomerCache());
    }

    @Test
    public void customerNewHandlerTest() throws Exception{
        CustomerName customerName = createCustomerName(FIRST_NAME,MIDDLE_NAME,LAST_NAME,MASTER_CUSTOMER_ID);
        CustomerNewEvent event = new CustomerNewEvent(createCustomerAddress(),customerName);
        customerNewHandler.handle(event);
        CachableCustomer cachableCustomer = createCachableCustomer();
        verify(getCustomerCache()).insert(CUSTOMER_ID, cachableCustomer);
    }
}
