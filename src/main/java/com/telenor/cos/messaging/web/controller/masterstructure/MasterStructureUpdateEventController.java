package com.telenor.cos.messaging.web.controller.masterstructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.masterstructure.MasterStructureUpdateEvent;
import com.telenor.cos.messaging.web.form.MasterStructureForm;

/**
 * @author t798435
 *
 */
@Controller
public class MasterStructureUpdateEventController extends MasterStructureCommonController {
    
private static final Logger LOG = LoggerFactory.getLogger(MasterStructureUpdateEventController.class);
    
    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/masterStructureUpdateEvent", method = RequestMethod.GET)
    public String testMasterStructureUpdateEvent() {
        LOG.info("called testMasterStructureUpdateEvent() for GET");
        return "redirect:masterStructureUpdateEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/masterStructureUpdateEventForm", method = RequestMethod.GET)
    public void testMasterStructureUpdateEventForm(Model model) {
        LOG.info("called testMasterStructureUpdateEvent()");
        MasterStructureForm form = getMasterStructureForm();
        model.addAttribute(form);
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param masterStructureForm --> MasterStructureForm 
     * @param model model
     */
    @RequestMapping(value = "/masterStructureUpdateEvent", method = RequestMethod.POST)
    public void testMasterStructureUpdateEvent(@ModelAttribute MasterStructureForm masterStructureForm, Model model) {
        MasterStructureForm masterStructForm = createMasterStructureForm(masterStructureForm);
        String xml = masterStructForm.toUpdateEventXML(MasterStructureForm.EventType.UPDATE);
        LOG.info("called testMasterStructureUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        MasterStructureUpdateEvent consumedNameChangeMasterStructureEvent = (MasterStructureUpdateEvent) getJms().receive(UPDATED_MASTER_STRUCTURE, correlationId);
        model.addAttribute("result", consumedNameChangeMasterStructureEvent);
        LOG.info("received event = " + consumedNameChangeMasterStructureEvent);
    }
    
    private MasterStructureForm createMasterStructureForm(MasterStructureForm masterStructureForm) {
        MasterStructureForm form = getMasterStructureForm();
        form.setMastIdMember(masterStructureForm.getMastIdMember());
        form.setMastIdOwner(masterStructureForm.getMastIdOwner());
        form.setOldMastIdOwner(masterStructureForm.getOldMastIdOwner());
        return form;
    }
}