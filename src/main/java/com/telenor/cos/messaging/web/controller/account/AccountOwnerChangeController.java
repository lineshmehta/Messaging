package com.telenor.cos.messaging.web.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.account.AccountOwnerUpdateEvent;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.messaging.web.form.AccountForm.AccountUpdateSubType;

/**
 * WebApp controller for {@link AccountOwnerUpdateEvent}
 * @author Babaprakash D
 *
 */
@Controller
public class AccountOwnerChangeController extends AccountCommonController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountOwnerChangeController.class);

    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/accountOwnerUpdateEvent", method = RequestMethod.GET)
    public String testAccountOwnerUpdateEvent() {
        LOG.info("called testAccountOwnerUpdateEvent() for GET");
        return "redirect:accountOwnerUpdateEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/accountOwnerUpdateEventForm", method = RequestMethod.GET)
    public void testAccountOwnerUpdateEventForm(Model model) {
        LOG.info("called testAccountOwnerUpdateEventForm()");
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
    @RequestMapping(value = "/accountOwnerUpdateEvent", method = RequestMethod.POST)
    public void testAccountOwnerUpdateEvent(@ModelAttribute AccountForm accountForm, Model model) {
        AccountForm accForm = createAccountOwnerUpdateForm(accountForm);
        String xml = accForm.toUpdateEventXML(AccountForm.EventType.UPDATE,AccountUpdateSubType.OWNER_CHANGE);
        LOG.info("called testAccountOwnerUpdateEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        AccountOwnerUpdateEvent consumedAccountOwnerUpdateEvent = (AccountOwnerUpdateEvent) getJms().receive(ACCOUNT_OWNER_CHANGE, correlationId);
        model.addAttribute("result", consumedAccountOwnerUpdateEvent);
        LOG.info("received event = " + consumedAccountOwnerUpdateEvent);
    }

    private AccountForm createAccountOwnerUpdateForm(AccountForm accountForm) {
        AccountForm accountUpdateCommonForm = createAccountUpdateCommonForm(accountForm);
        accountUpdateCommonForm.setCustIdResp(accountForm.getCustIdResp());
        return accountUpdateCommonForm;
    }
}
