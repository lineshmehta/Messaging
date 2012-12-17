package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.account.AccountNameChangeEvent;
import com.telenor.cos.messaging.producers.xpath.AccountUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for {@link com.telenor.cos.messaging.event.account.AccountNameChangeEvent}
 * @author Babaprakash D
 *
 */
@Component
public class AccountNameChangeProducer extends AbstractProducer {

    @Autowired
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return accountUpdateXpathExtractor.getNewAccountName(message)!= null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long accountId = XPathLong.getValue(accountUpdateXpathExtractor.getAccountId(message));
        String newAccountName = accountUpdateXpathExtractor.getNewAccountName(message).getValue();
        AccountNameChangeEvent accountNameChangeEvent = new AccountNameChangeEvent(accountId,newAccountName);
        return Collections.<Event>singletonList(accountNameChangeEvent);
    }
}
