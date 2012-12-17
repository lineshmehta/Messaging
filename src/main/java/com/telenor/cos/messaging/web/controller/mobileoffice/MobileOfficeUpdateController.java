package com.telenor.cos.messaging.web.controller.mobileoffice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeUpdateEvent;
import com.telenor.cos.messaging.web.form.MobileOfficeForm;

@Controller
public class MobileOfficeUpdateController extends MobileOfficeCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(MobileOfficeUpdateController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/mobileOfficeUpdateEvent", method = RequestMethod.GET)
    public String testMobileOfficeUpdateEvent() {
        LOG.info("called testMobileOfficeUpdateEvent() for GET");
        return "redirect:mobileOfficeUpdateEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/mobileOfficeUpdateEventForm", method = RequestMethod.GET)
    public void testMobileOfficeUpdateEventForm(Model model) {
        MobileOfficeForm form = getMobileOfficeForm();
        model.addAttribute(form);
        LOG.info("called testMobileOfficeUpdateEventForm()");
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/mobileOfficeUpdateEvent", method = RequestMethod.POST)
    public void testMobileOfficeUpdateEvent(@ModelAttribute MobileOfficeForm form, Model model) {
        LOG.info("called testMobileOfficeUpdateEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateXML(MobileOfficeForm.EventType.UPDATE, MobileOfficeForm.MobileOfficeUpdateSubType.CHANGE_EXTENSION_NUMBER);
        LOG.info("called testMobileOfficeUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        MobileOfficeUpdateEvent consumedMobileOfficeEvent = (MobileOfficeUpdateEvent) getJms().receive(UPDATE_MOBILE_OFFICE, correlationId);
        model.addAttribute("result", consumedMobileOfficeEvent);
        LOG.info("received event = " + consumedMobileOfficeEvent);
    }
}
