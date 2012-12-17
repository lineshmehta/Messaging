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
import com.telenor.cos.messaging.event.account.AccountOwnerUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.AccountForm;
import com.telenor.cos.test.suite.UnitTests;

@Category(UnitTests.class)
public class AccountOwnerChangeControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private AccountOwnerChangeController controller;
    private Model model;
    private static final Long ACC_ID = Long.valueOf(112233);
    private static final Long CUST_ID_OWNER = Long.valueOf(123456);
    private static final Long CUST_ID_OWNER_OLD = Long.valueOf(7890);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new AccountOwnerChangeController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testAccountOwnerUpdateEventForm(model);
        assertTrue("Model did not contain attribute accountForm",model.containsAttribute("accountForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:accountOwnerUpdateEventForm'", "redirect:accountOwnerUpdateEventForm",controller.testAccountOwnerUpdateEvent());
    }

    @Test
    public void testPost() {
        AccountForm accountOwnerChangeEventForm = new AccountForm();
        String correlationId = "corrId32164";
        accountOwnerChangeEventForm.setAccId(ACC_ID);
        accountOwnerChangeEventForm.setCustIdResp(CUST_ID_OWNER);
        accountOwnerChangeEventForm.setOldCustIdResp(CUST_ID_OWNER_OLD);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        CustomerName newOwnerCustomer = new CustomerName(CUST_ID_OWNER);
        CustomerAddress newOwnerCustomerAddress = new CustomerAddress(CUST_ID_OWNER);
        Event consumedAccountEvent = new AccountOwnerUpdateEvent(ACC_ID,newOwnerCustomer,newOwnerCustomerAddress);
        when(jmsTemplate.receive(AccountCommonController.ACCOUNT_OWNER_CHANGE, correlationId)).thenReturn(consumedAccountEvent);
        controller.testAccountOwnerUpdateEvent(accountOwnerChangeEventForm, model);
        AccountOwnerUpdateEvent addedEvent = (AccountOwnerUpdateEvent) ControllerAssertUtil.checkControllerOutput(ACC_ID,ACTION.OWNER_CHANGE, model);
        assertEquals(CUST_ID_OWNER, addedEvent.getNewOwnerCustomerName().getCustomerId());
    }
}
