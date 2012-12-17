package com.telenor.cos.messaging.handlers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.customer.CustomerAddressChangeEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;

public class CustomerAdressChangeHandlerTest extends CustomerCommonHandlerTest {

    private CustomerAddressChangeHandler customerAddressChangeHandler;

    @Before
    public void setUp() {
        super.setUp();
        customerAddressChangeHandler = new CustomerAddressChangeHandler();
        ReflectionTestUtils.setField(customerAddressChangeHandler, "customerCache", getCustomerCache());
    }

    @Test
    public void customerAdressChangeHandlerTest() throws Exception {
        CachableCustomer oldCustomer = createCachableCustomer();
        when(getCustomerCache().get(any(Long.class))).thenReturn(oldCustomer);
        CustomerAddress customerAdress = createCustomerAddress();
        CustomerAddressChangeEvent event = new CustomerAddressChangeEvent(CUSTOMER_ID, customerAdress);
        customerAddressChangeHandler.handle(event);
        verify(getCustomerCache()).insert(oldCustomer.getCustomerId(), oldCustomer);
        assertEquals(POSTCODE_ID_MAIN,oldCustomer.getPostcodeIdMain());
        assertEquals(POST_CODE_NAME_MAIN, oldCustomer.getPostcodeNameMain());
        assertEquals(ADDRESS_LINE_MAIN, oldCustomer.getAddressLineMain());
        assertEquals(ADDRESS_CO_NAME, oldCustomer.getAddressCOName());
        assertEquals(ADRESS_STREET_NAME, oldCustomer.getAddressStreetName());
        assertEquals(ADRESS_STREET_NUMBER, oldCustomer.getAddressStreetNumber());
    }
}
