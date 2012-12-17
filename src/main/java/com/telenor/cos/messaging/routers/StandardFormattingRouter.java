package com.telenor.cos.messaging.routers;

import org.apache.camel.model.ChoiceDefinition;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.RouteDefinition;


/**
 * The standard router used by messaging to generate messages that are not
 * just serialized objects. Camel supports a number of different formats.
 */
public abstract class StandardFormattingRouter extends StandardRouter {

    /**
     * @return the formatter for the event
     */
    protected abstract DataFormatDefinition getDataFormat();

    /**
     * {@inheritDoc}
     */
    @Override
    protected abstract ChoiceDefinition whenPart() throws Exception;

    /**
     * Override with the default settings, and set the marshaling to use from {@link #getDataFormat()}
     *
     * @param uri the uri
     * @return the route definition
     */
    @Override
    public RouteDefinition from(String uri) {
        return super.from(uri).marshal(getDataFormat());
    }

}
