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
import com.telenor.cos.messaging.event.account.AccountInvoiceFormatUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.test.suite.UnitTests;

@Category(UnitTests.class)
public class AccountInvoiceFormatChangeControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private AccountInvoiceFormatChangeController controller;
    private Model model;
    private static final Long ACC_ID = Long.valueOf(112233);
    private static final String ACC_INV_MEDIUM = "AP";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new AccountInvoiceFormatChangeController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testInvoiceFormatUpdateEventForm(model);
        assertTrue("Model did not contain attribute accountForm",model.containsAttribute("accountForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:accountInvoiceFormatUpdateEventForm'", "redirect:accountInvoiceFormatUpdateEventForm",controller.testAccountInvoiceFormatUpdateEvent());
    }

    @Test
    public void testPost() {
        AccountForm accountInvoiceFormatUpdateEventForm = new AccountForm();
        String correlationId = "corrId32164";
        accountInvoiceFormatUpdateEventForm.setAccId(ACC_ID);
        accountInvoiceFormatUpdateEventForm.setAccInvMedium(ACC_INV_MEDIUM);
        accountInvoiceFormatUpdateEventForm.setOldAccInvMedium("PA");
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedAccountEvent = new AccountInvoiceFormatUpdateEvent(ACC_ID,ACC_INV_MEDIUM);
        when(jmsTemplate.receive(AccountCommonController.ACCOUNT_INVOICE_FORMAT_CHANGE, correlationId)).thenReturn(consumedAccountEvent);
        controller.testAccountInvoiceFormatUpdateEvent(accountInvoiceFormatUpdateEventForm, model);
        AccountInvoiceFormatUpdateEvent addedEvent = (AccountInvoiceFormatUpdateEvent) ControllerAssertUtil.checkControllerOutput(ACC_ID,
                ACTION.INVOICE_FORMAT_CHANGE, model);
        assertEquals(ACC_INV_MEDIUM, addedEvent.getInvoiceFormat());
    }
}
