package com.telenor.cos.messaging.web.controller.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.resource.ResourceLogicalDeleteEvent;
import com.telenor.cos.messaging.web.form.ResourceForm;
import com.telenor.cos.messaging.web.form.ResourceForm.ResourceUpdateSubType;

/**
 * WebApp Controller for ResourceLogicalDelete Event.
 * @author Babaprakash D
 *
 */
@Controller
public class ResourceLogicalDeleteEventController extends ResourceCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceLogicalDeleteEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/resourceLogicalDeleteEvent", method = RequestMethod.GET)
    public String testResourceLogicalDeleteEvent() {
        LOG.info("called testResourceLogicalDeleteEvent() for GET");
        return "redirect:resourceLogicalDeleteEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/resourceLogicalDeleteEventForm", method = RequestMethod.GET)
    public void testResourceLogicalDeleteEventForm(Model model) {
        ResourceForm form = getResourceForm();
        model.addAttribute(form);
        LOG.info("called testResourceLogicalDeleteEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/resourceLogicalDeleteEvent", method = RequestMethod.POST)
    public void testResourceLogicalDeleteEvent(@ModelAttribute ResourceForm form, Model model) {
        ResourceForm resourceForm = createResourceForm(form);
        LOG.info("called testResourceLogicalDeleteEvent() for POST. form = " + resourceForm.toString());
        String xml = resourceForm.toUpdateEventXML(ResourceForm.EventType.UPDATE,ResourceUpdateSubType.LOGICAL_DELETE);
        LOG.info("called testResourceLogicalDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        ResourceLogicalDeleteEvent consumedResourceLogicalDeleteEvent = (ResourceLogicalDeleteEvent) getJms().receive(RESOURCE_LOGICAL_DELETE_TOPIC, correlationId);
        model.addAttribute("result", consumedResourceLogicalDeleteEvent);
        LOG.info("received event = " + consumedResourceLogicalDeleteEvent);
    }

    private ResourceForm createResourceForm(ResourceForm form) {
        ResourceForm resourceForm = createUpdateResourceForm(form);
        resourceForm.setInfoIsDeleted(form.isInfoIsDeleted());
        return resourceForm;
    }
}