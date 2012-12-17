package com.telenor.cos.messaging.web.controller.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.subscription.SubscriptionLogicalDeleteEvent;
import com.telenor.cos.messaging.web.form.SubscriptionForm;

/**
 * Controller for the SubscriptionLogicalDeleteEvent.
 */
@Controller
public class SubscriptionLogicalDeleteEventController extends SubscriptionCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionLogicalDeleteEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionLogicalDeleteEvent", method = RequestMethod.GET)
    public String testSubscriptionLogicalDeleteEvent() {
        LOG.info("called testSubscriptionLogicalDeleteEvent() for GET");
        return "redirect:subscriptionLogicalDeleteEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionLogicalDeleteEventForm", method = RequestMethod.GET)
    public void testSubscriptionLogicalDeleteEventForm(Model model) {
        SubscriptionForm form = getSubscriptionForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionLogicalDeleteEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/subscriptionLogicalDeleteEvent", method = RequestMethod.POST)
    public void testSubscriptionLogicalDeleteEvent(@ModelAttribute SubscriptionForm form, Model model) {
        LOG.info("called testSubscriptionLogicalDeleteEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateEventXML(SubscriptionForm.EventType.UPDATE,SubscriptionForm.SubscriptionUpdateSubType.LOGICAL_DELETE);
        LOG.info("called testSubscriptionLogicalDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        SubscriptionLogicalDeleteEvent consumedSubscriptionEvent = (SubscriptionLogicalDeleteEvent) getJms().receive(SUBSCRIPTION_LOGICAL_DELETE_TOPIC, correlationId);
        model.addAttribute("result", consumedSubscriptionEvent);
        LOG.info("received event = " + consumedSubscriptionEvent);
    }
}
