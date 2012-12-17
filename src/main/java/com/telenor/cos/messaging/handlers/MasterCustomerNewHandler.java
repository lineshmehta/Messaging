package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNewEvent;
import com.telenor.cos.messaging.jdbm.KurtIdCache;
import com.telenor.cos.messaging.jdbm.MasterCustomerCache;

/**
 * Handler of {@link MasterCustomerNewEvent}.
 * @author Babaprakash D
 *
 */
@Component
public class MasterCustomerNewHandler {

    @Autowired
    private MasterCustomerCache masterCustomerCache;

    @Autowired
    private KurtIdCache kurtIdCache;

    private static final Logger LOG = LoggerFactory.getLogger(MasterCustomerNewHandler.class);

    /**
     * Inserts a new masterCustomer(MasterCustomerId and OrgNumber) into the Jdbm3
     * @param masterCustomerNewEvent new masterCustomer.
     */
    public void handle(MasterCustomerNewEvent masterCustomerNewEvent) {
        Long masterCustomerId = masterCustomerNewEvent.getMasterCustomer().getMasterId();
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [MasterCustomerNewEvent] with [masterCustomerId] as ["+ masterCustomerId +"]");
        }
        Long custUnitNumber = masterCustomerNewEvent.getMasterCustomer().getOrganizationNumber();
        Long kurtId = masterCustomerNewEvent.getMasterCustomer().getKurtId();
        if(custUnitNumber != null) {
            masterCustomerCache.insert(custUnitNumber, masterCustomerId);
        } else {
            LOG.error("MasterCustomer with [masterCustomerId] as [" + masterCustomerId + "] has [custUnitNumber] as [null]");
        }
        if (kurtId != null) {
            kurtIdCache.insert(kurtId, masterCustomerId);
        } else {
            LOG.error("MasterCustomer with [masterCustomerId] as [" + masterCustomerId + "] has [kurtId] as [null]");
        }
    }
}