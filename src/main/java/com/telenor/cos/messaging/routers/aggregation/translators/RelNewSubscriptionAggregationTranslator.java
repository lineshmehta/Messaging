package com.telenor.cos.messaging.routers.aggregation.translators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.producers.xpath.RelSubscriptionXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class RelNewSubscriptionAggregationTranslator {

    private final static String DATA_CARD = "DK";
    private final static String DATA_CARD2 = "D2";
    private final static String TWIN_CARD = "TV";

    @Autowired
    private RelSubscriptionXpathExtractor relSubscriptionXpathExtractor;

    /**
     * Translates message to Event.
     * @param oldUpdatedSubscriptionEvent subscriptionEvent.
     * @param message message
     * @return Event.
     */
    public Event translate(NewSubscriptionEvent oldUpdatedSubscriptionEvent,Node message) {
        String relSubscriptionType = XPathString.getValue(relSubscriptionXpathExtractor.getRelSubscriptionType(message));
        if (DATA_CARD.equals(relSubscriptionType)) {
            oldUpdatedSubscriptionEvent.getData().setMsisdnDatakort(oldUpdatedSubscriptionEvent.getData().getMsisdn());
        } else if (TWIN_CARD.equals(relSubscriptionType)) {
            oldUpdatedSubscriptionEvent.getData().setMsisdnTvilling(oldUpdatedSubscriptionEvent.getData().getMsisdn());
        } else if (DATA_CARD2.equals(relSubscriptionType)) {
            oldUpdatedSubscriptionEvent.getData().setMsisdnDatakort2(oldUpdatedSubscriptionEvent.getData().getMsisdn());
        }
        oldUpdatedSubscriptionEvent.getData().setOwnerSubscriptionId(XPathLong.getValue(relSubscriptionXpathExtractor.getOwnerSubscriptionId(message)));
        return oldUpdatedSubscriptionEvent;
    }
}
