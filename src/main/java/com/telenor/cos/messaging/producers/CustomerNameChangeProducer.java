package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.customer.CustomerNameChangeEvent;
import com.telenor.cos.messaging.handlers.CustomerNameChangeHandler;
import com.telenor.cos.messaging.producers.xpath.CustomerUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class CustomerNameChangeProducer extends AbstractProducer {

    @Autowired
    private CustomerUpdateXpathExtractor customerUpdateXpathExtractor;

    @Autowired
    private CustomerNameChangeHandler customerNameChangeHandler;

    @Override
    public List<Event> produceMessage(Node message) {
        CustomerNameChangeEvent customerNameChangeEvent = createCustomerNameChangeEvent(message);
        List<Event> eventsList = new ArrayList<Event>();
        //Call Handler to save Event to Cache.
        customerNameChangeHandler.handle(customerNameChangeEvent);
        eventsList.add(customerNameChangeEvent);
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        XPathString newFirstName = customerUpdateXpathExtractor.getNewCustomerFirstName(message);
        XPathString newMiddleName = customerUpdateXpathExtractor.getNewCustomerMidddleName(message);
        XPathString newLastName = customerUpdateXpathExtractor.getNewCustomerLastName(message);
        XPathLong newMasterCustId = customerUpdateXpathExtractor.getNewMasterCustomerId(message);
        XPathLong newCustUnitNumber = customerUpdateXpathExtractor.getNewCustomerUnitNumber(message);

        return newFirstName != null || newMiddleName != null ||
                newLastName != null || newMasterCustId != null || newCustUnitNumber != null;
    }

    private CustomerNameChangeEvent createCustomerNameChangeEvent(Node message) {
        Long customerId = XPathLong.getValue(customerUpdateXpathExtractor.getOldCustomerId(message));
        CustomerName customer = new CustomerName(customerId);

        XPathString newCustomerFirstName = customerUpdateXpathExtractor.getNewCustomerFirstName(message);
        if(newCustomerFirstName != null){
            customer.setFirstName(newCustomerFirstName.getValue());
        } else {
            customer.setFirstName(customerUpdateXpathExtractor.getOldCustomerFirstName(message).getValue());
        }

        XPathString newCustomerMidddleName = customerUpdateXpathExtractor.getNewCustomerMidddleName(message);
        if(newCustomerMidddleName != null){
            customer.setMiddleName(newCustomerMidddleName.getValue());
        } else {
            customer.setMiddleName(customerUpdateXpathExtractor.getOldCustomerMidddleName(message).getValue());
        }

        XPathString newCustomerLastName = customerUpdateXpathExtractor.getNewCustomerLastName(message);
        if(newCustomerLastName != null){
            customer.setLastName(newCustomerLastName.getValue());
        } else {
            customer.setLastName(customerUpdateXpathExtractor.getOldCustomerLastName(message).getValue());
        }

        XPathLong newMasterCustomerId = customerUpdateXpathExtractor.getNewMasterCustomerId(message);
        if(newMasterCustomerId != null){
            customer.setMasterCustomerId(newMasterCustomerId.getValue());
        } else {
            customer.setMasterCustomerId(customerUpdateXpathExtractor.getOldMasterCustomerId(message).getValue());
        }

        XPathLong newCustomerUnitNumber = customerUpdateXpathExtractor.getNewCustomerUnitNumber(message);
        if(newCustomerUnitNumber != null){
            customer.setCustUnitNumber(newCustomerUnitNumber.getValue());
        } else {
            customer.setCustUnitNumber(customerUpdateXpathExtractor.getOldCustomerUnitNumber(message).getValue());
        }

        return new CustomerNameChangeEvent(customerId, customer);
    }
}
