package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.account.AccountInvoiceFormatUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.AccountUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

/**
 * Producer for {@link com.telenor.cos.messaging.event.account.AccountInvoiceFormatUpdateEvent}.
 * @author Babaprakash D
 *
 */
@Component
public class AccountInvoiceFormatUpdateProducer extends AbstractProducer {

    @Autowired
    private AccountUpdateXpathExtractor accountUpdateXPathExtractor;

    @Override
    public Boolean isApplicable(Node message) {
        return accountUpdateXPathExtractor.getNewAccountInvMedium(message) != null;
    }

    @Override
    public List<Event> produceMessage(Node message) {
        Long accountId = XPathLong.getValue(accountUpdateXPathExtractor.getAccountId(message));
        String invoiceFormat = accountUpdateXPathExtractor.getNewAccountInvMedium(message).getValue();
        AccountInvoiceFormatUpdateEvent accountInvoiceFormatUpdateEvent =  new AccountInvoiceFormatUpdateEvent(accountId,invoiceFormat);
        return Collections.<Event>singletonList(accountInvoiceFormatUpdateEvent);
    }
}
