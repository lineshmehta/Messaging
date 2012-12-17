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
import com.telenor.cos.messaging.event.account.AccountTypeUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.test.suite.UnitTests;

@Category(UnitTests.class)
public class AccountTypeChangeControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private AccountTypeChangeController controller;
    private Model model;
    private static final Long ACC_ID = Long.valueOf(112233);
    private static final String TYPE = "NO";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new AccountTypeChangeController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testAccountTypeUpdateEventForm(model);
        assertTrue("Model did not contain attribute accountForm",model.containsAttribute("accountForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:accountTypeUpdateEventForm'", "redirect:accountTypeUpdateEventForm",controller.testAccountTypeUpdateEvent());
    }

    @Test
    public void testPost() {
        AccountForm accountTypeChangeEventForm = new AccountForm();
        String correlationId = "corrId32164";
        accountTypeChangeEventForm.setAccId(ACC_ID);
        accountTypeChangeEventForm.setAccTypeId(TYPE);
        accountTypeChangeEventForm.setOldAccountTypeId("ON");
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedAccountEvent = new AccountTypeUpdateEvent(ACC_ID,TYPE);
        when(jmsTemplate.receive(AccountCommonController.ACCOUNT_TYPE_CHANGE, correlationId)).thenReturn(consumedAccountEvent);
        controller.testAccountTypeUpdateEvent(accountTypeChangeEventForm, model);
        AccountTypeUpdateEvent addedEvent = (AccountTypeUpdateEvent) ControllerAssertUtil.checkControllerOutput(ACC_ID,
                ACTION.TYPE_CHANGE, model);
        assertEquals(TYPE, addedEvent.getAccountType());
    }
}
