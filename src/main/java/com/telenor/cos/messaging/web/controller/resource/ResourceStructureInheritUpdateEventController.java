package com.telenor.cos.messaging.web.controller.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.resource.ResourceStructureInheritUpdateEvent;
import com.telenor.cos.messaging.web.form.ResourceForm;
import com.telenor.cos.messaging.web.form.ResourceForm.ResourceUpdateSubType;

/**
 * WebApp controller for {@link ResourceStructureInheritUpdateEvent}
 * @author Babaprakash D
 *
 */
@Controller
public class ResourceStructureInheritUpdateEventController extends ResourceCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceStructureInheritUpdateEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/resourceStructureInheritUpdateEvent", method = RequestMethod.GET)
    public String testResourceStructureInheritUpdateEvent() {
        LOG.info("called testResourceStructureInheritUpdateEvent() for GET");
        return "redirect:resourceStructureInheritUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/resourceStructureInheritUpdateEventForm", method = RequestMethod.GET)
    public void testResourceStructureInheritUpdateEventForm(Model model) {
        ResourceForm form = getResourceForm();
        model.addAttribute(form);
        LOG.info("called testResourceStructureInheritUpdateEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/resourceStructureInheritUpdateEvent", method = RequestMethod.POST)
    public void testResourceStructureInheritUpdateEvent(@ModelAttribute ResourceForm form, Model model) {
        ResourceForm resourceForm = createResourceForm(form);
        LOG.info("called testResourceStructureInheritUpdateEvent() for POST. form = " + resourceForm.toString());
        String xml = resourceForm.toUpdateEventXML(ResourceForm.EventType.UPDATE,ResourceUpdateSubType.STRUCTURE_INHERIT_UPDATE);
        LOG.info("called testResourceStructureInheritUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        ResourceStructureInheritUpdateEvent consumedResourceStructureInheritUpdateEvent = (ResourceStructureInheritUpdateEvent) getJms().receive(RESOURCE_STRUCTURE_INHERIT_UPDATE_TOPIC, correlationId);
        model.addAttribute("result", consumedResourceStructureInheritUpdateEvent);
        LOG.info("received event = " + consumedResourceStructureInheritUpdateEvent);
    }

    private ResourceForm createResourceForm(ResourceForm form) {
        ResourceForm resourceForm = createUpdateResourceForm(form);
        resourceForm.setResourceHasStructureInherit(form.getResourceHasStructureInherit());
        return resourceForm;
    }
}
