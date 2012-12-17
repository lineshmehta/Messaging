package com.telenor.cos.messaging.web.controller.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.telenor.cos.messaging.event.account.AccountNewEvent;
import com.telenor.cos.messaging.web.form.AccountForm;

/**
 * Controller for the Account New Event.
 * @author Babaprakash D
 */
@Controller
public class AccountNewEventController extends AccountCommonController {
    
    private static final Logger LOG = LoggerFactory.getLogger(AccountNewEventController.class);
    
    /**
     * The plain HTTP GET mapping, just forwarding the GET including the model
     *
     * @return a forward
     */
    @RequestMapping(value = "/accountNewEvent", method = RequestMethod.GET)
    public String testAccountNewEvent() {
        LOG.info("called testAccountNewEvent() for GET");
        return "redirect:accountNewEventForm";
    }

    /**
     * The HTTP GET handling the model, populating the form with default values.
     *
     * @param model model
     */
    @RequestMapping(value = "/accountNewEventForm", method = RequestMethod.GET)
    public void testAccountNewEventForm(Model model) {
        LOG.info("called testAccountNewEventForm()");
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
    @RequestMapping(value = "/accountNewEvent", method = RequestMethod.POST)
    public void testAccountNewEvent(@ModelAttribute AccountForm accountForm, Model model) {
        AccountForm accForm = createNewAccountForm(accountForm);
        String xml = accForm.toNewEventXML(AccountForm.EventType.INSERT);
        LOG.info("called testAccountNewEvent() for POST. xml = " + xml);
        setUp();
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        AccountNewEvent consumedNewAccountEvent = (AccountNewEvent) getJms().receive(ACCOUNT_NEW, correlationId);
        model.addAttribute("result", consumedNewAccountEvent);
        LOG.info("received event = " + consumedNewAccountEvent);
    }
    
    private AccountForm createNewAccountForm(AccountForm accountForm) {
        AccountForm form = getAccountForm();
        form.setAccId(accountForm.getAccId());
        form.setCustIdResp(accountForm.getCustIdResp());
        form.setCustIdPayer(accountForm.getCustIdPayer());
        form.setAccountName(accountForm.getAccountName());
        form.setAccStatusId(accountForm.getAccStatusId());
        form.setAccTypeId(accountForm.getAccTypeId());
        form.setAccStatusId2(accountForm.getAccStatusId2());
        form.setAccInvMedium(accountForm.getAccInvMedium());
        return form;
    }
}