package com.telenor.cos.messaging.web.controller.userref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import com.telenor.cos.messaging.CosCorrelationJmsTemplate;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.userref.InvoiceReferenceUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.UserReferenceForm;

public class SubscriptionInvoiceRefUpdateControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private SubscriptionInvoiceRefUpdateController controller;
    private Model model;
    private static final Long SUBSCRIPTION_ID = Long.valueOf(112233);
    private static final String NEW_INVOICE_REF = "Test1";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new SubscriptionInvoiceRefUpdateController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testSubscriptionInvoiceReferenceUpdateEventForm(model);
        assertTrue("Model did not contain attribute userReferenceForm",model.containsAttribute("userReferenceForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:subscriptionInvoiceReferenceUpdateEventForm'", "redirect:subscriptionInvoiceReferenceUpdateEventForm",controller.testSubscriptionInvoiceReferenceUpdateEvent());
    }

    @Test
    public void testPost() {
        UserReferenceForm userReferenceForm = new UserReferenceForm();
        String correlationId = "corrId32164";
        userReferenceForm.setSubscriptionId(SUBSCRIPTION_ID);
        userReferenceForm.setOldEInvoiceRef("Test");
        userReferenceForm.seteInvoiceRef(NEW_INVOICE_REF);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedInvoiceRefUpdateEvent = new InvoiceReferenceUpdateEvent(SUBSCRIPTION_ID,NEW_INVOICE_REF);
        when(jmsTemplate.receive(SubscriptionUserRefCommonController.USER_REFERENCE_INVOICE_CHANGE_TOPIC, correlationId)).thenReturn(consumedInvoiceRefUpdateEvent);
        controller.testSubscriptionInvoiceReferenceUpdateEvent(userReferenceForm, model);
        InvoiceReferenceUpdateEvent addedEvent = (InvoiceReferenceUpdateEvent) ControllerAssertUtil.checkControllerOutput(
                SUBSCRIPTION_ID, ACTION.INVOICE_CHANGE, model);
        assertEquals(NEW_INVOICE_REF, addedEvent.getInvoiceRef());
    }
}
