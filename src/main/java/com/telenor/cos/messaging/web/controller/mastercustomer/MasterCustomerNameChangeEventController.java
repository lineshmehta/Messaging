package com.telenor.cos.messaging.web.controller.mastercustomer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNameChangeEvent;
import com.telenor.cos.messaging.web.form.MasterCustomerForm;

@Controller
public class MasterCustomerNameChangeEventController extends MasterCustomerCommonController{
    
private static final Logger LOG = LoggerFactory.getLogger(MasterCustomerNameChangeEventController.class);
    
    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/masterCustomerNameChangeEvent", method = RequestMethod.GET)
    public String testMasterCustomerNameChangeEvent() {
        LOG.info("called testMasterCustomerNameChangeEvent() for GET");
        return "redirect:masterCustomerNameChangeEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/masterCustomerNameChangeEventForm", method = RequestMethod.GET)
    public void testMasterCustomerNameChangeEvent(Model model) {
        LOG.info("called testMasterCustomerNameChangeEvent()");
        MasterCustomerForm form = getMasterCustomerForm();
        model.addAttribute(form);
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param masterCustomerForm --> MasterCustomerForm 
     * @param model model
     */
    @RequestMapping(value = "/masterCustomerNameChangeEvent", method = RequestMethod.POST)
    public void testMasterCustomerNameChangeEvent(@ModelAttribute MasterCustomerForm masterCustomerForm, Model model) {
        MasterCustomerForm masterCustForm = createMasterCustomerForm(masterCustomerForm);
        String xml = masterCustForm.toUpdateEventXML(MasterCustomerForm.EventType.UPDATE, MasterCustomerForm.MasterCustomerUpdateSubType.NAME_CHANGE);
        LOG.info("called testMasterCustomerNameChangeEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        MasterCustomerNameChangeEvent consumedNameChangeMasterCustomerEvent = (MasterCustomerNameChangeEvent) getJms().receive(MASTERCUSTOMER_NAME_CHANGE, correlationId);
        model.addAttribute("result", consumedNameChangeMasterCustomerEvent);
        LOG.info("received event = " + consumedNameChangeMasterCustomerEvent);
    }
    
    private MasterCustomerForm createMasterCustomerForm(MasterCustomerForm masterCustomerForm) {
        MasterCustomerForm form = getMasterCustomerForm();
        form.setMasterId(masterCustomerForm.getMasterId());
        form.setCustFirstName(masterCustomerForm.getCustFirstName());
        form.setCustMiddleName(masterCustomerForm.getCustMiddleName());
        form.setCustLastName(masterCustomerForm.getCustLastName());
        form.setOldFirstName(masterCustomerForm.getOldFirstName());
        form.setOldLastName(masterCustomerForm.getOldLastName());
        form.setOldMiddleName(masterCustomerForm.getOldMiddleName());
        return form;
    }
}