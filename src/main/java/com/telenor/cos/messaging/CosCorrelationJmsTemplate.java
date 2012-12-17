package com.telenor.cos.messaging;

import static com.telenor.cos.messaging.event.CosCorrelationIdFactory.createCorrelationId;
import static com.telenor.cos.messaging.event.CosCorrelationIdFactory.createMessageSelectorExpression;
import static com.telenor.cos.messaging.event.CosCorrelationIdFactory.createPostProcessor;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.event.Event;

public class CosCorrelationJmsTemplate extends JmsTemplate {
    /**
     * Send a message, with an added header property
     *
     * @param destinationName destination to send to
     * @param message         the message to send
     * @return the correlation id to use when receiving
     */
    public String send(String destinationName, Object message) {
        String correlationId = createCorrelationId();
        super.convertAndSend(destinationName, message, createPostProcessor(correlationId));
        return correlationId;
    }

    /**
     * Receive an event with the given correlationId
     *
     * @param destinationName destination to receieve from
     * @param correlationId   correlation id
     * @return the event
     */
    public Event receive(String destinationName, String correlationId) {
        return (Event) super.receiveSelectedAndConvert(destinationName, createMessageSelectorExpression(correlationId));
    }
    
}
