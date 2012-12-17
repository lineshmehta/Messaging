package com.telenor.cos.messaging.web.controller.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.customer.CustomerNameChangeEvent;
import com.telenor.cos.messaging.web.form.CustomerForm;
import com.telenor.cos.messaging.web.form.CustomerForm.CustomerUpdateSubType;

@Controller
public class CustomerNameChangeEventController extends CustomerCommonController{
    
private static final Logger LOG = LoggerFactory.getLogger(CustomerNameChangeEventController.class);
    
    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/customerNameChangeEvent", method = RequestMethod.GET)
    public String testCustomerNameChangeEvent() {
        LOG.info("called testcustomerNameChangeEvent() for GET");
        return "redirect:customerNameChangeEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/customerNameChangeEventForm", method = RequestMethod.GET)
    public void testCustomerNameChangeEventForm(Model model) {
        LOG.info("called testCustomerNameChangeEventForm()");
        CustomerForm form = getCustomerForm();
        form.setCustId(9846);
        model.addAttribute(form);
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param customerForm form
     * @param model model
     */
    @RequestMapping(value = "/customerNameChangeEvent", method = RequestMethod.POST)
    public void testCustomerNameChangeEvent(@ModelAttribute CustomerForm customerForm, Model model) {
        String xml = customerForm.toUpdateEventXML(CustomerForm.EventType.UPDATE, CustomerUpdateSubType.NAME_CHANGE);
        LOG.info("called testCustomerNameChangeEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        CustomerNameChangeEvent consumedNameChangeCustomerEvent = (CustomerNameChangeEvent) getJms().receive(CUSTOMER_NAME_CHANGE, correlationId);
        model.addAttribute("result", consumedNameChangeCustomerEvent);
        LOG.info("received event = " + consumedNameChangeCustomerEvent);
    }
}
