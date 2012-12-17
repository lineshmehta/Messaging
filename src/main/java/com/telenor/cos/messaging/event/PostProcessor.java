package com.telenor.cos.messaging.event;

import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.JMSException;
import javax.jms.Message;


public class PostProcessor implements MessagePostProcessor {

    public static final String HEADER_ID = "cosCorrelationId";

    private String correlationId;

    /**
     * Constructor
     *
     * @param correlationId the correalation id to add
     */
    public PostProcessor(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public Message postProcessMessage(Message message) throws JMSException {
        message.setStringProperty(HEADER_ID, correlationId);
        return message;
    }
}
