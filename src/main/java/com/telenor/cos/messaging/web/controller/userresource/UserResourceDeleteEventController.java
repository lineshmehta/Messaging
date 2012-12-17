package com.telenor.cos.messaging.web.controller.userresource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.userresource.UserResourceDeleteEvent;
import com.telenor.cos.messaging.web.form.UserResourceForm;

/**
 * WebApp Controller for UserResourceDeleteEvent.
 * @author Babaprakash D
 *
 */
@Controller
public class UserResourceDeleteEventController extends UserResourceCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(UserResourceDeleteEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/userResourceDeleteEvent", method = RequestMethod.GET)
    public String testUserResourceDeleteEvent() {
        LOG.info("called testUserResourceDeleteEvent() for GET");
        return "redirect:userResourceDeleteEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/userResourceDeleteEventForm", method = RequestMethod.GET)
    public void testUserResourceDeleteEventForm(Model model) {
        UserResourceForm userResourceForm = getUserResourceForm();
        model.addAttribute(userResourceForm);
        LOG.info("called testUserResourceDeleteEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param userResourceForm form
     * @param model model
     */
    @RequestMapping(value = "/userResourceDeleteEvent", method = RequestMethod.POST)
    public void testUserResourceDeleteEvent(@ModelAttribute UserResourceForm userResourceForm, Model model) {
        LOG.info("called testUserResourceDeleteEvent() for POST. form = " + userResourceForm.toString());
        String xml = userResourceForm.toDeleteEventXML();
        LOG.info("called testUserResourceDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        UserResourceDeleteEvent consumedEvent = (UserResourceDeleteEvent) getJms().receive(DELETE_USER_RESOURCE_TOPIC, correlationId);
        model.addAttribute("result", consumedEvent);
        LOG.info("received event = " + consumedEvent);
    }
}
