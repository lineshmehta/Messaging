package com.telenor.cos.messaging.web.controller.mastercustomer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.web.form.MasterCustomerForm;

/**
 * Controller for the MasterCustomerLogicalDeleteEvent.
 */
@Controller
public class MasterCustomerLogicalDeleteEventController extends MasterCustomerCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(MasterCustomerLogicalDeleteEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/masterCustomerLogicalDeleteEvent", method = RequestMethod.GET)
    public String testMasterCustomerLogicalDeleteEvent() {
        LOG.info("called testMasterCustomerLogicalDeleteEvent() for GET");
        return "redirect:masterCustomerLogicalDeleteEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/masterCustomerLogicalDeleteEventForm", method = RequestMethod.GET)
    public void testMasterCustomerLogicalDeleteEventForm(Model model) {
        MasterCustomerForm form = getMasterCustomerForm();
        model.addAttribute(form);
        LOG.info("called testMasterCustomerLogicalDeleteEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/masterCustomerLogicalDeleteEvent", method = RequestMethod.POST)
    public void testMasterCustomerLogicalDeleteEvent(@ModelAttribute MasterCustomerForm form, Model model) {
        String xml = form.toUpdateEventXML(MasterCustomerForm.EventType.UPDATE,MasterCustomerForm.MasterCustomerUpdateSubType.LOGICAL_DELETE);
        LOG.info("called testMasterCustomerLogicalDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        MasterCustomerLogicalDeleteEvent consumedMasterCustomerEvent = (MasterCustomerLogicalDeleteEvent) getJms().receive(DELETED_MASTERCUSTOMERS, correlationId);
        model.addAttribute("result", consumedMasterCustomerEvent);
        LOG.info("received event = " + consumedMasterCustomerEvent);
    }
}
