package com.telenor.cos.messaging.web.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.account.AccountPaymentStatusUpdateEvent;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.messaging.web.form.AccountForm.AccountUpdateSubType;

/**
 * Web App Controller for {@link AccountPaymentStatusUpdateEvent} 
 * @author Babaprakash D
 *
 */
@Controller
public class AccountPayementStatusChangeController extends AccountCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountPayementStatusChangeController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/accountPaymentStatusUpdateEvent", method = RequestMethod.GET)
    public String testAccountPayementStatusChangeEvent() {
        LOG.info("called testAccountPayementStatusChangeEvent() for GET");
        return "redirect:accountPaymentStatusUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/accountPaymentStatusUpdateEventForm", method = RequestMethod.GET)
    public void testAccountPayementStatusChangeEventForm(Model model) {
        LOG.info("called testAccountPayementStatusChangeEventForm()");
        AccountForm form = getAccountForm();
        model.addAttribute(form);
    }

    /**
     * The HTTP POST handler. Posting XML to the queue, receiving result and sets the result as an attribute to the
     * model.
     *
     * @param accountForm form
     * @param model model
     */
    @RequestMapping(value = "/accountPaymentStatusUpdateEvent", method = RequestMethod.POST)
    public void testAccountPayementStatusChangeEvent(@ModelAttribute AccountForm accountForm, Model model) {
        AccountForm accForm = createAccountPayementStatusChangeForm(accountForm);
        String xml = accForm.toUpdateEventXML(AccountForm.EventType.UPDATE,AccountUpdateSubType.PAYMENT_STATUS_CHANGE);
        LOG.info("called testAccountPayementStatusChangeEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        AccountPaymentStatusUpdateEvent consumedAccountPaymentStatusChangeEvent = (AccountPaymentStatusUpdateEvent) getJms().receive(ACCOUNT_PAYMENTSTATUS_CHANGE, correlationId);
        model.addAttribute("result", consumedAccountPaymentStatusChangeEvent);
        LOG.info("received event = " + consumedAccountPaymentStatusChangeEvent);
    }

    private AccountForm createAccountPayementStatusChangeForm(AccountForm accountForm) {
        AccountForm accountUpdateCommonForm = createAccountUpdateCommonForm(accountForm);
        accountUpdateCommonForm.setAccStatusId(accountForm.getAccStatusId());
        return accountUpdateCommonForm;
    }
}
