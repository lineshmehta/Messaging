package com.telenor.cos.messaging.routers.aggregation.translators;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Subscription;
import com.telenor.cos.messaging.event.subscription.UpdatedSubscriptionEvent;
import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Translator used when aggregating subscriptions. 
 */
@Component("subscriptionUpdatedAggregationTranslator")
public class SubscriptionUpdatedAggregationTranslator {

    @Autowired
    private SubscriptionUpdateXpathExtractor subscriptionUpdateXpathExtractor;

    @Autowired
    private SubscriptionTypeCache subscriptionTypeCache;

    /**
     * @param doc the node to translate
     * @return a list of events
     */
    public List<Event> translate(Node doc) {
        Subscription newSubscriptionData = new Subscription();
        newSubscriptionData.setAccountId(XPathLong.getValue(subscriptionUpdateXpathExtractor.getNewAccId(doc)));

        if("Y".equals(XPathString.getValue(subscriptionUpdateXpathExtractor.getNewSubscrHasSecretNumber(doc)))){
            newSubscriptionData.setIsSecretNumber(true);
        }

        String newS212ProductId = XPathString.getValue(subscriptionUpdateXpathExtractor.getNewS212ProductId(doc));
        String baseProductId = subscriptionTypeCache.get(newS212ProductId);
        newSubscriptionData.setSubscriptionType(baseProductId);
        
        newSubscriptionData.setUserCustomerId(XPathLong.getValue(subscriptionUpdateXpathExtractor.getNewCustIdUser(doc)));
        newSubscriptionData.setValidToDate(XPathDate.getValue(subscriptionUpdateXpathExtractor.getNewSubscrValidToDate(doc)));
        newSubscriptionData.setValidFromDate(XPathDate.getValue(subscriptionUpdateXpathExtractor.getNewSubscrValidFromDate(doc)));
        
        String newContractId = XPathString.getValue(subscriptionUpdateXpathExtractor.getNewContractId(doc));
        if (newContractId != null) {
            newSubscriptionData.setContractId(Long.valueOf(newContractId));
        }
        String newDirectoryNumber = XPathString.getValue(subscriptionUpdateXpathExtractor.getNewDirNumberId(doc));
        if (newDirectoryNumber != null) {
            newSubscriptionData.setMsisdn(Long.valueOf(newDirectoryNumber));
        }
        
        Long subscriptionId = XPathLong.getValue(subscriptionUpdateXpathExtractor.getOldSubscrId(doc));
        List<Event> eventsList = new ArrayList<Event>();
        eventsList.add(new UpdatedSubscriptionEvent(subscriptionId, newSubscriptionData));
        return eventsList;
    }
}
