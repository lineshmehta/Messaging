package com.telenor.cos.messaging.web.controller.masterstructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.masterstructure.MasterStructureDeleteEvent;
import com.telenor.cos.messaging.web.form.MasterStructureForm;

/**
 * Controller for the MasterStructureDeleteEvent.
 * @author t798435
 */
@Controller
public class MasterStructureDeleteEventController extends MasterStructureCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(MasterStructureDeleteEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/masterStructureDeleteEvent", method = RequestMethod.GET)
    public String testMasterStructureDeleteEvent() {
        LOG.info("called testMasterStructureDeleteEvent() for GET");
        return "redirect:masterStructureDeleteEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/masterStructureDeleteEventForm", method = RequestMethod.GET)
    public void testMasterStructureDeleteEventForm(Model model) {
        MasterStructureForm form = getMasterStructureForm();
        model.addAttribute(form);
        LOG.info("called testMasterStructureDeleteEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param masterStructureForm form
     * @param model model
     */
    @RequestMapping(value = "/masterStructureDeleteEvent", method = RequestMethod.POST)
    public void testMasterStructureDeleteEvent(@ModelAttribute MasterStructureForm masterStructureForm, Model model) {
        String xml = masterStructureForm.toUpdateEventXML(MasterStructureForm.EventType.DELETE);
        LOG.info("called testMasterStructureDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        MasterStructureDeleteEvent consumedMasterStructureEvent = (MasterStructureDeleteEvent) getJms().receive(DELETED_MASTER_STRUCTURE, correlationId);
        model.addAttribute("result", consumedMasterStructureEvent);
        LOG.info("received event = " + consumedMasterStructureEvent);
    }
}
