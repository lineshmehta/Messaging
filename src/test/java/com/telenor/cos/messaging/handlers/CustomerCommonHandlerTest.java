package com.telenor.cos.messaging.handlers;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;

public class CustomerCommonHandlerTest {

    public static final Long CUSTOMER_ID = 1L;
    public static final String FIRST_NAME="Nils";
    public static final String MIDDLE_NAME="Ole";
    public static final String LAST_NAME="Larsen";
    public static final Long MASTER_CUSTOMER_ID = 33L;
    public static final String POSTCODE_ID_MAIN = "4444";
    public static final String POST_CODE_NAME_MAIN = "name";
    public static final String ADDRESS_LINE_MAIN = "line";
    public static final String ADDRESS_CO_NAME = "3333";
    public static final String ADRESS_STREET_NAME = "street";
    public static final String ADRESS_STREET_NUMBER = "78";

    @Mock
    private CustomerCache customerCache;

    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    public CustomerCache getCustomerCache() {
        return customerCache;
    }

    protected CustomerName createCustomerName(String firstName,String middleName,String lastName,Long masterId) {
        CustomerName customer = new CustomerName(CUSTOMER_ID);
        customer.setMasterCustomerId(masterId);
        customer.setFirstName(firstName);
        customer.setMiddleName(middleName);
        customer.setLastName(lastName);
        return customer;
    }

    protected CustomerAddress createCustomerAddress() {
        CustomerAddress customerAddress = new CustomerAddress(CUSTOMER_ID);
        customerAddress.setPostcodeIdMain("4444");
        customerAddress.setPostcodeNameMain("name");
        customerAddress.setAddressLineMain("line");
        customerAddress.setAddressCoName("3333");
        customerAddress.setAddressStreetName("street");
        customerAddress.setAddressStreetNumber("78");
        return customerAddress;
    }

    protected CachableCustomer createCachableCustomer() {
        CachableCustomer cachableCustomer = new CachableCustomer(CUSTOMER_ID);
        cachableCustomer.setMasterCustomerId(MASTER_CUSTOMER_ID);
        cachableCustomer.setFirstName(FIRST_NAME);
        cachableCustomer.setMiddleName(MIDDLE_NAME);
        cachableCustomer.setLastName(LAST_NAME);
        return cachableCustomer;
    }
}
