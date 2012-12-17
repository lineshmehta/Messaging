package com.telenor.cos.messaging.web.controller.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.resource.ResourceNewEvent;
import com.telenor.cos.messaging.web.form.ResourceForm;

/**
 * WebApp controller for ResourceNewEvent.
 * @author Babaprakash D
 *
 */
@Controller
public class ResourceNewEventController extends ResourceCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceNewEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/resourceNewEvent", method = RequestMethod.GET)
    public String testResourceNewEvent() {
        LOG.info("called testResourceNewEvent() for GET");
        return "redirect:resourceNewEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/resourceNewEventForm", method = RequestMethod.GET)
    public void testNewResourceEventForm(Model model) {
        ResourceForm form = getResourceForm();
        model.addAttribute(form);
        LOG.info("called testNewResourceEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/resourceNewEvent", method = RequestMethod.POST)
    public void testResourceNewEvent(@ModelAttribute ResourceForm form, Model model) {
        ResourceForm resourceForm = createResourceForm(form);
        LOG.info("called testResourceNewEvent() for POST. form = " + resourceForm.toString());
        String xml = resourceForm.toNewEventXML(ResourceForm.EventType.INSERT);
        LOG.info("called testResourceNewEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        ResourceNewEvent consumedResourceNewEvent = (ResourceNewEvent) getJms().receive(RESOURCE_NEW_TOPIC, correlationId);
        model.addAttribute("result", consumedResourceNewEvent);
        LOG.info("received event = " + consumedResourceNewEvent);
    }

    private ResourceForm createResourceForm(ResourceForm form) {
        ResourceForm resourceForm = getResourceForm();
        resourceForm.setResourceId(form.getResourceId());
        resourceForm.setResourceTypeId(form.getResourceTypeId());
        resourceForm.setResourceTypeIdKey(form.getResourceTypeIdKey());
        resourceForm.setResourceHasContentInherit(form.getResourceHasContentInherit());
        resourceForm.setResourceHasStructureInherit(form.getResourceHasStructureInherit());
        return resourceForm;
    }
}
