package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.MasterCustomer;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNameChangeEvent;
import com.telenor.cos.messaging.producers.xpath.MasterCustomerUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class MasterCustomerNameChangeProducer extends AbstractProducer {
    
    @Autowired
    private MasterCustomerUpdateXpathExtractor masterCustomerUpdateXpathExtractor;
    
    @Override
    public List<Event> produceMessage(Node message) {
        List<Event> masterCustNameChangeEventList = new ArrayList<Event>();
        Long masterId = XPathLong.getValue(masterCustomerUpdateXpathExtractor.getOldMasterCustomerId(message));
        MasterCustomer masterCustomerName = new MasterCustomer();

        String newFirstName = XPathString.getValue(masterCustomerUpdateXpathExtractor.getNewFirstName(message));
        if (newFirstName != null) {
            masterCustomerName.setFirstName(newFirstName);
        } else {
            masterCustomerName.setFirstName(XPathString.getValue(masterCustomerUpdateXpathExtractor.getOldFirstName(message)));
        }

        String newMiddleName = XPathString.getValue(masterCustomerUpdateXpathExtractor.getNewMiddleName(message));
        if (newMiddleName != null) {
            masterCustomerName.setMiddleName(newMiddleName);
        } else {
            masterCustomerName.setMiddleName(XPathString.getValue(masterCustomerUpdateXpathExtractor.getOldMiddleName(message)));
        }

        String newLastName = XPathString.getValue(masterCustomerUpdateXpathExtractor.getNewLastName(message));
        if (newLastName != null) {
            masterCustomerName.setLastName(newLastName);
        } else {
            masterCustomerName.setLastName(XPathString.getValue(masterCustomerUpdateXpathExtractor.getOldLastName(message)));
        }

        MasterCustomerNameChangeEvent masterCustNameChangeEvent= new MasterCustomerNameChangeEvent(masterId, masterCustomerName);
        masterCustNameChangeEventList.add(masterCustNameChangeEvent);
        return masterCustNameChangeEventList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return ( masterCustomerUpdateXpathExtractor.getNewFirstName(message) != null
                || masterCustomerUpdateXpathExtractor.getNewMiddleName(message) != null
                || masterCustomerUpdateXpathExtractor.getNewLastName(message) != null
                );
    }
}
