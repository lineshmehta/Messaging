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
import com.telenor.cos.messaging.event.userref.UserReferenceLogicalDeleteEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.UserReferenceForm;

public class SubscriptionUserRefLogicalDeleteControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private SubscriptionUserRefLogicalDeleteController controller;
    private Model model;
    private static final Long SUBSCRIPTION_ID = Long.valueOf(112233);
    private static final String NUMBER_TYPE = "ES";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new SubscriptionUserRefLogicalDeleteController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testSubscriptionUserRefLogicalDeleteEventForm(model);
        assertTrue("Model did not contain attribute userReferenceForm", model.containsAttribute("userReferenceForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:subscriptionUserRefLogicalDeleteEventForm'",
                "redirect:subscriptionUserRefLogicalDeleteEventForm", controller.testSubscriptionUserRefLogicalDeleteEvent());
    }

    @Test
    public void testPost() {
        UserReferenceForm userReferenceForm = new UserReferenceForm();
        String correlationId = "corrId32164";
        userReferenceForm.setSubscriptionId(SUBSCRIPTION_ID);
        userReferenceForm.setInfoIsDeleted(true);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedSubscriptionUserRefLogicalDeleteEvent = new UserReferenceLogicalDeleteEvent(SUBSCRIPTION_ID, NUMBER_TYPE);
        when(jmsTemplate.receive(SubscriptionUserRefCommonController.USER_REFERENCE_LOGICAL_DELETE_TOPIC, correlationId))
                .thenReturn(consumedSubscriptionUserRefLogicalDeleteEvent);
        controller.testSubscriptionUserRefLogicalDeleteEvent(userReferenceForm, model);
        UserReferenceLogicalDeleteEvent addedEvent = (UserReferenceLogicalDeleteEvent) ControllerAssertUtil.checkControllerOutput(
                SUBSCRIPTION_ID, ACTION.LOGICAL_DELETE, model);
        assertEquals(NUMBER_TYPE, addedEvent.getNumberType());
    }
}
