package com.telenor.cos.messaging.web.controller.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.resource.ResourceTypeIdKeyUpdateEvent;
import com.telenor.cos.messaging.web.form.ResourceForm;
import com.telenor.cos.messaging.web.form.ResourceForm.ResourceUpdateSubType;

/**
 * WebApp controller for {@link ResourceTypeIdKeyUpdateEvent}
 * @author Babaprakash D
 *
 */
@Controller
public class ResourceTypeIdKeyUpdateEventController extends ResourceCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceTypeIdKeyUpdateEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/resourceTypeIdKeyUpdateEvent", method = RequestMethod.GET)
    public String testResourceTypeIdKeyUpdateEvent() {
        LOG.info("called testResourceTypeIdKeyUpdateEvent() for GET");
        return "redirect:resourceTypeIdKeyUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/resourceTypeIdKeyUpdateEventForm", method = RequestMethod.GET)
    public void testResourceTypeIdKeyUpdateEventForm(Model model) {
        ResourceForm form = getResourceForm();
        model.addAttribute(form);
        LOG.info("called testResourceTypeIdKeyUpdateEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/resourceTypeIdKeyUpdateEvent", method = RequestMethod.POST)
    public void testResourceTypeIdKeyUpdateEvent(@ModelAttribute ResourceForm form, Model model) {
        ResourceForm resourceForm = createResourceForm(form);
        LOG.info("called testResourceTypeIdKeyUpdateEvent() for POST. form = " + resourceForm.toString());
        String xml = resourceForm.toUpdateEventXML(ResourceForm.EventType.UPDATE,ResourceUpdateSubType.TYPE_ID_KEY_UPDATE);
        LOG.info("called testResourceTypeIdKeyUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        ResourceTypeIdKeyUpdateEvent consumedResourceTypeIdKeyUpdateEvent = (ResourceTypeIdKeyUpdateEvent) getJms().receive(RESOURCE_TYPE_ID_KEY_UPDATE_TOPIC, correlationId);
        model.addAttribute("result", consumedResourceTypeIdKeyUpdateEvent);
        LOG.info("received event = " + consumedResourceTypeIdKeyUpdateEvent);
    }

    private ResourceForm createResourceForm(ResourceForm form) {
        ResourceForm resourceForm = createUpdateResourceForm(form);
        resourceForm.setResourceTypeIdKey(form.getResourceTypeIdKey());
        return resourceForm;
    }
}
