package com.telenor.cos.messaging.web.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.account.AccountLogicalDeleteEvent;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.messaging.web.form.AccountForm.AccountUpdateSubType;

/**
 * Controller for AccountLogical Delete Event.
 * @author Babaprakash D
 *
 */
@Controller
public class AccountLogicalDeleteController extends AccountCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountLogicalDeleteController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/accountLogicalDeleteEvent", method = RequestMethod.GET)
    public String testAccountLogicalDeleteEvent() {
        LOG.info("called testAccountLogicalDeleteEvent() for GET");
        return "redirect:accountLogicalDeleteEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/accountLogicalDeleteEventForm", method = RequestMethod.GET)
    public void testAccountLogicalDeleteEventForm(Model model) {
        LOG.info("called testAccountLogicalDeleteEventForm()");
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
    @RequestMapping(value = "/accountLogicalDeleteEvent", method = RequestMethod.POST)
    public void testAccountLogicalDeleteEvent(@ModelAttribute AccountForm accountForm, Model model) {
        AccountForm accForm = createLogicalDeleteAccountForm(accountForm);
        String xml = accForm.toUpdateEventXML(AccountForm.EventType.UPDATE,AccountUpdateSubType.LOGICAL_DELETE);
        LOG.info("called testAccountLogicalDeleteEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        AccountLogicalDeleteEvent consumedAccountLogicalDeleteEvent = (AccountLogicalDeleteEvent) getJms().receive(ACCOUNT_LOGICAL_DELETE, correlationId);
        model.addAttribute("result", consumedAccountLogicalDeleteEvent);
        LOG.info("received event = " + consumedAccountLogicalDeleteEvent);
    }

    private AccountForm createLogicalDeleteAccountForm(AccountForm accountForm) {
        AccountForm accountUpdateCommonForm = createAccountUpdateCommonForm(accountForm);
        accountUpdateCommonForm.setInfoIsDeleted(accountForm.isInfoIsDeleted());
        return accountUpdateCommonForm;
    }
}
