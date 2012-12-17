package com.telenor.cos.messaging.routers.aggregation.translators;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Subscription;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.jdbm.CachableCustomer;
import com.telenor.cos.messaging.jdbm.CustomerCache;
import com.telenor.cos.messaging.jdbm.SubscriptionTypeCache;
import com.telenor.cos.messaging.producers.xpath.SubscriptionInsertXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

/**
 * Implementation of {@link SubscriptionNewTranslator}
 *
 * @author Eirik Bergande (Capgemini)
 */
@Component("subscriptionNewTranslator")
public class SubscriptionNewTranslator {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionNewTranslator.class);

    @Autowired
    private SubscriptionInsertXpathExtractor subscriptionInsertXpathExtractor;

    @Autowired
    private CustomerCache customerCache;

    @Autowired
    private SubscriptionTypeCache subscriptionTypeCache;

    /**
     * @param doc the node to translate
     * @return a list of events
     */
    public List<Event> translate(Node doc) {
        Long subscriptionId = XPathLong.getValue(subscriptionInsertXpathExtractor.getSubscrId(doc));
        Subscription subscriptionData = new Subscription();

        Long accoundId = XPathLong.getValue(subscriptionInsertXpathExtractor.getAccId(doc));
        subscriptionData.setAccountId(accoundId);

        Long userCustomerId = XPathLong.getValue(subscriptionInsertXpathExtractor.getCustId(doc));
        subscriptionData.setUserCustomerId(userCustomerId);

        CachableCustomer customer = customerCache.get(userCustomerId);
        if (customer != null) {
            subscriptionData.setFirstName(customer.getFirstName());
            subscriptionData.setMiddleName(customer.getMiddleName());
            subscriptionData.setLastName(customer.getLastName());
        } else {
            LOG.error("No customer corresponding to userCustomerId [" + userCustomerId +"] was found in the Customer Cache! Hence Subscription with id [" + subscriptionId + "]willnot have Customer details updated!");
        }
        Date validFromDate = XPathDate.getValue(subscriptionInsertXpathExtractor.getSubscrValidFromDate(doc));
        if (validFromDate != null) {
            subscriptionData.setValidFromDate(validFromDate);
        }
        Date validToDate = XPathDate.getValue(subscriptionInsertXpathExtractor.getSubscrValidToDate(doc));
        if (validToDate != null) {
            subscriptionData.setValidToDate(validToDate);
        }

        Long directoryNumberId = XPathLong.getValue(subscriptionInsertXpathExtractor.getDirectoryNumberId(doc));
        subscriptionData.setMsisdn(directoryNumberId);

        Long contractId = XPathLong.getValue(subscriptionInsertXpathExtractor.getContractId(doc));
        subscriptionData.setContractId(contractId);

        String subscriptionType = XPathString.getValue(subscriptionInsertXpathExtractor.getS212ProductId(doc));
        String baseProductId = subscriptionTypeCache.get(subscriptionType);
        subscriptionData.setSubscriptionType(baseProductId);

        String hasSecretNumber = XPathString.getValue(subscriptionInsertXpathExtractor.getSubscrHasSecretNumber(doc));
        subscriptionData.setIsSecretNumber("Y".equals(hasSecretNumber) ? true : false);

        String statusId = XPathString.getValue(subscriptionInsertXpathExtractor.getStatusId(doc));
        subscriptionData.setStatusId(statusId);

        List<Event> eventsList = new ArrayList<Event>();
        eventsList.add(new NewSubscriptionEvent(subscriptionId, subscriptionData));
        return eventsList;
    }
}
