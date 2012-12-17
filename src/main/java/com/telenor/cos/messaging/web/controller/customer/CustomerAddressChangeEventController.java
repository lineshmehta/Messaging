package com.telenor.cos.messaging.web.controller.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.customer.CustomerAddressChangeEvent;
import com.telenor.cos.messaging.web.form.CustomerForm;
import com.telenor.cos.messaging.web.form.CustomerForm.CustomerUpdateSubType;

@Controller
public class CustomerAddressChangeEventController extends CustomerCommonController{

    private static final Logger LOG = LoggerFactory.getLogger(CustomerAddressChangeEventController.class);
    
    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/customerAddressChangeEvent", method = RequestMethod.GET)
    public String testCustomerAddressChangeEvent() {
        LOG.info("called testcustomerAddressChangeEvent() for GET");
        return "redirect:customerAddressChangeEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/customerAddressChangeEventForm", method = RequestMethod.GET)
    public void testCustomerAddressChangeEventForm(Model model) {
        LOG.info("called testCustomerAddressChangeEventForm()");
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
    @RequestMapping(value = "/customerAddressChangeEvent", method = RequestMethod.POST)
    public void testCustomerAddressChangeEvent(@ModelAttribute CustomerForm customerForm, Model model) {
        String xml = customerForm.toUpdateEventXML(CustomerForm.EventType.UPDATE, CustomerUpdateSubType.ADRESS_CHANGE);
        LOG.info("called testCustomerAddressChangeEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        CustomerAddressChangeEvent consumedAddressChangeCustomerEvent = (CustomerAddressChangeEvent) getJms().receive(CUSTOMER_ADRESS_CHANGE, correlationId);
        model.addAttribute("result", consumedAddressChangeCustomerEvent);
        LOG.info("received event = " + consumedAddressChangeCustomerEvent);
    }
}
