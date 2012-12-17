package com.telenor.cos.messaging.producers;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.producers.xpath.TransactionIdExtractor;

@Component
public abstract class AbstractProducer implements Producer {

    @Autowired
    private TransactionIdExtractor transactionIdExtractor;

    /**
     * Creates a domain event
     *
     * @param body the body of the message
     * @return the domain event
     */
    public List<Event> createEvent(Node body) {
        List<Event> events = produceMessage(body);
        for (Event event : events) {
            event.setEventId(getEventId(body));
        }
        return events;
    }

    /**
     * Should this producer create a message
     *
     * @param exchange camel exchange
     * @return true if the given producer is applicable, false otherwise
     */
    public Boolean filter(Exchange exchange) {
        Node message = exchange.getIn().getBody(Node.class);
        Boolean applicable = isApplicable(message);
        if (applicable) {
            Document doc = exchange.getIn().getBody(Document.class);
            doc.setUserData(Exchange.FILTER_MATCHED, Boolean.TRUE, null);
        }
        return applicable;
    }

    private String getEventId(Node body) {
        try {
            return transactionIdExtractor.getTransactionId(body);
        } catch (XPathExpressionException e) {
            return "No Event Id to extract";
        }
    }

    @Override
    public abstract List<Event> produceMessage(Node message);

    @Override
    public abstract Boolean isApplicable(Node message);
}