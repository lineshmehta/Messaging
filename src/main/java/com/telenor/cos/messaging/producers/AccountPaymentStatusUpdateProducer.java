package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.account.AccountPaymentStatusUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.AccountUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for {@link com.telenor.cos.messaging.event.account.AccountPaymentStatusUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class AccountPaymentStatusUpdateProducer extends AbstractProducer {

    @Autowired
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return accountUpdateXpathExtractor.getNewAccountStatusId(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long accountId = XPathLong.getValue(accountUpdateXpathExtractor.getAccountId(message));
        String newAccountStatusId = XPathString.getValue(accountUpdateXpathExtractor.getNewAccountStatusId(message));
        AccountPaymentStatusUpdateEvent accountPaymentStatusUpdateEvent = new AccountPaymentStatusUpdateEvent(accountId, newAccountStatusId);
        return Collections.<Event>singletonList(accountPaymentStatusUpdateEvent);
    }
}
