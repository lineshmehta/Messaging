package com.telenor.cos.messaging.web.controller.userref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.telenor.cos.messaging.event.UserReference;
import com.telenor.cos.messaging.event.userref.UserReferenceNewEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.UserReferenceForm;

public class SubscriptionUserReferenceNewEventControllerTest {
    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private SubscriptionUserReferenceNewEventController controller;
    private Model model;
    private static final Long SUBSCRIPTION_ID = Long.valueOf(112233);
    private static final String INVOICE_REF = "Test1";
    private static final String NUMBER_TYPE = "ES";
    private static final String USERREF_DESCR = "Test1";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new SubscriptionUserReferenceNewEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testSubscriptionUserReferenceNewEventForm(model);
        assertTrue("Model did not contain attribute userReferenceForm",model.containsAttribute("userReferenceForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:subscriptionUserReferenceNewEventForm'", "redirect:subscriptionUserReferenceNewEventForm",controller.testSubscriptionUserReferenceNewEvent());
    }

    @Test
    public void testPost() {
        UserReferenceForm userReferenceForm = new UserReferenceForm();
        String correlationId = "corrId32164";
        userReferenceForm.setSubscriptionId(SUBSCRIPTION_ID);
        userReferenceForm.seteInvoiceRef(INVOICE_REF);
        userReferenceForm.setNumberType(NUMBER_TYPE);
        userReferenceForm.setUserRefDescr(USERREF_DESCR);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        UserReference userReference = new UserReference();
        userReference.setInvoiceRef(INVOICE_REF);
        userReference.setNumberType(NUMBER_TYPE);
        userReference.setUserRefDescr(USERREF_DESCR);
        Event consumedUserRefNewEvent = new UserReferenceNewEvent(SUBSCRIPTION_ID,userReference);
        when(jmsTemplate.receive(SubscriptionUserRefCommonController.USER_REFERENCE_NEW_TOPIC, correlationId)).thenReturn(consumedUserRefNewEvent);
        controller.testSubscriptionUserReferenceNewEvent(userReferenceForm, model);
        UserReferenceNewEvent addedEvent = (UserReferenceNewEvent) ControllerAssertUtil.checkControllerOutput(SUBSCRIPTION_ID,
                ACTION.CREATED, model);
        assertNotNull(addedEvent.getUserReference());
    }
}
