package com.telenor.cos.messaging.web.controller.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import com.telenor.cos.messaging.CosCorrelationJmsTemplate;
import com.telenor.cos.messaging.event.Account;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountNewEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.test.suite.UnitTests;

@Category(UnitTests.class)
public class AccountNewEventControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private AccountNewEventController controller;
    private Model model;
    private static final Long ACC_ID = Long.valueOf(112233);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new AccountNewEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testAccountNewEventForm(model);
        assertTrue("Model did not contain attribute accountForm",model.containsAttribute("accountForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:accountNewEventForm'", "redirect:accountNewEventForm",controller.testAccountNewEvent());
    }

    @Test
    public void testPost() {
        AccountForm accountNewEventForm = new AccountForm();
        String correlationId = "corrId32164";
        accountNewEventForm.setAccId(ACC_ID);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Account account = new Account();
        account.setAccountId(ACC_ID);
        Event consumedAccountEvent = new AccountNewEvent(account);
        when(jmsTemplate.receive(AccountCommonController.ACCOUNT_NEW, correlationId)).thenReturn(consumedAccountEvent);
        controller.testAccountNewEvent(accountNewEventForm, model);
        ControllerAssertUtil.checkControllerOutput(ACC_ID, ACTION.CREATED, model);
    }
}
