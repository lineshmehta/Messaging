package com.telenor.cos.messaging.web.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.account.AccountInvoiceFormatUpdateEvent;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.messaging.web.form.AccountForm.AccountUpdateSubType;

/**
 * Controller for AccountInvoiceFormatUpdate Event.
 * @author Babaprakash D
 *
 */
@Controller
public class AccountInvoiceFormatChangeController extends AccountCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountInvoiceFormatChangeController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/accountInvoiceFormatUpdateEvent", method = RequestMethod.GET)
    public String testAccountInvoiceFormatUpdateEvent() {
        LOG.info("called testInvoiceFormatUpdateEventForm() for GET");
        return "redirect:accountInvoiceFormatUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/accountInvoiceFormatUpdateEventForm", method = RequestMethod.GET)
    public void testInvoiceFormatUpdateEventForm(Model model) {
        LOG.info("called testInvoiceFormatUpdateEventForm()");
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
    @RequestMapping(value = "/accountInvoiceFormatUpdateEvent", method = RequestMethod.POST)
    public void testAccountInvoiceFormatUpdateEvent(@ModelAttribute AccountForm accountForm, Model model) {
        AccountForm accForm = createInvoiceFormatUpdateAccountForm(accountForm);
        String xml = accForm.toUpdateEventXML(AccountForm.EventType.UPDATE,AccountUpdateSubType.INVOICE_FORMAT_CHANGE);
        LOG.info("called testAccountInvoiceFormatUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        AccountInvoiceFormatUpdateEvent consumedAccountInvoiceFormatUpdateEvent = (AccountInvoiceFormatUpdateEvent) getJms().receive(ACCOUNT_INVOICE_FORMAT_CHANGE, correlationId);
        model.addAttribute("result", consumedAccountInvoiceFormatUpdateEvent);
        LOG.info("received event = " + consumedAccountInvoiceFormatUpdateEvent);
    }

    private AccountForm createInvoiceFormatUpdateAccountForm(AccountForm accountForm) {
        AccountForm accountUpdateCommonForm = createAccountUpdateCommonForm(accountForm);
        accountUpdateCommonForm.setAccInvMedium(accountForm.getAccInvMedium());
        return accountUpdateCommonForm;
    }
}
