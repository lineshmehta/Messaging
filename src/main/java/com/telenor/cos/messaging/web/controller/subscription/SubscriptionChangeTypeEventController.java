package com.telenor.cos.messaging.web.controller.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.subscription.SubscriptionChangeTypeEvent;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm.SubscriptionUpdateSubType;

@Controller
public class SubscriptionChangeTypeEventController extends SubscriptionCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionChangeTypeEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     * 
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionChangeTypeEvent", method = RequestMethod.GET)
    public String testSubscriptionChangeTypeEvent() {
        LOG.info("called testSubscriptionChangeTypeEvent() for GET");
        return "redirect:subscriptionChangeTypeEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     * 
     * @param model
     *            model
     */
    @RequestMapping(value = "/subscriptionChangeTypeEventForm", method = RequestMethod.GET)
    public void testSubscriptionChangeTypeEventForm(Model model) {
        SubscriptionForm form = getSubscriptionForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionChangeTypeEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the model.
     * 
     * @param form
     *            form
     * @param model
     *            model
     */
    @RequestMapping(value = "/subscriptionChangeTypeEvent", method = RequestMethod.POST)
    public void testSubscriptionChangeTypeEvent(@ModelAttribute SubscriptionForm form, Model model) {
        LOG.info("called testSubscriptionChangeTypeEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateEventXML(SubscriptionForm.EventType.UPDATE, SubscriptionUpdateSubType.CHANGE_SUBSCRIPTION_TYPE);
        LOG.info("called testSubscriptionChangeTypeEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        SubscriptionChangeTypeEvent consumedSubscriptionEvent = (SubscriptionChangeTypeEvent) getJms().receive(
                SUBSCRIPTION_CHANGE_TYPE_TOPIC, correlationId);
        model.addAttribute("result", consumedSubscriptionEvent);
        LOG.info("received event = " + consumedSubscriptionEvent);
    }
}
