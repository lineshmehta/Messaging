package com.telenor.cos.messaging.web.controller.userref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.userref.UserReferenceDescriptionUpdateEvent;
import com.telenor.cos.messaging.web.form.UserReferenceForm;
import com.telenor.cos.messaging.web.form.UserReferenceForm.UserReferenceUpdateSubType;

@Controller
public class SubscriptionUserRefDescrUpdateController extends SubscriptionUserRefCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionUserRefDescrUpdateController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionUserReferenceDescrUpdateEvent", method = RequestMethod.GET)
    public String testSubscriptionUserReferenceDescrUpdateEvent() {
        LOG.info("called testSubscriptionUserReferenceDescrUpdateEvent() for GET");
        return "redirect:subscriptionUserReferenceDescrUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionUserReferenceDescrUpdateEventForm", method = RequestMethod.GET)
    public void testSubscriptionUserReferenceDescrUpdateEventForm(Model model) {
        LOG.info("called testSubscriptionUserReferenceDescrUpdateEventForm()");
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
    @RequestMapping(value = "/subscriptionUserReferenceDescrUpdateEvent", method = RequestMethod.POST)
    public void testSubscriptionUserReferenceDescrUpdateEvent(@ModelAttribute UserReferenceForm userRefForm, Model model) {
        String xml = userRefForm.toUpdateEventXML(UserReferenceForm.EventType.UPDATE,UserReferenceUpdateSubType.CHANGE_USERREF_DESCR);
        LOG.info("called testSubscriptionUserReferenceDescrUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        UserReferenceDescriptionUpdateEvent consumedUserReferenceDescriptionUpdateEvent = (UserReferenceDescriptionUpdateEvent) getJms().receive(USER_REFERENCE_DESCR_CHANGE_TOPIC, correlationId);
        model.addAttribute("result", consumedUserReferenceDescriptionUpdateEvent);
        LOG.info("received event = " + consumedUserReferenceDescriptionUpdateEvent);
    }
}
