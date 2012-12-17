package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.subscription.SubscriptionExpiredEvent;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class SubscriptionExpiredProducer extends AbstractProducer {

    @Autowired
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        XPathDate validToDate = subscriptionUpdateXpathExtractor.getNewSubscrValidToDate(message);
        XPathDate validFromDate = subscriptionUpdateXpathExtractor.getOldSubscrValidFromDate(message);
        return (validToDate == null || validFromDate == null) ? false : isValidToDateAfterValidFromDate(validToDate.getValue(), validFromDate.getValue());
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long subscriptionId = XPathLong.getValue(subscriptionUpdateXpathExtractor.getOldSubscrId(message));
        SubscriptionExpiredEvent event =  new SubscriptionExpiredEvent(subscriptionId,subscriptionUpdateXpathExtractor.getNewSubscrValidToDate(message).getValue());
        String subTypeId = XPathString.getValue(subscriptionUpdateXpathExtractor.getOldSubscrTypeId(message));
        if(subTypeId!=null){
            event.setSubscrType(subTypeId);
            event.setMsisdn(XPathString.getValue(subscriptionUpdateXpathExtractor.getOldDirNumberId(message)));
        }
        return Collections.<Event> singletonList(event);
    }

    /**
     * Corner case check.
     * If valid_to_date is before valid_from_date the subscription is not applicable for termination.
     *
     * @param validToDate the valid to date
     * @param validFromDate the valid from date
     * @return true if valid_to_date is after valid_from_date
     */
    private boolean isValidToDateAfterValidFromDate(Date validToDate, Date validFromDate) {
        return validToDate == null || validToDate.after(validFromDate);
    }
}
