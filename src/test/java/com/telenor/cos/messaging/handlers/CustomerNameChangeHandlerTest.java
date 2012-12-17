package com.telenor.cos.messaging.handlers;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.customer.CustomerNameChangeEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;

public class CustomerNameChangeHandlerTest extends CustomerCommonHandlerTest {

    private CustomerNameChangeHandler customerNameChangeHandler;

    private static final Long MASTER_CUST_ID_NEW = Long.valueOf(126);

    @Before
    public void setUp() {
        super.setUp();
        customerNameChangeHandler = new CustomerNameChangeHandler();
        ReflectionTestUtils.setField(customerNameChangeHandler, "customerCache", getCustomerCache());
    }

    @Test
    public void customerNameChangeHandlerTest() throws Exception{
        CachableCustomer oldCustomer = createCachableCustomer();
        when(getCustomerCache().get(anyLong())).thenReturn(oldCustomer);

        CustomerName customer = createCustomerName("Ole", "Dole", "Doffen", MASTER_CUST_ID_NEW);
        CustomerNameChangeEvent event = new CustomerNameChangeEvent(customer.getCustomerId(), customer);
        customerNameChangeHandler.handle(event);

        verify(getCustomerCache()).insert(customer.getCustomerId(), oldCustomer);
    }
}
