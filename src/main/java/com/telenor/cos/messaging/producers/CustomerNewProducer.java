package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.customer.CustomerNewEvent;
import com.telenor.cos.messaging.handlers.CustomerNewHandler;
import com.telenor.cos.messaging.producers.xpath.CustomerInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.messaging.util.MessageFormattingUtil;

@Component
public class CustomerNewProducer extends AbstractProducer {

    @Autowired
    private CustomerInsertXpathExtractor customerInsertXpathExtractor;


    @Autowired
    private CustomerNewHandler customerNewHandler;

    @Override
    public List<Event> produceMessage(Node message) {
        CustomerNewEvent customerNewEvent = createCustomerNewEvent(message);
        List<Event> eventsList = new ArrayList<Event>();
        //Call Handler To Save Event to CustomerCache.
        customerNewHandler.handle(customerNewEvent);
        eventsList.add(customerNewEvent);
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        return customerInsertXpathExtractor.getCustomerId(message) != null;
    }

    private CustomerNewEvent createCustomerNewEvent(Node message) {
        Long customerId = XPathLong.getValue(customerInsertXpathExtractor.getCustomerId(message));

        CustomerAddress customerAddress = new CustomerAddress(customerId);
        CustomerName customerName = new CustomerName(customerId);

        customerName.setMasterCustomerId(XPathLong.getValue(customerInsertXpathExtractor.getMasterCustomerId(message)));

        customerName.setFirstName(XPathString.getValue(customerInsertXpathExtractor.getCustomerName(message)));
        customerName.setMiddleName(XPathString.getValue(customerInsertXpathExtractor.getCustomerMidddleName(message)));
        customerName.setLastName(XPathString.getValue(customerInsertXpathExtractor.getCustomerLastName(message)));

        customerName.setCustUnitNumber(XPathLong.getValue(customerInsertXpathExtractor.getCustomerUnitNumber(message)));

        Long postCodeIdMain = XPathLong.getValue(customerInsertXpathExtractor.getPostcodeIdMain(message));
        customerAddress.setPostcodeIdMain(MessageFormattingUtil.getFormattedPostCodeIdMain(postCodeIdMain));

        customerAddress.setPostcodeNameMain(XPathString.getValue(customerInsertXpathExtractor.getPostcodeNameMain(message)));
        customerAddress.setAddressCoName(XPathString.getValue(customerInsertXpathExtractor.getAddressCOName(message)));
        customerAddress.setAddressLineMain(XPathString.getValue(customerInsertXpathExtractor.getAddressLineMain(message)));
        customerAddress.setAddressStreetName(XPathString.getValue(customerInsertXpathExtractor.getAddressStreetName(message)));
        customerAddress.setAddressStreetNumber(XPathString.getValue(customerInsertXpathExtractor.getAddressStreetNumber(message)));

        return new CustomerNewEvent(customerAddress,customerName);
    }
}
