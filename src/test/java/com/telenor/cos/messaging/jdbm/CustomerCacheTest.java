package com.telenor.cos.messaging.jdbm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbm3.xml")
@DirtiesContext
public class CustomerCacheTest {

    @Autowired
    private CustomerCache customerCache;

    @Before
    public void setUp() {
        customerCache.clear();
    }

    @Test
    public void testInsert() throws Exception {
        CachableCustomer customer = new CachableCustomer(1L);
        customer.setFirstName("Eirik");
        customer.setMiddleName("Falk Georg");
        customer.setLastName("Bergande");
        customerCache.insert(customer.getCustomerId(), customer);
        CachableCustomer o = customerCache.get(1L);
        assertNotNull(o);
        assertEquals("Eirik", o.getFirstName());
        assertEquals("Falk Georg", o.getMiddleName());
        assertEquals("Bergande", o.getLastName());
    }

    @Test
    public void testDetachedObject() throws Exception {
        CachableCustomer customer = new CachableCustomer(666L);
        CachableCustomer customer2 = new CachableCustomer(777L);
        customerCache.insert(666L, customer);
        CachableCustomer customer3 = customerCache.get(666L);
        customerCache.insert(777L, customer2);
        CachableCustomer customer4 = customerCache.get(666L);
        assertTrue(customer4 != customer3);
    }

    @Test
    public void testThreads() throws Exception {
        int j = 30000;
        Map<Long, CachableCustomer> tempMap = new HashMap<Long, CachableCustomer>();
        for (int i = 1; i <= j; i++) {
            CachableCustomer customer = new CachableCustomer(Long.valueOf(i));
            tempMap.put(Long.valueOf(i), customer);

            if (i % 5000 == 0) {
                customerCache.batchInsert(tempMap);
                tempMap.clear();
            }

        }
        assertEquals(j, customerCache.size());
    }

    @Test
    public void testUpdate() throws Exception {
        CachableCustomer customer1 = new CachableCustomer(1L);
        customer1.setFirstName("Eirik");
        customer1.setMiddleName("Falk Georg");
        customer1.setLastName("Bergande");
        CachableCustomer customer2 = new CachableCustomer(1L);
        customer2.setFirstName("Eirik");
        customer2.setMiddleName("Georg");
        customer2.setLastName("Bergande");
        customerCache.insert(customer1.getCustomerId(), customer1);
        customerCache.insert(customer2.getCustomerId(), customer2);
        CachableCustomer outputCustomer = customerCache.get(1L);
        assertEquals("Georg", outputCustomer.getMiddleName());
    }

    @Test
    public void testRemove() throws Exception {
        CachableCustomer customer1 = new CachableCustomer(1L);
        customer1.setFirstName("Eirik");
        customer1.setMiddleName("Falk Georg");
        customer1.setLastName("Bergande");
        customerCache.insert(customer1.getCustomerId(), customer1);
        CachableCustomer outputCustomer = customerCache.get(1L);
        assertNotNull(outputCustomer);
        customerCache.remove(1L);
        CachableCustomer removedCustomer = customerCache.get(1L);
        assertNull(removedCustomer);
    }

}
