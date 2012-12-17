package com.telenor.cos.messaging.web.controller.subscription;

import com.telenor.cos.messaging.event.subscription.SubscriptionSecretNumberEvent;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm.SubscriptionUpdateSubType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for the SubscriptionChangedAccountEvent.
 */
@Controller
public class SubscriptionSecretNumberEventController extends SubscriptionCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionSecretNumberEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionSecretNumberEvent", method = RequestMethod.GET)
    public String testSubscriptionBarredEvent() {
        LOG.info("called testSubscriptionSecretNumberEvent() for GET");
        return "redirect:subscriptionSecretNumberEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionSecretNumberEventForm", method = RequestMethod.GET)
    public void testSubscriptionSecretNumberEventForm(Model model) {
        SubscriptionForm form = getSubscriptionForm();
        model.addAttribute(form);
        LOG.info("called testSubscriptionSecretNumberEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/subscriptionSecretNumberEvent", method = RequestMethod.POST)
    public void testSubscriptionSecretNumberEvent(@ModelAttribute SubscriptionForm form, Model model) {
        addSubscriptionDefaultValues(form);
        LOG.info("called testSubscriptionSecretNumberEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateEventXML(SubscriptionForm.EventType.UPDATE, SubscriptionUpdateSubType.SECRETNUMBER_CHANGE);
        LOG.info("called testSubscriptionSecretNumberEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        SubscriptionSecretNumberEvent consumedSubscriptionEvent = (SubscriptionSecretNumberEvent) getJms().receive(SUBSCRIPTION_SECRET_NUMBER_UPDATE_TOPIC, correlationId);
        model.addAttribute("result", consumedSubscriptionEvent);
        LOG.info("received event = " + consumedSubscriptionEvent);
    }
}
