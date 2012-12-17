package com.telenor.cos.messaging.web.controller.mobileoffice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeDeleteEvent;
import com.telenor.cos.messaging.web.form.MobileOfficeForm;


@Controller
public class MobileOfficeDeleteEventController extends MobileOfficeCommonController {
    
    private static final Logger LOG = LoggerFactory.getLogger(MobileOfficeDeleteEventController.class);

    /**
     * The plain HTTP GET mapping. just forwarding the GET including the model.
     * 
     * @return a forward
     */
    @RequestMapping(value = "/mobileOfficeDeleteEvent", method = RequestMethod.GET )
    public String testMobileOfficeDeleteEvent() {
        LOG.info("Called testMobileOfficeDeleteEvent() for GET");
        return "redirect:mobileOfficeDeleteEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/mobileOfficeDeleteEventForm", method = RequestMethod.GET)
    public void testMobileOfficeDeleteEventForm(Model model) {
        MobileOfficeForm form = getMobileOfficeForm();
        model.addAttribute(form);
        LOG.info("called testMobileOfficeDeleteEventForm()");
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/mobileOfficeDeleteEvent", method = RequestMethod.POST)
    public void testMobileOfficeDeleteEvent(@ModelAttribute MobileOfficeForm form, Model model) {
        LOG.info("called testMobileOfficeDeleteEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateXML(MobileOfficeForm.EventType.UPDATE, MobileOfficeForm.MobileOfficeUpdateSubType.LOGICAL_DELETE);
        LOG.info("called testMobileOfficeDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        MobileOfficeDeleteEvent consumedMobileOfficeEvent = (MobileOfficeDeleteEvent) getJms().receive(DELETE_MOBILE_OFFICE, correlationId);
        model.addAttribute("result", consumedMobileOfficeEvent);
        LOG.info("received event = " + consumedMobileOfficeEvent);
    }
}
