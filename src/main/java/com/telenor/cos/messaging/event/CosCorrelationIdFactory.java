package com.telenor.cos.messaging.event;


import java.util.UUID;

public final class CosCorrelationIdFactory {
    private CosCorrelationIdFactory() {
    }

    /**
     * creates a messageSelectorExpression for the coscorrelaionId
     *
     * @param correlationId the correlationId
     * @return the message selector expression
     */
    public static String createMessageSelectorExpression(String correlationId) {
        return PostProcessor.HEADER_ID + " = '" + correlationId + "'";
    }

    /**
     * create a postprocessor adding a correlation id
     *
     * @param correlationId the id
     * @return the postProcessor
     */
    public static PostProcessor createPostProcessor(String correlationId) {
        return new PostProcessor(correlationId);
    }

    /**
     * creates an id to use as correlation id
     *
     * @return an unique id
     */
    public static String createCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
