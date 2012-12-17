package com.telenor.cos.messaging.routers;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ShutdownRoute;
import org.apache.camel.ShutdownRunningTask;
import org.apache.camel.model.ChoiceDefinition;
import org.apache.camel.model.RouteDefinition;

import com.telenor.cos.messaging.CosMessagingException;
import com.telenor.cos.messaging.event.Event;

/**
 * The standard router used by messaging
 */
public abstract class StandardRouter extends AbstractRouter {
    
    protected static final String CLOSE_BRACKET = "]";

    /**
     * {@inheritDoc}
     *
     * Sets up the default types for the events
     */
    @Override
    public final void configure() throws Exception {
       setErrorHandlers();
       whenPart()
           .otherwise()
           .process(new Processor() {
               public void process(Exchange exchange) throws Exception {
                   Event event = exchange.getIn().getBody(Event.class);
                   if (event == null) {
                       throw new CosMessagingException(getClass().getName() + " - Recieved a null event!", null);
                   }
                   throw new CosMessagingException(getClass().getName() + " - Invalid Message Received from source for event [" + event.toString() +" ]", null);
               }
           })
           .end();
    }

    /**
     * Override with the default settings
     *
     * @param uri the uri
     * @return the route definition
     */
    @Override
    public RouteDefinition from(String uri) {
        return super.from(uri)
            .to("log:com.telenor.cos.messaging?level=DEBUG")
            .shutdownRoute(ShutdownRoute.Defer)
            .shutdownRunningTask(ShutdownRunningTask.CompleteAllTasks);
    }

    /**
     * This is where the details for this route in implemented as follows.
     * NOTE: This should be of the sort
     *
     * protected ChoiceDefinition whenPart() throws Exception{
     *  return from(uri)
     *       .choice()
     *       .when(isEventOfType(CREATED)).log(LoggingLevel.DEBUG, "Got an account of type: " + CREATED)
     *       .to(NEW_TOPIC)
     * }
     *
     * @return the choice definition defined
     * @throws Exception on error
     */
    protected abstract ChoiceDefinition whenPart() throws Exception;

}
