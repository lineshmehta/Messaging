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
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountNameChangeEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.test.suite.UnitTests;

@Category(UnitTests.class)
public class AccountNameChangeEventControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private AccountNameChangeEventController controller;
    private Model model;
    private static final Long ACC_ID = Long.valueOf(112233);
    private static final String ACC_NAME = "AP";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new AccountNameChangeEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testAccountNameChangeEventForm(model);
        assertTrue("Model did not contain attribute accountForm",model.containsAttribute("accountForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:accountNameChangeEventForm'", "redirect:accountNameChangeEventForm",controller.testAccountNameChangeEvent());
    }

    @Test
    public void testPost() {
        AccountForm accountNameChangeEventForm = new AccountForm();
        String correlationId = "corrId32164";
        accountNameChangeEventForm.setAccId(ACC_ID);
        accountNameChangeEventForm.setAccountName(ACC_NAME);
        accountNameChangeEventForm.setOldAccountName("PA");
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedAccountEvent = new AccountNameChangeEvent(ACC_ID,ACC_NAME);
        when(jmsTemplate.receive(AccountCommonController.ACCOUNT_NAME_CHANGE, correlationId)).thenReturn(consumedAccountEvent);
        controller.testAccountNameChangeEvent(accountNameChangeEventForm, model);
        AccountNameChangeEvent addedEvent = (AccountNameChangeEvent) ControllerAssertUtil.checkControllerOutput(ACC_ID,
                ACTION.NAME_CHANGE, model);
        assertEquals(ACC_NAME, addedEvent.getName());
    }
}
