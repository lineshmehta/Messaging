package com.telenor.cos.messaging.web.controller.userref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.userref.UserReferenceNewEvent;
import com.telenor.cos.messaging.web.form.UserReferenceForm;

@Controller
public class SubscriptionUserReferenceNewEventController extends SubscriptionUserRefCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionUserReferenceNewEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionUserReferenceNewEvent", method = RequestMethod.GET)
    public String testSubscriptionUserReferenceNewEvent() {
        LOG.info("called testSubscriptionUserReferenceNewEvent() for GET");
        return "redirect:subscriptionUserReferenceNewEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionUserReferenceNewEventForm", method = RequestMethod.GET)
    public void testSubscriptionUserReferenceNewEventForm(Model model) {
        LOG.info("called testSubscriptionUserReferenceNewEventForm()");
        UserReferenceForm userReferenceForm = getUserRefForm();
        model.addAttribute(userReferenceForm);
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param userRefForm userRefForm
     * @param model model
     */
    @RequestMapping(value = "/subscriptionUserReferenceNewEvent", method = RequestMethod.POST)
    public void testSubscriptionUserReferenceNewEvent(@ModelAttribute UserReferenceForm userRefForm, Model model) {
        String xml = userRefForm.toNewEventXML(UserReferenceForm.EventType.INSERT);
        LOG.info("called testSubscriptionUserReferenceNewEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        UserReferenceNewEvent consumedUserReferenceNewEvent = (UserReferenceNewEvent) getJms().receive(USER_REFERENCE_NEW_TOPIC, correlationId);
        model.addAttribute("result", consumedUserReferenceNewEvent);
        LOG.info("received event = " + consumedUserReferenceNewEvent);
    }
}
