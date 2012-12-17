package com.telenor.cos.messaging.web.controller;

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
import com.telenor.cos.messaging.event.subscription.SubscriptionLogicalDeleteEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.controller.subscription.SubscriptionCommonController;
import com.telenor.cos.messaging.web.controller.subscription.SubscriptionLogicalDeleteEventController;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.test.suite.UnitTests;

/**
 * @author Per Jørgen Walstrøm
 */
@Category(UnitTests.class)
public class SubscriptionLogicalDeleteEventControllerTest {
    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private SubscriptionLogicalDeleteEventController controller;
    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new SubscriptionLogicalDeleteEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testSubscriptionLogicalDeleteEventForm(model);
        assertTrue("Model did not contain attribute commonForm", model.containsAttribute("subscriptionForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:subscriptionLogicalDeleteEventForm'",
                "redirect:subscriptionLogicalDeleteEventForm", controller.testSubscriptionLogicalDeleteEvent());
    }

    @Test
    public void testPost() {
        SubscriptionForm subscriptionForm = new SubscriptionForm();
        Long subscrId = 112233L;
        String correlationId = "corrId32164";
        subscriptionForm.setSubscrId(subscrId);
        subscriptionForm.setInfoIsDeleted(true);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedSubscriptionEvent = new SubscriptionLogicalDeleteEvent(subscrId);
        when(jmsTemplate.receive(SubscriptionCommonController.SUBSCRIPTION_LOGICAL_DELETE_TOPIC, correlationId)).thenReturn(
                consumedSubscriptionEvent);
        controller.testSubscriptionLogicalDeleteEvent(subscriptionForm, model);
        ControllerAssertUtil.checkControllerOutput(subscrId, ACTION.LOGICAL_DELETE, model);
    }
}
