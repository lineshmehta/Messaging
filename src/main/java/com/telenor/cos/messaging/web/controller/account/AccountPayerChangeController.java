package com.telenor.cos.messaging.web.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.account.AccountPayerUpdateEvent;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.messaging.web.form.AccountForm.AccountUpdateSubType;

/**
 * WebApp controller for {@link AccountPayerUpdateEvent}
 * @author Babaprakash D
 *
 */
@Controller
public class AccountPayerChangeController extends AccountCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountPayerChangeController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/accountPayerUpdateEvent", method = RequestMethod.GET)
    public String testAccountPayerUpdateEvent() {
        LOG.info("called testAccountPayerUpdateEvent() for GET");
        return "redirect:accountPayerUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/accountPayerUpdateEventForm", method = RequestMethod.GET)
    public void testAccountPayerUpdateEventForm(Model model) {
        LOG.info("called testAccountPayerUpdateEventForm()");
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
    @RequestMapping(value = "/accountPayerUpdateEvent", method = RequestMethod.POST)
    public void testAccountPayerUpdateEvent(@ModelAttribute AccountForm accountForm, Model model) {
        AccountForm accForm = createAccountPayerUpdateForm(accountForm);
        String xml = accForm.toUpdateEventXML(AccountForm.EventType.UPDATE,AccountUpdateSubType.PAYER_CHANGE);
        LOG.info("called testAccountPayerUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        AccountPayerUpdateEvent consumedAccountPayerUpdateEvent = (AccountPayerUpdateEvent) getJms().receive(ACCOUNT_PAYER_CHANGE, correlationId);
        model.addAttribute("result", consumedAccountPayerUpdateEvent);
        LOG.info("received event = " + consumedAccountPayerUpdateEvent);
    }

    private AccountForm createAccountPayerUpdateForm(AccountForm accountForm) {
        AccountForm accountUpdateCommonForm = createAccountUpdateCommonForm(accountForm);
        accountUpdateCommonForm.setCustIdPayer(accountForm.getCustIdPayer());
        return accountUpdateCommonForm;
    }
}
