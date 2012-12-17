package com.telenor.cos.messaging.web.controller.mobileoffice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeNewEvent;
import com.telenor.cos.messaging.web.form.MobileOfficeForm;

@Controller
public class MobileOfficeNewEventController extends MobileOfficeCommonController{

    private static final Logger LOG = LoggerFactory.getLogger(MobileOfficeNewEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/mobileOfficeNewEvent", method = RequestMethod.GET)
    public String testMobileOfficeNewEvent() {
        LOG.info("called testMobileOfficeNewEvent() for GET");
        return "redirect:mobileOfficeNewEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/mobileOfficeNewEventForm", method = RequestMethod.GET)
    public void testMobileOfficeNewEventForm(Model model) {
        MobileOfficeForm form = getMobileOfficeForm();
        model.addAttribute(form);
        LOG.info("called testMobileOfficeNewEventForm()");
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/mobileOfficeNewEvent", method = RequestMethod.POST)
    public void testMobileOfficeNewEvent(@ModelAttribute MobileOfficeForm form, Model model) {
        LOG.info("called testMobileOfficeNewEvent() for POST. form = " + form.toString());
        String xml = form.toNewXML(MobileOfficeForm.EventType.INSERT);
        LOG.info("called testMobileOfficeNewEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        MobileOfficeNewEvent consumedMobileOfficeEvent = (MobileOfficeNewEvent) getJms().receive(NEW_MOBILE_OFFICE, correlationId);
        model.addAttribute("result", consumedMobileOfficeEvent);
        LOG.info("received event = " + consumedMobileOfficeEvent);
    }
}
