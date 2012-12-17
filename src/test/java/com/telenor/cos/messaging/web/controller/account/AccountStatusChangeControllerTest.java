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
import com.telenor.cos.messaging.event.account.AccountStatusUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.test.suite.UnitTests;

@Category(UnitTests.class)
public class AccountStatusChangeControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private AccountStatusChangeController controller;
    private Model model;
    private static final Long ACC_ID = Long.valueOf(112233);
    private static final String STATUS = "NO";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new AccountStatusChangeController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testAccountStatusUpdateEventForm(model);
        assertTrue("Model did not contain attribute accountForm",model.containsAttribute("accountForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:accountStatusUpdateEventForm'", "redirect:accountStatusUpdateEventForm",controller.testAccountStatusUpdateEvent());
    }

    @Test
    public void testPost() {
        AccountForm accountStatusChangeEventForm = new AccountForm();
        String correlationId = "corrId32164";
        accountStatusChangeEventForm.setAccId(ACC_ID+1);
        accountStatusChangeEventForm.setAccStatusId2(STATUS);
        accountStatusChangeEventForm.setOldAccountStatusId2("ON");
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedAccountEvent = new AccountStatusUpdateEvent(ACC_ID,STATUS);
        when(jmsTemplate.receive(AccountCommonController.ACCOUNT_STATUS_CHANGE, correlationId)).thenReturn(consumedAccountEvent);
        controller.testAccountStatusUpdateEvent(accountStatusChangeEventForm, model);
        AccountStatusUpdateEvent addedEvent = (AccountStatusUpdateEvent) ControllerAssertUtil.checkControllerOutput(ACC_ID,
                ACTION.STATUS_UPDATE, model);
        assertEquals(STATUS, addedEvent.getStatus());
    }
}
