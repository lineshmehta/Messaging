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
import com.telenor.cos.messaging.event.account.AccountLogicalDeleteEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.test.suite.UnitTests;

@Category(UnitTests.class)
public class AccountLogicalDeleteControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private AccountLogicalDeleteController controller;
    private Model model;
    private static final Long ACC_ID = Long.valueOf(112233);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new AccountLogicalDeleteController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testAccountLogicalDeleteEventForm(model);
        assertTrue("Model did not contain attribute accountForm",model.containsAttribute("accountForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:accountLogicalDeleteEventForm'", "redirect:accountLogicalDeleteEventForm",controller.testAccountLogicalDeleteEvent());
    }

    @Test
    public void testPost() {
        AccountForm accountLogicalDeleteEventForm = new AccountForm();
        String correlationId = "corrId32164";
        accountLogicalDeleteEventForm.setAccId(ACC_ID);
        accountLogicalDeleteEventForm.setInfoIsDeleted(true);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedAccountEvent = new AccountLogicalDeleteEvent(ACC_ID);
        when(jmsTemplate.receive(AccountCommonController.ACCOUNT_LOGICAL_DELETE, correlationId)).thenReturn(consumedAccountEvent);
        controller.testAccountLogicalDeleteEvent(accountLogicalDeleteEventForm, model);
        ControllerAssertUtil.checkControllerOutput(ACC_ID, ACTION.LOGICAL_DELETE, model);
    }
}
