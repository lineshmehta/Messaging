package com.telenor.cos.messaging.web.controller.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.resource.ResourceContentInheritUpdateEvent;
import com.telenor.cos.messaging.web.form.ResourceForm;
import com.telenor.cos.messaging.web.form.ResourceForm.ResourceUpdateSubType;

/**
 * WebApp controller for ResourceContentInheritUpdateEvent.
 * @author Babaprakash D
 *
 */
@Controller
public class ResourceContentInheritUpdateEventController extends ResourceCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceContentInheritUpdateEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/resourceContentInheritUpdateEvent", method = RequestMethod.GET)
    public String testResourceContentInheritUpdateEvent() {
        LOG.info("called testResourceContentInheritUpdateEvent() for GET");
        return "redirect:resourceContentInheritUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/resourceContentInheritUpdateEventForm", method = RequestMethod.GET)
    public void testResourceContentInheritUpdateEventForm(Model model) {
        ResourceForm form = getResourceForm();
        model.addAttribute(form);
        LOG.info("called testResourceContentInheritUpdateEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/resourceContentInheritUpdateEvent", method = RequestMethod.POST)
    public void testResourceContentInheritUpdateEvent(@ModelAttribute ResourceForm form, Model model) {
        ResourceForm resourceForm = createResourceForm(form);
        LOG.info("called testResourceContentInheritUpdateEvent() for POST. form = " + resourceForm.toString());
        String xml = resourceForm.toUpdateEventXML(ResourceForm.EventType.UPDATE,ResourceUpdateSubType.CONTENT_INHERIT_UPDATE);
        LOG.info("called testResourceContentInheritUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        ResourceContentInheritUpdateEvent consumedResourceContentInheritUpdateEvent = (ResourceContentInheritUpdateEvent) getJms().receive(RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC, correlationId);
        model.addAttribute("result", consumedResourceContentInheritUpdateEvent);
        LOG.info("received event = " + consumedResourceContentInheritUpdateEvent);
    }

    private ResourceForm createResourceForm(ResourceForm form) {
        ResourceForm resourceForm = createUpdateResourceForm(form);
        resourceForm.setResourceHasContentInherit(form.getResourceHasContentInherit());
        return resourceForm;
    }
}
