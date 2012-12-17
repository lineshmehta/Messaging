package com.telenor.cos.messaging.web.controller.agreementmember;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.agreementmember.AgreementMemberLogicalDeleteEvent;
import com.telenor.cos.messaging.web.controller.agreement.AgreementLogicalDeleteEventController;
import com.telenor.cos.messaging.web.form.AgreementMemberForm;

@Controller
public class AgreementMemberLogicalDeleteController extends AgreementMemberCommonController{

    private static final Logger LOG = LoggerFactory.getLogger(AgreementLogicalDeleteEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/agreementMemberLogicalDeleteEvent", method = RequestMethod.GET)
    public String testAgreementMemberLogicalDeleteEvent() {
        LOG.info("called testAgreementMemberLogicalDeleteEvent() for GET");
        return "redirect:agreementMemberLogicalDeleteEventForm";
    }
    
    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/agreementMemberLogicalDeleteEventForm", method = RequestMethod.GET)
    public void testAgreementMemberForm(Model model) {
        AgreementMemberForm form = new AgreementMemberForm();
        model.addAttribute(form);
        LOG.info("called testAgreementMemberLogicalDeleteEventForm()");
    }
    
    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param form form
     * @param model model
     */
    @RequestMapping(value = "/agreementMemberLogicalDeleteEvent", method = RequestMethod.POST)
    public void testAgreementMemberLogicalDeleteEvent(@ModelAttribute AgreementMemberForm form, Model model) {
        LOG.info("called testAgreementMemberLogicalDeleteEvent() for POST. form = " + form.toString());
        String xml = form.toUpdateEventXML();
        LOG.info("called testAgreementMemberLogicalDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        AgreementMemberLogicalDeleteEvent consumedAgreementMemberEvent = (AgreementMemberLogicalDeleteEvent) getJms().receive(LOGICAL_DELETED_AGREEMENT_MEMBER, correlationId);
        model.addAttribute("result", consumedAgreementMemberEvent);
        LOG.info("received event = " + consumedAgreementMemberEvent);
    }
}
