package com.telenor.cos.messaging.web.controller.agreementowner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.agreementowner.AgreementOwnerNewEvent;
import com.telenor.cos.messaging.web.form.AgreementOwnerForm;

@Controller
public class AgreementOwnerNewEventController extends AgreementOwnerCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(AgreementOwnerNewEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     * 
     * @return a forward
     */
    @RequestMapping(value = "/agreementOwnerNewEvent", method = RequestMethod.GET)
    public String testAgreementOwnerNewEvent() {
        return "redirect:agreementOwnerNewEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     * 
     * @param model
     *            model
     */
    @RequestMapping(value = "/agreementOwnerNewEventForm", method = RequestMethod.GET)
    public void testAgreementOwnerNewEventForm(Model model) {
        AgreementOwnerForm form = getAgreementOwnerForm();
        model.addAttribute(form);
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the model.
     * 
     * @param agreementOwnerForm
     *            the form
     * @param model
     *            model
     */
    @RequestMapping(value = "/agreementOwnerNewEvent", method = RequestMethod.POST)
    public void testAgreementOwnerNewEventForm(@ModelAttribute AgreementOwnerForm agreementOwnerForm, Model model) {
        String xml = agreementOwnerForm.toNewEventXML();
        LOG.info("called testAgreementOwnerNewEventForm() for POST. xml = " + xml);

        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);

        AgreementOwnerNewEvent consumedAgreementOwnerNewEvent = (AgreementOwnerNewEvent) getJms().receive(AGREEMENT_OWNER_NEW,
                correlationId);
        model.addAttribute("result", consumedAgreementOwnerNewEvent);
        LOG.info("received event = " + consumedAgreementOwnerNewEvent);
    }
}
