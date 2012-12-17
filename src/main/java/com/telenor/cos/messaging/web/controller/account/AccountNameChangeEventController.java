package com.telenor.cos.messaging.web.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.account.AccountNameChangeEvent;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.messaging.web.form.AccountForm.AccountUpdateSubType;

/**
 * Controller for {@link AccountNameChangeEvent}
 * @author Babaprakash D
 *
 */
@Controller
public class AccountNameChangeEventController extends AccountCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountNameChangeEventController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/accountNameChangeEvent", method = RequestMethod.GET)
    public String testAccountNameChangeEvent() {
        LOG.info("called testAccountNameChangeEvent() for GET");
        return "redirect:accountNameChangeEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/accountNameChangeEventForm", method = RequestMethod.GET)
    public void testAccountNameChangeEventForm(Model model) {
        LOG.info("called testAccountNameChangeEventForm()");
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
    @RequestMapping(value = "/accountNameChangeEvent", method = RequestMethod.POST)
    public void testAccountNameChangeEvent(@ModelAttribute AccountForm accountForm, Model model) {
        AccountForm accForm = createAccountNameChangeForm(accountForm);
        String xml = accForm.toUpdateEventXML(AccountForm.EventType.UPDATE,AccountUpdateSubType.NAME_CHANGE);
        LOG.info("called testAccountNameChangeEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        AccountNameChangeEvent consumedAccountNameChangeEvent = (AccountNameChangeEvent) getJms().receive(ACCOUNT_NAME_CHANGE, correlationId);
        model.addAttribute("result", consumedAccountNameChangeEvent);
        LOG.info("received event = " + consumedAccountNameChangeEvent);
    }

    private AccountForm createAccountNameChangeForm(AccountForm accountForm) {
        AccountForm accountUpdateCommonForm = createAccountUpdateCommonForm(accountForm);
        accountUpdateCommonForm.setAccountName(accountForm.getAccountName());
        return accountUpdateCommonForm;
    }
}
