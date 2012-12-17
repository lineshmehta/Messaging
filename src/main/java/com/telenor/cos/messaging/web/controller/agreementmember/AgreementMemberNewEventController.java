package com.telenor.cos.messaging.web.controller.agreementmember;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.agreementmember.AgreementMemberNewEvent;
import com.telenor.cos.messaging.web.form.AgreementMemberForm;

@Controller
public class AgreementMemberNewEventController extends AgreementMemberCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(AgreementMemberNewEventController.class);


    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/agreementMemberNewEvent", method = RequestMethod.GET)
    public String testAgreementMemberNewEvent() {
        return "redirect:agreementMemberNewEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/agreementMemberNewEventForm", method = RequestMethod.GET)
    public void testAgreementMemberNewEventForm(Model model) {
        AgreementMemberForm form = getAgreementMemberForm();
        model.addAttribute(form);
    }


    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param agreementMemberForm the form
     * @param model model
     */
    @RequestMapping(value = "/agreementMemberNewEvent", method = RequestMethod.POST)
    public void testAgreementMemberNewEventForm(@ModelAttribute AgreementMemberForm agreementMemberForm, Model model) {
        String xml = agreementMemberForm.toNewEventXML();
        LOG.info("called testAgreementMemberNewEventForm() for POST. xml = " + xml);

        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);

        AgreementMemberNewEvent consumedAgreementMemberNewEvent = (AgreementMemberNewEvent) getJms().receive(AGREEMENT_MEMBER_NEW, correlationId);
        model.addAttribute("result", consumedAgreementMemberNewEvent);
        LOG.info("received event = " + consumedAgreementMemberNewEvent);
    }

}
