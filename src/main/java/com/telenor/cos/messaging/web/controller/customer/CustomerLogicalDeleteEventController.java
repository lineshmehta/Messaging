package com.telenor.cos.messaging.web.controller.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.customer.CustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.web.form.CustomerForm;
import com.telenor.cos.messaging.web.form.CustomerForm.CustomerUpdateSubType;

@Controller
public class CustomerLogicalDeleteEventController extends CustomerCommonController{

private static final Logger LOG = LoggerFactory.getLogger(CustomerLogicalDeleteEventController.class);
    
    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/customerLogicalDeleteEvent", method = RequestMethod.GET)
    public String testCustomerLogicalDeleteEvent() {
        LOG.info("called testcustomerLogicalDeleteEvent() for GET");
        return "redirect:customerLogicalDeleteEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/customerLogicalDeleteEventForm", method = RequestMethod.GET)
    public void testCustomerLogicalDeleteEventForm(Model model) {
        LOG.info("called testCustomerLogicalDeleteEventForm()");
        CustomerForm form = getCustomerForm();
        form.setInfoIsDeleted(true);
        model.addAttribute(form);
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param customerForm form
     * @param model model
     */
    @RequestMapping(value = "/customerLogicalDeleteEvent", method = RequestMethod.POST)
    public void testCustomerLogicalDeleteEvent(@ModelAttribute CustomerForm customerForm, Model model) {
        String xml = customerForm.toUpdateEventXML(CustomerForm.EventType.UPDATE, CustomerUpdateSubType.LOGICAL_DELETE);
        LOG.info("called testCustomerLogicalDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        CustomerLogicalDeleteEvent consumedLogicalDeleteCustomerEvent = (CustomerLogicalDeleteEvent) getJms().receive(CUSTOMER_LOGICAL_DELETE, correlationId);
        model.addAttribute("result", consumedLogicalDeleteCustomerEvent);
        LOG.info("received event = " + consumedLogicalDeleteCustomerEvent);
    }
}
