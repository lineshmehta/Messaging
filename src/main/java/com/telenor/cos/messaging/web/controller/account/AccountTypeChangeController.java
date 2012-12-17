package com.telenor.cos.messaging.web.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.account.AccountTypeUpdateEvent;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.messaging.web.form.AccountForm.AccountUpdateSubType;

/**
 * WebApp controller for {@link AccountTypeUpdateEvent}
 * @author Babaprakash D
 *
 */
@Controller
public class AccountTypeChangeController extends AccountCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountTypeChangeController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/accountTypeUpdateEvent", method = RequestMethod.GET)
    public String testAccountTypeUpdateEvent() {
        LOG.info("called testAccountTypeUpdateEvent() for GET");
        return "redirect:accountTypeUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/accountTypeUpdateEventForm", method = RequestMethod.GET)
    public void testAccountTypeUpdateEventForm(Model model) {
        LOG.info("called testAccountTypeUpdateEventForm()");
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
    @RequestMapping(value = "/accountTypeUpdateEvent", method = RequestMethod.POST)
    public void testAccountTypeUpdateEvent(@ModelAttribute AccountForm accountForm, Model model) {
        AccountForm accForm = createAccountTypeUpdateForm(accountForm);
        String xml = accForm.toUpdateEventXML(AccountForm.EventType.UPDATE,AccountUpdateSubType.TYPE_CHANGE);
        LOG.info("called testAccountTypeUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        AccountTypeUpdateEvent consumedAccountTypeUpdateEvent = (AccountTypeUpdateEvent) getJms().receive(ACCOUNT_TYPE_CHANGE, correlationId);
        model.addAttribute("result", consumedAccountTypeUpdateEvent);
        LOG.info("received event = " + consumedAccountTypeUpdateEvent);
    }

    private AccountForm createAccountTypeUpdateForm(AccountForm accountForm) {
        AccountForm accountUpdateCommonForm = createAccountUpdateCommonForm(accountForm);
        accountUpdateCommonForm.setAccTypeId(accountForm.getAccTypeId());
        return accountUpdateCommonForm;
    }
}
