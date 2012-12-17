package com.telenor.cos.messaging.web.controller.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.resource.ResourceTypeIdUpdateEvent;
import com.telenor.cos.messaging.web.form.ResourceForm;
import com.telenor.cos.messaging.web.form.ResourceForm.ResourceUpdateSubType;

/**
 * WebApp controller for {@link ResourceTypeIdUpdateEvent}
 * @author Babaprakash D
 *
 */
@Controller
public class ResourceTypeIdUpdateEventController extends ResourceCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceTypeIdUpdateEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/resourceTypeIdUpdateEvent", method = RequestMethod.GET)
    public String testResourceTypeIdUpdateEvent() {
        LOG.info("called testResourceTypeIdUpdateEvent() for GET");
        return "redirect:resourceTypeIdUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/resourceTypeIdUpdateEventForm", method = RequestMethod.GET)
    public void testResourceTypeIdUpdateEventForm(Model model) {
        ResourceForm form = getResourceForm();
        model.addAttribute(form);
        LOG.info("called testResourceTypeIdUpdateEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/resourceTypeIdUpdateEvent", method = RequestMethod.POST)
    public void testResourceTypeIdUpdateEvent(@ModelAttribute ResourceForm form, Model model) {
        ResourceForm resourceForm = createResourceForm(form);
        LOG.info("called testResourceTypeIdUpdateEvent() for POST. form = " + resourceForm.toString());
        String xml = resourceForm.toUpdateEventXML(ResourceForm.EventType.UPDATE,ResourceUpdateSubType.TYPE_ID_UPDATE);
        LOG.info("called testResourceTypeIdUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        ResourceTypeIdUpdateEvent consumedResourceTypeIdUpdateEvent = (ResourceTypeIdUpdateEvent) getJms().receive(RESOURCE_TYPE_ID_UPDATE_TOPIC, correlationId);
        model.addAttribute("result", consumedResourceTypeIdUpdateEvent);
        LOG.info("received event = " + consumedResourceTypeIdUpdateEvent);
    }

    private ResourceForm createResourceForm(ResourceForm form) {
        ResourceForm resourceForm = createUpdateResourceForm(form);
        resourceForm.setResourceTypeId(form.getResourceTypeId());
        return resourceForm;
    }
}
