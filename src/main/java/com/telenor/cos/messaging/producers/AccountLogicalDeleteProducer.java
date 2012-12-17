package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.account.AccountLogicalDeleteEvent;
import com.telenor.cos.messaging.producers.xpath.AccountUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;


/**
 * Producer for {@link com.telenor.cos.messaging.event.account.AccountLogicalDeleteEvent}.
 *
 */
@Component
public class AccountLogicalDeleteProducer extends AbstractProducer {

    @Autowired
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;



    @Override
    public Boolean isApplicable(Node message) {
        String infoIsDeleted = XPathString.getValue(accountUpdateXpathExtractor.getNewInfoIsDeleted(message));
        return "Y".equalsIgnoreCase(infoIsDeleted);
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long accountId = XPathLong.getValue(accountUpdateXpathExtractor.getAccountId(message));
        AccountLogicalDeleteEvent accountLogicalDeleteEvent = new AccountLogicalDeleteEvent(accountId);
        return Collections.<Event>singletonList(accountLogicalDeleteEvent);
    }
}