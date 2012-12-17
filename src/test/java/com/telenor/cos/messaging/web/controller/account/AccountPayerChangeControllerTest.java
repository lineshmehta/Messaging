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
import com.telenor.cos.messaging.event.CustomerAddress;
import com.telenor.cos.messaging.event.CustomerName;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountPayerUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.test.suite.UnitTests;

@Category(UnitTests.class)
public class AccountPayerChangeControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private AccountPayerChangeController controller;
    private Model model;
    private static final Long ACC_ID = Long.valueOf(112233);
    private static final Long CUST_ID_PAYER = Long.valueOf(123456);
    private static final Long CUST_ID_PAYER_OLD = Long.valueOf(7890);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new AccountPayerChangeController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testAccountPayerUpdateEventForm(model);
        assertTrue("Model did not contain attribute accountForm",model.containsAttribute("accountForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:accountPayerUpdateEventForm'", "redirect:accountPayerUpdateEventForm",controller.testAccountPayerUpdateEvent());
    }

    @Test
    public void testPost() {
        AccountForm accountPayerChangeEventForm = new AccountForm();
        String correlationId = "corrId32164";
        accountPayerChangeEventForm.setAccId(ACC_ID);
        accountPayerChangeEventForm.setCustIdPayer(CUST_ID_PAYER);
        accountPayerChangeEventForm.setOldCustIdPayer(CUST_ID_PAYER_OLD);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        CustomerName newPayerCustomer = new CustomerName(CUST_ID_PAYER);
        CustomerAddress newPayerCustomerAddress = new CustomerAddress(CUST_ID_PAYER);
        Event consumedAccountEvent = new AccountPayerUpdateEvent(ACC_ID,newPayerCustomer,newPayerCustomerAddress);
        when(jmsTemplate.receive(AccountCommonController.ACCOUNT_PAYER_CHANGE, correlationId)).thenReturn(consumedAccountEvent);
        controller.testAccountPayerUpdateEvent(accountPayerChangeEventForm, model);
        AccountPayerUpdateEvent addedEvent = (AccountPayerUpdateEvent) ControllerAssertUtil.checkControllerOutput(ACC_ID,ACTION.PAYER_CHANGE, model);
        assertEquals(CUST_ID_PAYER, addedEvent.getNewPayerCustomerName().getCustomerId());
    }
}
