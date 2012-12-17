package com.telenor.cos.messaging.web.controller.userresource;

import com.telenor.cos.messaging.event.userresource.UserResourceResourceIdUpdateEvent;
import com.telenor.cos.messaging.web.form.UserResourceForm;
import com.telenor.cos.messaging.web.form.UserResourceForm.UserResourceUpdateSubType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * WebApp Controller for UserResourceUpdateEvent.
 */
@Controller
public class UserResourceIdUpdateEventController extends UserResourceCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(UserResourceIdUpdateEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/userResourceIdUpdateEvent", method = RequestMethod.GET)
    public String testUserResourceIdUpdateEvent() {
        LOG.info("called testUserResourceIdUpdateEvent() for GET");
        return "redirect:userResourceIdUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/userResourceIdUpdateEventForm", method = RequestMethod.GET)
    public void testUserResourceIdUpdateEventForm(Model model) {
        UserResourceForm userResourceForm = getUserResourceForm();
        model.addAttribute(userResourceForm);
        LOG.info("called testUserResourceIdUpdateEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param userResourceForm form
     * @param model model
     */
    @RequestMapping(value = "/userResourceIdUpdateEvent", method = RequestMethod.POST)
    public void testUserResourceIdUpdateEvent(@ModelAttribute UserResourceForm userResourceForm, Model model) {
        LOG.info("called testUserResourceIdUpdateEvent() for POST. form = " + userResourceForm.toString());
        String xml = userResourceForm.toUpdateEventXML(UserResourceUpdateSubType.RESOURCE_ID_CHANGE);
        LOG.info("called testUserResourceIdUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        UserResourceResourceIdUpdateEvent consumedEvent = (UserResourceResourceIdUpdateEvent) getJms().receive(RESOURCEID_UPDATE_TOPIC, correlationId);
        model.addAttribute("result", consumedEvent);
        LOG.info("received event = " + consumedEvent);
    }
}