package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.MasterCustomer;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNewEvent;
import com.telenor.cos.messaging.handlers.MasterCustomerNewHandler;
import com.telenor.cos.messaging.producers.xpath.MasterCustomerInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * Producer for new MasterCustomer.
 * @author Babaprakash D
 *
 */
@Component
public class MasterCustomerNewProducer extends AbstractProducer {

    @Autowired
    private MasterCustomerInsertXpathExtractor masterCustomerInsertXpathExtractor;

    @Autowired
    private MasterCustomerNewHandler masterCustomerNewHandler;

    @Override
    public List<Event> produceMessage(Node message) {
        MasterCustomer masterCustomer = createMasterCustomer(message);
        List<Event> eventsList = new ArrayList<Event>();
        MasterCustomerNewEvent masterCustomerNewEvent = new MasterCustomerNewEvent(masterCustomer);
        //Call Handler to Update Event in MasterCustomer and KurtIdCache.
        masterCustomerNewHandler.handle(masterCustomerNewEvent);
        eventsList.add(masterCustomerNewEvent);
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return masterCustomerInsertXpathExtractor.getMasterCustomerId(message)!= null;
    }

    /**
     * This method creates MasterCustomer object.
     * @param message received from repServer.
     * @return MasterCustomerName
     */
    private MasterCustomer createMasterCustomer(Node message) {
        MasterCustomer masterCustomer = new MasterCustomer();
        masterCustomer.setMasterId(masterCustomerInsertXpathExtractor.getMasterCustomerId(message).getValue());

        XPathString firstName = masterCustomerInsertXpathExtractor.getFirstName(message);
        if(firstName != null){
            masterCustomer.setFirstName(firstName.getValue());
        }

        XPathString lastName = masterCustomerInsertXpathExtractor.getLastName(message);
        if(lastName != null){
            masterCustomer.setLastName(lastName.getValue());
        }

        XPathString middleName = masterCustomerInsertXpathExtractor.getMiddleName(message);
        if(middleName != null){
            masterCustomer.setMiddleName(middleName.getValue());
        }

        XPathLong organizationNumber = masterCustomerInsertXpathExtractor.getOrganizationNumber(message);
        if(organizationNumber != null){
            masterCustomer.setOrganizationNumber(organizationNumber.getValue());
        }
        XPathLong kurtId = masterCustomerInsertXpathExtractor.getKurtId(message);
        if(kurtId != null) {
            masterCustomer.setKurtId(kurtId.getValue());
        }
        return masterCustomer;
    }
}
