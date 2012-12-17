package com.telenor.cos.messaging.web.controller.userresource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.userresource.UserResourceCsUserIdUpdateEvent;
import com.telenor.cos.messaging.web.form.UserResourceForm;
import com.telenor.cos.messaging.web.form.UserResourceForm.UserResourceUpdateSubType;

@Controller
public class UserResourceCsUserIdUpdateEventController extends UserResourceCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(UserResourceCsUserIdUpdateEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/userResourceCsUserIdUpdateEvent", method = RequestMethod.GET)
    public String testUserResourceCsUserIdUpdateEvent() {
        LOG.info("called testUserResourceCsUserIdUpdateEvent() for GET");
        return "redirect:userResourceCsUserIdUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/userResourceCsUserIdUpdateEventForm", method = RequestMethod.GET)
    public void testUserResourceCsUserIdUpdateEventForm(Model model) {
        UserResourceForm userResourceForm = getUserResourceForm();
        model.addAttribute(userResourceForm);
        LOG.info("called testUserResourceCsUserIdUpdateEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param userResourceForm form
     * @param model model
     */
    @RequestMapping(value = "/userResourceCsUserIdUpdateEvent", method = RequestMethod.POST)
    public void testUserResourceCsUserIdUpdateEvent(@ModelAttribute UserResourceForm userResourceForm, Model model) {
        LOG.info("called testUserResourceCsUserIdUpdateEvent() for POST. form = " + userResourceForm.toString());
        String xml = userResourceForm.toUpdateEventXML(UserResourceUpdateSubType.CS_USERID_CHANGE);
        LOG.info("called testUserResourceCsUserIdUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        UserResourceCsUserIdUpdateEvent consumedEvent = (UserResourceCsUserIdUpdateEvent) getJms().receive(CSUSERID_UPDATE_TOPIC, correlationId);
        model.addAttribute("result", consumedEvent);
        LOG.info("received event = " + consumedEvent);
    }
}
