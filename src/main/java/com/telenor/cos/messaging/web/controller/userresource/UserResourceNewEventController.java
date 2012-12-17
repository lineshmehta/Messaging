package com.telenor.cos.messaging.web.controller.userresource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.userresource.UserResourceNewEvent;
import com.telenor.cos.messaging.web.form.UserResourceForm;

/**
 * WebApp Controller for UserResourceNewEvent.
 * @author Babaprakash D
 *
 */
@Controller
public class UserResourceNewEventController extends UserResourceCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(UserResourceNewEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/userResourceNewEvent", method = RequestMethod.GET)
    public String testUserResourceNewEvent() {
        LOG.info("called testUserResourceNewEvent() for GET");
        return "redirect:userResourceNewEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/userResourceNewEventForm", method = RequestMethod.GET)
    public void testUserResourceNewEventForm(Model model) {
        UserResourceForm userResourceForm = getUserResourceForm();
        model.addAttribute(userResourceForm);
        LOG.info("called testUserResourceNewEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param userResourceForm form
     * @param model model
     */
    @RequestMapping(value = "/userResourceNewEvent", method = RequestMethod.POST)
    public void testUserResourceNewEvent(@ModelAttribute UserResourceForm userResourceForm, Model model) {
        LOG.info("called testUserResourceNewEvent() for POST. form = " + userResourceForm.toString());
        String xml = userResourceForm.toNewEventXML();
        LOG.info("called testUserResourceNewEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        UserResourceNewEvent consumedEvent = (UserResourceNewEvent) getJms().receive(NEW_USER_RESOURCE_TOPIC, correlationId);
        model.addAttribute("result", consumedEvent);
        LOG.info("received event = " + consumedEvent);
    }
}
