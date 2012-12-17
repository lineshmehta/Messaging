package com.telenor.cos.messaging.web.controller.userref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.userref.InvoiceReferenceUpdateEvent;
import com.telenor.cos.messaging.web.form.UserReferenceForm;
import com.telenor.cos.messaging.web.form.UserReferenceForm.UserReferenceUpdateSubType;

@Controller
public class SubscriptionInvoiceRefUpdateController extends SubscriptionUserRefCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionInvoiceRefUpdateController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/subscriptionInvoiceReferenceUpdateEvent", method = RequestMethod.GET)
    public String testSubscriptionInvoiceReferenceUpdateEvent() {
        LOG.info("called testSubscriptionInvoiceReferenceUpdateEvent() for GET");
        return "redirect:subscriptionInvoiceReferenceUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/subscriptionInvoiceReferenceUpdateEventForm", method = RequestMethod.GET)
    public void testSubscriptionInvoiceReferenceUpdateEventForm(Model model) {
        LOG.info("called testSubscriptionInvoiceReferenceUpdateEventForm()");
        UserReferenceForm userReferenceForm = getUserRefForm();
        model.addAttribute(userReferenceForm);
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param userReferenceForm userRefForm
     * @param model model
     */
    @RequestMapping(value = "/subscriptionInvoiceReferenceUpdateEvent", method = RequestMethod.POST)
    public void testSubscriptionInvoiceReferenceUpdateEvent(@ModelAttribute UserReferenceForm userReferenceForm, Model model) {
        UserReferenceForm userRefForm = createUserReferenceForm(userReferenceForm);
        String xml = userRefForm.toUpdateEventXML(UserReferenceForm.EventType.UPDATE,UserReferenceUpdateSubType.CHANGE_EINVOICE_REF);
        LOG.info("called testSubscriptionInvoiceReferenceUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        InvoiceReferenceUpdateEvent consumedInvoiceReferenceUpdateEvent = (InvoiceReferenceUpdateEvent) getJms().receive(USER_REFERENCE_INVOICE_CHANGE_TOPIC, correlationId);
        model.addAttribute("result", consumedInvoiceReferenceUpdateEvent);
        LOG.info("received event = " + consumedInvoiceReferenceUpdateEvent);
    }

    private UserReferenceForm createUserReferenceForm(UserReferenceForm userReferenceForm) {
        UserReferenceForm userRefForm = getUserRefForm();
        userRefForm.setSubscriptionId(userReferenceForm.getSubscriptionId());
        userRefForm.seteInvoiceRef(userReferenceForm.geteInvoiceRef());
        userRefForm.setNumberType("ES");
        return userRefForm;
    }
}
