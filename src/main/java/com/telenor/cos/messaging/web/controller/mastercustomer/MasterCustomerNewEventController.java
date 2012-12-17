package com.telenor.cos.messaging.web.controller.mastercustomer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNewEvent;
import com.telenor.cos.messaging.web.form.MasterCustomerForm;

/**
 * Controller For MasterCustomer New Event.
 * @author Babaprakash D
 *
 */
@Controller
public class MasterCustomerNewEventController extends MasterCustomerCommonController {
    private static final Logger LOG = LoggerFactory.getLogger(MasterCustomerNewEventController.class);
    
    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/masterCustomerNewEvent", method = RequestMethod.GET)
    public String testMasterCustomerNewEvent() {
        LOG.info("called testMasterCustomerNewEvent() for GET");
        return "redirect:masterCustomerNewEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/masterCustomerNewEventForm", method = RequestMethod.GET)
    public void testNewMasterCustomerEventForm(Model model) {
        MasterCustomerForm form = getMasterCustomerForm();
        model.addAttribute(form);
        LOG.info("called testNewMasterCustomerEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/masterCustomerNewEvent", method = RequestMethod.POST)
    public void testMasterCustomerNewEvent(@ModelAttribute MasterCustomerForm form, Model model) {
        String xml = form.toNewEventXML(MasterCustomerForm.EventType.INSERT);
        LOG.info("called testMasterCustomerNewEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        MasterCustomerNewEvent consumedMasterCustomerEvent = (MasterCustomerNewEvent) getJms().receive(NEW_MASTERCUSTOMERS, correlationId);
        model.addAttribute("result", consumedMasterCustomerEvent);
        LOG.info("received event = " + consumedMasterCustomerEvent);
    }
}
