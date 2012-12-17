package com.telenor.cos.messaging.web.controller.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.subscription.SubscriptionChangedStatusEvent;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm.SubscriptionUpdateSubType;

/**
 * Controller for the SubscriptionChangedStatusEvent.
 */
@Controller
public class SubscriptionChangedStatusEventController extends SubscriptionCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionChangedStatusEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionChangedStatusEvent", method = RequestMethod.GET)
    public String testSubscriptionChangedStatusEvent() {
        LOG.info("called testSubscriptionChangedStatusEvent() for GET");
        return "redirect:subscriptionChangedStatusEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionChangedStatusEventForm", method = RequestMethod.GET)
    public void testSubscriptionChangedStatusEventForm(Model model) {
        SubscriptionForm form = getSubscriptionForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionChangedStatusEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/subscriptionChangedStatusEvent", method = RequestMethod.POST)
    public void testSubscriptionChangedStatusEvent(@ModelAttribute SubscriptionForm form, Model model) {
        addSubscriptionDefaultValues(form);
        LOG.info("called testSubscriptionChangedStatusEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateEventXML(SubscriptionForm.EventType.UPDATE, SubscriptionUpdateSubType.CHANGE_SUBSCRIPTION_STATUS);
        LOG.info("called testSubscriptionChangedStatusEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        SubscriptionChangedStatusEvent consumedSubscriptionEvent = (SubscriptionChangedStatusEvent) getJms().receive(SUBSCRIPTION_CHANGE_STATUS_TOPIC, correlationId);
        model.addAttribute("result", consumedSubscriptionEvent);
        LOG.info("received event = " + consumedSubscriptionEvent);
    }
}
