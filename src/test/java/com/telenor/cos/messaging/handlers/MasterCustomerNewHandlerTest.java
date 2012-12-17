package com.telenor.cos.messaging.handlers;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.telenor.cos.messaging.event.MasterCustomer;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNewEvent;
import com.telenor.cos.messaging.jdbm.KurtIdCache;
import com.telenor.cos.messaging.jdbm.MasterCustomerCache;

/**
 * Test case for {@link MasterCustomerNewHandler}
 * @author Babaprakash D
 *
 */
public class MasterCustomerNewHandlerTest {

    private static final Long MASTER_ID = Long.valueOf(123);
    private static final Long ORG_NUMBER = Long.valueOf(4556789);
    private static final Long KURT_ID = Long.valueOf(4556789);

    @Mock
    private MasterCustomerCache masterCustomerCache;

    @Mock
    private KurtIdCache kurtIdCache;

    private MasterCustomerNewHandler masterCustomerNewHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        masterCustomerNewHandler = new MasterCustomerNewHandler();
        ReflectionTestUtils.setField(masterCustomerNewHandler, "masterCustomerCache", masterCustomerCache);
        ReflectionTestUtils.setField(masterCustomerNewHandler, "kurtIdCache", kurtIdCache);
    }

    @Test
    public void testHandle() throws Exception {
        MasterCustomer masterCustomer = createMasterCustomer();
        MasterCustomerNewEvent masterCustomerNewEvent = new MasterCustomerNewEvent(masterCustomer);
        masterCustomerNewHandler.handle(masterCustomerNewEvent);
        verify(masterCustomerCache).insert(ORG_NUMBER, MASTER_ID);
        verify(kurtIdCache).insert(KURT_ID, MASTER_ID);
    }

    private MasterCustomer createMasterCustomer() {
        MasterCustomer masterCustomer = new MasterCustomer();
        masterCustomer.setMasterId(MASTER_ID);
        masterCustomer.setOrganizationNumber(ORG_NUMBER);
        masterCustomer.setKurtId(KURT_ID);
        return masterCustomer;
    }
}
