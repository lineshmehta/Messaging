package com.telenor.cos.messaging.jdbm.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CachableResource;

public class JdbmUtilsTest {

    @Test
    public void testCreateCustomerObject() {
        String customerData = "6|100000002|LINE; CECILIE LUNDE||HOVRUD||900|FAGERNES|FEJORDV 20| |FEJORDV|20";
        CachableCustomer customer = JDBMUtils.createCustomerObject(customerData);
        assertEquals(Long.valueOf(6), customer.getCustomerId());
        assertEquals(Long.valueOf(100000002),customer.getMasterCustomerId());
        assertEquals("LINE; CECILIE LUNDE", customer.getFirstName());
        assertEquals("HOVRUD", customer.getLastName());
        assertEquals(null, customer.getCustUnitNumber());
        assertEquals("0900", customer.getPostcodeIdMain());
        assertEquals("FAGERNES", customer.getPostcodeNameMain());
        assertEquals("FEJORDV 20", customer.getAddressLineMain());
        assertEquals("FEJORDV",customer.getAddressStreetName());
        assertEquals("20", customer.getAddressStreetNumber());
    }

    @Test
    public void testCreateCustomerObjectWhenPostCodeIdMainIsNull() {
        String customerData = "6|100000002|LINE; CECILIE LUNDE||HOVRUD|||FAGERNES|FEJORDV 20| |FEJORDV|20";
        CachableCustomer customer = JDBMUtils.createCustomerObject(customerData);
        assertEquals("Unexpected  PostCodeId Main",null, customer.getPostcodeIdMain());
    }

    @Test
    public void testCreateCustomerObjectWithMasterIdNull() {
        String customerData = "999999062||BENGT||SAMUELSSON||5014|BERGEN|TORGALLMENNINGEN 8| |TORGALLMENNINGEN|8";
        CachableCustomer customer = JDBMUtils.createCustomerObject(customerData);
        assertEquals(Long.valueOf(999999062), customer.getCustomerId());
        assertEquals(null,customer.getMasterCustomerId());
        assertEquals("BENGT", customer.getFirstName());
    }

    @Test
    public void testCreateResourceObject() {
        String resourceData = "1|0||-|-|";
        CachableResource cachableResource = JDBMUtils.createResourceObject(resourceData);
        assertNotNull(cachableResource);
        assertEquals("Unexpected Resource Id",Long.valueOf(1),cachableResource.getResourceId());
        assertEquals("Unexpected Resource Type Id",Integer.valueOf(0),cachableResource.getResourceTypeId());
        assertEquals("Unexpected Resource TypeId Key",null,cachableResource.getResourceTypeIdKey());
        assertEquals("Unexpected Resource Content Inherit","-",cachableResource.getResourceHasContentInherit());
        assertEquals("Unexpected Resource Structure Inherit","-",cachableResource.getResourceHasStructureInherit());
    }
}
