package com.telenor.cos.messaging.web.controller.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.customer.CustomerNewEvent;
import com.telenor.cos.messaging.web.form.CustomerForm;

@Controller
public class CustomerNewEventController extends CustomerCommonController{

    private static final Logger LOG = LoggerFactory.getLogger(CustomerNewEventController.class);
    
    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/customerNewEvent", method = RequestMethod.GET)
    public String testCustomerNewEvent() {
        LOG.info("called testcustomerNewEvent() for GET");
        return "redirect:customerNewEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/customerNewEventForm", method = RequestMethod.GET)
    public void testCustomerNewEventForm(Model model) {
        LOG.info("called testCustomerNewEventForm()");
        CustomerForm form = getCustomerForm();
        model.addAttribute(form);
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param customerForm form
     * @param model model
     */
    @RequestMapping(value = "/customerNewEvent", method = RequestMethod.POST)
    public void testCustomerNewEvent(@ModelAttribute CustomerForm customerForm, Model model) {
        String xml = customerForm.toNewEventXML(CustomerForm.EventType.INSERT);
        LOG.info("called testCustomerNewEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        CustomerNewEvent consumedNewCustomerEvent = (CustomerNewEvent) getJms().receive(CUSTOMER_NEW, correlationId);
        model.addAttribute("result", consumedNewCustomerEvent);
        LOG.info("received event = " + consumedNewCustomerEvent);
    }
}
