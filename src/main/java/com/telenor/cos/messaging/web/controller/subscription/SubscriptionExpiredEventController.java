package com.telenor.cos.messaging.web.controller.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.subscription.SubscriptionExpiredEvent;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm.SubscriptionUpdateSubType;

/**
 * Controller for the SubscriptionExpiredEvent.
 */
@Controller
public class SubscriptionExpiredEventController extends SubscriptionCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionExpiredEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     * 
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionExpiredEvent", method = RequestMethod.GET)
    public String testSubscriptionExpiredEvent() {
        LOG.info("called testSubscriptionExpiredEvent() for GET");
        return "redirect:subscriptionExpiredEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     * 
     * @param model
     *            model
     */
    @RequestMapping(value = "/subscriptionExpiredEventForm", method = RequestMethod.GET)
    public void testSubscriptionExpiredEventForm(Model model) {
        SubscriptionForm form = getSubscriptionForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionExpiredEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the model.
     * 
     * @param form
     *            form
     * @param model
     *            model
     */
    @RequestMapping(value = "/subscriptionExpiredEvent", method = RequestMethod.POST)
    public void testSubscriptionExpiredEvent(@ModelAttribute SubscriptionForm form, Model model) {
        LOG.info("called testSubscriptionExpiredEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateEventXML(SubscriptionForm.EventType.UPDATE, SubscriptionUpdateSubType.EXPIRED);
        LOG.info("called testSubscriptionExpiredEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        SubscriptionExpiredEvent consumedSubscriptionEvent = (SubscriptionExpiredEvent) getJms().receive(
                SUBSCRIPTION_EXPIRED_TOPIC, correlationId);
        LOG.info("received event = " + consumedSubscriptionEvent);
        model.addAttribute("result", consumedSubscriptionEvent);
        LOG.info("received event = " + consumedSubscriptionEvent);
    }
}
