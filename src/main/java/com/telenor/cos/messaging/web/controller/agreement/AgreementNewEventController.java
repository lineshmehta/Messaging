package com.telenor.cos.messaging.web.controller.agreement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.agreement.AgreementEvent;
import com.telenor.cos.messaging.web.form.AgreementForm;

@Controller
public class AgreementNewEventController extends AgreementCommonController{

    
    private static final Logger LOG = LoggerFactory.getLogger(AgreementNewEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/agreementNewEvent", method = RequestMethod.GET)
    public String testAgreementNewEvent() {
        LOG.info("called testAgreementNewEvent() for GET");
        return "redirect:agreementNewEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/agreementNewEventForm", method = RequestMethod.GET)
    public void testAgreementNewEventForm(Model model) {
        AgreementForm form = new AgreementForm();
        model.addAttribute(form);
        LOG.info("called testAgreementNewEventForm()");
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/agreementNewEvent", method = RequestMethod.POST)
    public void testAgreementNewEvent(@ModelAttribute AgreementForm form, Model model) {
        LOG.info("called testAgreementNewEvent() for POST. form = " + form.toString());
        
        String xml = form.toNewXML(AgreementForm.EventType.INSERT);
        LOG.info("called testAgreementNewEvent() for POST. xml = " + xml);
        
        setUp();
        
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        
        AgreementEvent consumedAgreementEvent = (AgreementEvent) getJms().receive(NEW_AGREEMENT, correlationId);
        model.addAttribute("result", consumedAgreementEvent);
        LOG.info("received event = " + consumedAgreementEvent);
    }
}
