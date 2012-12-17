package com.telenor.cos.messaging.web.controller.agreement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.agreement.AgreementLogicalDeletedEvent;
import com.telenor.cos.messaging.web.form.AgreementForm;

@Controller
public class AgreementLogicalDeleteEventController extends AgreementCommonController{

    
    private static final Logger LOG = LoggerFactory.getLogger(AgreementLogicalDeleteEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/agreementLogicalDeleteEvent", method = RequestMethod.GET)
    public String testAgreementLogicalDeleteEvent() {
        LOG.info("called testAgreementLogicalDeleteEvent() for GET");
        return "redirect:agreementLogicalDeleteEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/agreementLogicalDeleteEventForm", method = RequestMethod.GET)
    public void testAgreementForm(Model model) {
        AgreementForm form = new AgreementForm();
        model.addAttribute(form);
        LOG.info("called testAgreementLogicalDeleteEventForm()");
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/agreementLogicalDeleteEvent", method = RequestMethod.POST)
    public void testAgreementLogicalDeleteEvent(@ModelAttribute AgreementForm form, Model model) {
        LOG.info("called testAgreementNewEvent() for POST. form = " + form.toString());
        
        String xml = form.toUpdateXML(AgreementForm.EventType.UPDATE);
        LOG.info("called testAgreementLogicalDeleteEvent() for POST. xml = " + xml);
        
        setUp();
        
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        
        AgreementLogicalDeletedEvent consumedAgreementEvent = (AgreementLogicalDeletedEvent) getJms().receive(LOGICAL_DELETED_AGREEMENT, correlationId);
        model.addAttribute("result", consumedAgreementEvent);
        LOG.info("received event = " + consumedAgreementEvent);
    }
}
