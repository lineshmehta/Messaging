package com.telenor.cos.messaging.web.controller.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.subscription.SubscriptionChangeUserEvent;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm.SubscriptionUpdateSubType;

@Controller
public class SubscriptionChangeUserEventController extends SubscriptionCommonController{
    
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionChangeUserEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionChangeUserEvent", method = RequestMethod.GET)
    public String testSubscriptionChangeUserEvent() {
        LOG.info("called testSubscriptionChangeUserEvent() for GET");
        return "redirect:subscriptionChangeUserEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionChangeUserEventForm", method = RequestMethod.GET)
    public void testSubscriptionChangeUserEventForm(Model model) {
        SubscriptionForm form = getSubscriptionForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionChangeUserEventForm()");
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/subscriptionChangeUserEvent", method = RequestMethod.POST)
    public void testSubscriptionChangeUserEvent(@ModelAttribute SubscriptionForm form, Model model) {
        LOG.info("called testSubscriptionChangeUserEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateEventXML(SubscriptionForm.EventType.UPDATE, SubscriptionUpdateSubType.CHANGE_USER);
        LOG.info("called testSubscriptionChangeUserEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        SubscriptionChangeUserEvent consumedSubscriptionEvent = (SubscriptionChangeUserEvent) getJms().receive(SUBSCRIPTION_CHANGE_USER_TOPIC, correlationId);
        model.addAttribute("result", consumedSubscriptionEvent);
        LOG.info("received event = " + consumedSubscriptionEvent);
    }

}
