package com.telenor.cos.messaging.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telenor.cos.messaging.event.customer.CustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.jdbm.CustomerCache;

/**
 * Handler of {@link CustomerLogicalDeleteEvent}
 *
 */
@Component
public class CustomerLogicalDeleteHandler {

    @Autowired
    private CustomerCache customerCache;

    private static final Logger LOG = LoggerFactory.getLogger(CustomerLogicalDeleteHandler.class);

    /**
     * Removes a customer from the jdbm3
     * @param customerLogicalDeleteEvent customerLogicalDeleteEvent.
     */
    public void handle(CustomerLogicalDeleteEvent customerLogicalDeleteEvent) {
        customerCache.remove(customerLogicalDeleteEvent.getDomainId());
        if (LOG.isDebugEnabled()){
            LOG.debug("Received [CustomerLogicalDeleteEvent] with [customerId] as ["+ customerLogicalDeleteEvent.getDomainId() +"] and successfully removed it from the Customer Cache.");
        }
    }
}
