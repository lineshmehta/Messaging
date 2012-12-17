package com.telenor.cos.messaging.web.controller.masterstructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.masterstructure.MasterStructureNewEvent;
import com.telenor.cos.messaging.web.form.MasterStructureForm;

/**
 * Controller For MasterStructure New Event.
 * @author t798435
 *
 */
@Controller
public class MasterStructureNewEventController extends MasterStructureCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(MasterStructureNewEventController.class);
    
    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/masterStructureNewEvent", method = RequestMethod.GET)
    public String testMasterStructureNewEvent() {
        LOG.info("called testMasterStructureNewEvent() for GET");
        return "redirect:masterStructureNewEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/masterStructureNewEventForm", method = RequestMethod.GET)
    public void testNewMasterStructureEventForm(Model model) {
        MasterStructureForm form = getMasterStructureForm();
        model.addAttribute(form);
        LOG.info("called testNewMasterStructureEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param masterStructureForm - the form
     * @param model model
     */
    @RequestMapping(value = "/masterStructureNewEvent", method = RequestMethod.POST)
    public void testMasterStructureNewEvent(@ModelAttribute MasterStructureForm masterStructureForm, Model model) {
        String xml = masterStructureForm.toNewEventXML(MasterStructureForm.EventType.INSERT);
        LOG.info("called testMasterStructureNewEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        MasterStructureNewEvent consumedMasterStructureEvent = (MasterStructureNewEvent) getJms().receive(NEW_MASTER_STRUCTURE, correlationId);
        model.addAttribute("result", consumedMasterStructureEvent);
        LOG.info("received event = " + consumedMasterStructureEvent);
    }
}
