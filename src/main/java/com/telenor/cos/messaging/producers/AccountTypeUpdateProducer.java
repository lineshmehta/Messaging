package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.account.AccountTypeUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.AccountUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for {@link com.telenor.cos.messaging.event.account.AccountTypeUpdateEvent}
 * @author Babaprakash D
 *
 */
@Component
public class AccountTypeUpdateProducer extends AbstractProducer {

    @Autowired
    private AccountUpdateXpathExtractor accountUpdateXPathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return accountUpdateXPathExtractor.getNewAccountTypeId(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long accountId = XPathLong.getValue(accountUpdateXPathExtractor.getAccountId(message));
        String newAccountTypeId = XPathString.getValue(accountUpdateXPathExtractor.getNewAccountTypeId(message));
        AccountTypeUpdateEvent accountTypeUpdateEvent = new AccountTypeUpdateEvent(accountId, newAccountTypeId);
        return Collections.<Event>singletonList(accountTypeUpdateEvent);
    }
}
