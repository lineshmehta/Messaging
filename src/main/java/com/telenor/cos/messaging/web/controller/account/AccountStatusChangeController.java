package com.telenor.cos.messaging.web.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.account.AccountStatusUpdateEvent;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.messaging.web.form.AccountForm.AccountUpdateSubType;

/**
 * WebApp Controller for {@link AccountStatusUpdateEvent}
 * @author Babaprakash D
 *
 */
@Controller
public class AccountStatusChangeController extends AccountCommonController {
    
    private static final Logger LOG = LoggerFactory.getLogger(AccountStatusChangeController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/accountStatusUpdateEvent", method = RequestMethod.GET)
    public String testAccountStatusUpdateEvent() {
        LOG.info("called testAccountStatusUpdateEvent() for GET");
        return "redirect:accountStatusUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/accountStatusUpdateEventForm", method = RequestMethod.GET)
    public void testAccountStatusUpdateEventForm(Model model) {
        LOG.info("called testAccountStatusUpdateEventForm()");
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
    @RequestMapping(value = "/accountStatusUpdateEvent", method = RequestMethod.POST)
    public void testAccountStatusUpdateEvent(@ModelAttribute AccountForm accountForm, Model model) {
        AccountForm accForm = createAccountStatusUpdateForm(accountForm);
        String xml = accForm.toUpdateEventXML(AccountForm.EventType.UPDATE,AccountUpdateSubType.STATUS_UPDATE);
        LOG.info("called testAccountStatusUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        AccountStatusUpdateEvent consumedAccountStatusUpdateEvent = (AccountStatusUpdateEvent) getJms().receive(ACCOUNT_STATUS_CHANGE, correlationId);
        model.addAttribute("result", consumedAccountStatusUpdateEvent);
        LOG.info("received event = " + consumedAccountStatusUpdateEvent);
    }

    private AccountForm createAccountStatusUpdateForm(AccountForm accountForm) {
        AccountForm accountUpdateCommonForm = createAccountUpdateCommonForm(accountForm);
        accountUpdateCommonForm.setAccStatusId2(accountForm.getAccStatusId2());
        return accountUpdateCommonForm;
    }
}
