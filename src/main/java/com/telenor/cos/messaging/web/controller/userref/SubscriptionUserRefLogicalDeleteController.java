package com.telenor.cos.messaging.web.controller.userref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.userref.UserReferenceLogicalDeleteEvent;
import com.telenor.cos.messaging.web.form.UserReferenceForm;
import com.telenor.cos.messaging.web.form.UserReferenceForm.UserReferenceUpdateSubType;

@Controller
public class SubscriptionUserRefLogicalDeleteController extends SubscriptionUserRefCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionUserRefLogicalDeleteController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionUserRefLogicalDeleteEvent", method = RequestMethod.GET)
    public String testSubscriptionUserRefLogicalDeleteEvent() {
        LOG.info("called testSubscriptionUserRefLogicalDeleteEvent() for GET");
        return "redirect:subscriptionUserRefLogicalDeleteEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionUserRefLogicalDeleteEventForm", method = RequestMethod.GET)
    public void testSubscriptionUserRefLogicalDeleteEventForm(Model model) {
        LOG.info("called testSubscriptionUserRefLogicalDeleteEventForm()");
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
    @RequestMapping(value = "/subscriptionUserRefLogicalDeleteEvent", method = RequestMethod.POST)
    public void testSubscriptionUserRefLogicalDeleteEvent(@ModelAttribute UserReferenceForm userRefForm, Model model) {
        String xml = userRefForm.toUpdateEventXML(UserReferenceForm.EventType.UPDATE,UserReferenceUpdateSubType.LOGICAL_DELETE);
        LOG.info("called testSubscriptionUserRefLogicalDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        UserReferenceLogicalDeleteEvent consumedUserReferenceLogicalDeleteEvent = (UserReferenceLogicalDeleteEvent) getJms().receive(USER_REFERENCE_LOGICAL_DELETE_TOPIC, correlationId);
        model.addAttribute("result", consumedUserReferenceLogicalDeleteEvent);
        LOG.info("received event = " + consumedUserReferenceLogicalDeleteEvent);
    }
}
