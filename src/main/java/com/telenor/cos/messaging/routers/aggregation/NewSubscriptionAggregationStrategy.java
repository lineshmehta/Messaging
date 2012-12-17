package com.telenor.cos.messaging.routers.aggregation;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Subscription;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.event.subscription.UpdatedSubscriptionEvent;
import com.telenor.cos.messaging.routers.aggregation.translators.RelNewSubscriptionAggregationTranslator;
import com.telenor.cos.messaging.routers.aggregation.translators.SubscriptionNewTranslator;
import com.telenor.cos.messaging.routers.aggregation.translators.SubscriptionUpdatedAggregationTranslator;
import com.telenor.cos.messaging.routers.aggregation.translators.NewUserReferenceAggregationTranslator;
import com.telenor.cos.messaging.producers.xpath.RelSubscriptionXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.SubscriptionInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.SubscriptionUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.UserReferenceInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;

/**
 * This class aggregates new subscriptions messages.
 * 
 * Normal/simple flow:
 * 1. Insert message received
 * 2. First update message received
 * 3. Second update message received 
 */
@Component
public class NewSubscriptionAggregationStrategy implements AggregationStrategy {

    @Autowired
    private SubscriptionUpdatedAggregationTranslator subscriptionUpdatedAggregationTranslator;

    @Autowired
    private SubscriptionNewTranslator subscriptionNewTranslator;

    @Autowired
    private SubscriptionInsertXpathExtractor subscriptionInsertXpathExtractorImpl;

    @Autowired
    private SubscriptionUpdateXpathExtractor subscriptionUpdatedXpathExtractor;

    @Autowired
    private RelSubscriptionXpathExtractor relSubscriptionXpathExtractor; 

    @Autowired
    private RelNewSubscriptionAggregationTranslator relNewSubscriptionAggregateTranslator;

    @Autowired
    private BeanMerger beanMerger;

    @Autowired
    private UserReferenceInsertXpathExtractor userRefeInsertXpathExtractor;

    @Autowired
    private NewUserReferenceAggregationTranslator newUserReferenceAggregateTranslator;


    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return translateFirstMessage(newExchange);
        } else {
            return mergeInsertAndUpdateMessages(oldExchange, newExchange);
        }
    }

    private Exchange translateFirstMessage(Exchange exchange) {

       // If first message is a SUBSCRIPTION insert message, act accordingly,
       // otherwise set a header to abort the aggregation - and route messages the "normal" non-aggregation way.

       if ((subscriptionInsertXpathExtractorImpl.getSubscrId(exchange.getIn().getBody(Node.class)) ) != null ) {
            return translateFirstInsertMessageIfApplicable(exchange);
        } else {
           exchange.getIn().setHeader("abortAggregation",true);
           return exchange;
       }

    }

    private Exchange translateFirstInsertMessageIfApplicable(Exchange newExchange) {
        XPathLong subscriptionId = subscriptionInsertXpathExtractorImpl.getSubscrId(newExchange.getIn().getBody(Node.class));
        if (subscriptionId != null) { 
            List<Event> eventsList = subscriptionNewTranslator.translate(newExchange.getIn().getBody(Node.class));
            NewSubscriptionEvent firstNewSubscriptionEvent =(NewSubscriptionEvent)eventsList.get(0);
            newExchange.getIn().setBody(firstNewSubscriptionEvent);
        }
        return newExchange;
    }

    private Exchange mergeInsertAndUpdateMessages(Exchange oldExchange,  Exchange newExchange) {
        Node message = newExchange.getIn().getBody(Node.class);
        XPathLong subscriptionId = subscriptionUpdatedXpathExtractor.getOldSubscrId(message);
        if (subscriptionId != null) {
            return updateSubscription(oldExchange, message);
        } 
        if (relSubscriptionXpathExtractor.getOwnerSubscriptionId(message) != null) {
            return updateRelSubscriptionTypeMsisdn(oldExchange, message);
        }
        if(userRefeInsertXpathExtractor.getSubscriptionId(message) != null) {
            return updateUserReferenceFields(oldExchange, message);
        }
        return oldExchange;
    }

    private Exchange updateSubscription(Exchange oldExchange, Node message) {
        NewSubscriptionEvent oldUpdatedSubscriptionEvent = oldExchange.getIn().getBody(NewSubscriptionEvent.class);
        List<Event> updateSubscriptionEvent = subscriptionUpdatedAggregationTranslator.translate(message);
        UpdatedSubscriptionEvent updatedSubscriptionEvent =(UpdatedSubscriptionEvent)updateSubscriptionEvent.get(0);

        Subscription mergedSubscription = beanMerger.mergeNotNullValues(oldUpdatedSubscriptionEvent.getData(), updatedSubscriptionEvent.getData());
        NewSubscriptionEvent updatedNewSubscriptionEvent = new NewSubscriptionEvent(oldUpdatedSubscriptionEvent.getDomainId(), mergedSubscription);
        oldExchange.getIn().setBody(updatedNewSubscriptionEvent);
        return oldExchange;
    }

    private Exchange updateRelSubscriptionTypeMsisdn(Exchange oldExchange, Node message) {
        NewSubscriptionEvent oldUpdatedSubscriptionEvent = oldExchange.getIn().getBody(NewSubscriptionEvent.class);
        NewSubscriptionEvent updatedSubscriptionEvent = (NewSubscriptionEvent)relNewSubscriptionAggregateTranslator.translate(oldUpdatedSubscriptionEvent, message);
        oldExchange.getIn().setBody(updatedSubscriptionEvent);
        return oldExchange;
    }
    private Exchange updateUserReferenceFields(Exchange oldExchange, Node message) {
        NewSubscriptionEvent oldUpdatedSubscriptionEvent = oldExchange.getIn().getBody(NewSubscriptionEvent.class);
        NewSubscriptionEvent updatedSubscriptionEvent =(NewSubscriptionEvent)newUserReferenceAggregateTranslator.translate(oldUpdatedSubscriptionEvent, message);
        oldExchange.getIn().setBody(updatedSubscriptionEvent);
        return  oldExchange;
    }
}
