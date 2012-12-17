package com.telenor.cos.messaging.web.controller.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.subscription.SubscriptionChangedAccountEvent;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm.SubscriptionUpdateSubType;

/**
 * Controller for the SubscriptionChangedAccountEvent.
 */
@Controller
public class SubscriptionChangedAccountEventController extends SubscriptionCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionChangedAccountEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionChangedAccountEvent", method = RequestMethod.GET)
    public String testSubscriptionChangedAccountEvent() {
        LOG.info("called testSubscriptionChangedAccountEvent() for GET");
        return "redirect:subscriptionChangedAccountEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionChangedAccountEventForm", method = RequestMethod.GET)
    public void testSubscriptionChangedAccountEventForm(Model model) {
        SubscriptionForm form = getSubscriptionForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionChangedAccountEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/subscriptionChangedAccountEvent", method = RequestMethod.POST)
    public void testSubscriptionChangedAccountEvent(@ModelAttribute SubscriptionForm form, Model model) {
        LOG.info("called testSubscriptionChangedAccountEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateEventXML(SubscriptionForm.EventType.UPDATE,SubscriptionUpdateSubType.CHANGE_ACCOUNT);
        LOG.info("called testSubscriptionChangedAccountEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        SubscriptionChangedAccountEvent consumedSubscriptionEvent = (SubscriptionChangedAccountEvent) getJms().receive(SUBSCRIPTION_CHANGE_ACCOUNT_TOPIC, correlationId);
        model.addAttribute("result", consumedSubscriptionEvent);
        LOG.info("received event = " + consumedSubscriptionEvent);
    }
}
