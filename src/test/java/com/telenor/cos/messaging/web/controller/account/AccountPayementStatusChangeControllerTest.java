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
import com.telenor.cos.messaging.event.account.AccountPaymentStatusUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.test.suite.UnitTests;

@Category(UnitTests.class)
public class AccountPayementStatusChangeControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private AccountPayementStatusChangeController controller;
    private Model model;
    private static final Long ACC_ID = Long.valueOf(112233);
    private static final String ACC_STATUS = "AP";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new AccountPayementStatusChangeController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testAccountPayementStatusChangeEventForm(model);
        assertTrue("Model did not contain attribute accountForm",model.containsAttribute("accountForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:accountPaymentStatusUpdateEventForm'", "redirect:accountPaymentStatusUpdateEventForm",controller.testAccountPayementStatusChangeEvent());
    }

    @Test
    public void testPost() {
        AccountForm accountPaymentStatusChangeEventForm = new AccountForm();
        String correlationId = "corrId32164";
        accountPaymentStatusChangeEventForm.setAccId(ACC_ID);
        accountPaymentStatusChangeEventForm.setAccStatusId(ACC_STATUS);
        accountPaymentStatusChangeEventForm.setOldAccountStatusId("PA");
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedAccountEvent = new AccountPaymentStatusUpdateEvent(ACC_ID,ACC_STATUS);
        when(jmsTemplate.receive(AccountCommonController.ACCOUNT_PAYMENTSTATUS_CHANGE, correlationId)).thenReturn(consumedAccountEvent);
        controller.testAccountPayementStatusChangeEvent(accountPaymentStatusChangeEventForm, model);
        AccountPaymentStatusUpdateEvent addedEvent = (AccountPaymentStatusUpdateEvent) ControllerAssertUtil.checkControllerOutput(ACC_ID,
                ACTION.PAYMENT_STATUS_CHANGE, model);
        assertEquals(ACC_STATUS, addedEvent.getPaymentStatus());
    }
}
