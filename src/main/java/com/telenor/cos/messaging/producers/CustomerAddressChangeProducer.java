package com.telenor.cos.messaging.producers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.customer.CustomerAddressChangeEvent;
import com.telenor.cos.messaging.handlers.CustomerAddressChangeHandler;
import com.telenor.cos.messaging.producers.xpath.CustomerUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.messaging.util.MessageFormattingUtil;

@Component
public class CustomerAddressChangeProducer extends AbstractProducer {

    @Autowired
    private CustomerUpdateXpathExtractor customerUpdateXpathExtractor;
    
    @Autowired
    private CustomerAddressChangeHandler customerAddressChangeHandler;

    @Override
    public List<Event> produceMessage(Node message) {
        CustomerAddressChangeEvent customerAddressChangeEvent = createAddressChangeEvent(message);
        List<Event> eventsList = new ArrayList<Event>();
        //Call Handler to save Event to Cache.
        customerAddressChangeHandler.handle(customerAddressChangeEvent);
        eventsList.add(customerAddressChangeEvent);
        return eventsList;
    }

    @Override
    public Boolean isApplicable(Node message) {
        XPathLong postcodeId = customerUpdateXpathExtractor.getNewPostcodeIdMain(message);
        XPathString name = customerUpdateXpathExtractor.getNewPostcodeNameMain(message);
        XPathString addressLineMain = customerUpdateXpathExtractor.getNewAdressLineMain(message);
        XPathString addressCoName = customerUpdateXpathExtractor.getNewAdressCOName(message);
        XPathString addressStreetName = customerUpdateXpathExtractor.getNewAdressStreetName(message);
        XPathString addressStreetNumber = customerUpdateXpathExtractor.getNewAdressStreetNumber(message);
        return postcodeId != null || name != null || addressLineMain != null
                || addressCoName != null || addressStreetName != null
                || addressStreetNumber != null;
    }

    private CustomerAddressChangeEvent createAddressChangeEvent(Node message) {
        Long domainId = XPathLong.getValue(customerUpdateXpathExtractor.getOldCustomerId(message));
        CustomerAddress customerAddress = new CustomerAddress(domainId);

        XPathString newAddressLineMain = customerUpdateXpathExtractor.getNewAdressLineMain(message);
        XPathString newAddressCOName  = customerUpdateXpathExtractor.getNewAdressCOName(message);
        XPathString newAddressStreetName = customerUpdateXpathExtractor.getNewAdressStreetName(message);
        XPathString newAddressStreetNumber = customerUpdateXpathExtractor.getNewAdressStreetNumber(message);
        XPathLong newPostcodeIdMain = customerUpdateXpathExtractor.getNewPostcodeIdMain(message);
        XPathString newPostcodeNameMain = customerUpdateXpathExtractor.getNewPostcodeNameMain(message);

        if(newAddressLineMain != null) {
            customerAddress.setAddressLineMain(newAddressLineMain.getValue());
        }else {
            customerAddress.setAddressLineMain(XPathString.getValue(customerUpdateXpathExtractor.getOldAddressLineMain(message)));
        }

        if(newAddressCOName != null) {
            customerAddress.setAddressCoName(newAddressCOName.getValue());
        }else {
            customerAddress.setAddressCoName(XPathString.getValue(customerUpdateXpathExtractor.getOldAddressCOName(message)));
        }
        if(newAddressStreetName != null) {
            customerAddress.setAddressStreetName(newAddressStreetName.getValue());
        }else {
            customerAddress.setAddressCoName(XPathString.getValue(customerUpdateXpathExtractor.getOldAddressCOName(message)));
        }
        if(newAddressStreetNumber != null) {
            customerAddress.setAddressStreetNumber(newAddressStreetNumber.getValue());
        }else {
            customerAddress.setAddressStreetNumber(XPathString.getValue(customerUpdateXpathExtractor.getOldAddressStreetNumber(message)));
        }
        if (newPostcodeIdMain != null) {
            String formattedPostCodeIdMain = MessageFormattingUtil.getFormattedPostCodeIdMain(newPostcodeIdMain.getValue());
            customerAddress.setPostcodeIdMain(formattedPostCodeIdMain);
        } else {
            Long oldPostCodeIdMain = XPathLong.getValue(customerUpdateXpathExtractor.getOldPostcodeIdMain(message));
            String oldFormattedPostCodeIdMain = MessageFormattingUtil.getFormattedPostCodeIdMain(oldPostCodeIdMain);
            customerAddress.setPostcodeIdMain(oldFormattedPostCodeIdMain);
        }
        if (newPostcodeNameMain != null) {
            customerAddress.setPostcodeNameMain(newPostcodeNameMain.getValue());
        }else {
            customerAddress.setPostcodeNameMain(XPathString.getValue(customerUpdateXpathExtractor.getOldPostcodeNameMain(message)));
        }
        return new CustomerAddressChangeEvent(domainId, customerAddress);
    }
}
