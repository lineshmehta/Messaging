package com.telenor.cos.messaging.routers;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.model.ChoiceDefinition;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.dataformat.XStreamDataFormat;

import com.telenor.cos.messaging.event.Event;


/**
 * The standard router used by messaging to generate XML (XStream) messages
 */
public abstract class StandardXmlFormattingRouter extends StandardFormattingRouter {

    /**
     * The data format
     */
    private static DataFormatDefinition dataformat = createFormater();

    /**
     * @return the formatter for the event
     */
    @Override
    protected DataFormatDefinition getDataFormat() {
        return dataformat;
    }

    /**
     * Return the formatter, if it cannot be created log an error and return null
     *
     * @return the formatter
     */
    private static DataFormatDefinition createFormater() {
        return new XStreamDataFormat("UTF-8");
    }

    @Override
    protected Predicate isEventOfType(final Event.ACTION action) {
        return new Predicate() {
            @Override
            public boolean matches(Exchange exchange) {
                String event = exchange.getIn().getBody(String.class);
                return event.contains("<action>" + action + "</action>");
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected abstract ChoiceDefinition whenPart() throws Exception;

}
