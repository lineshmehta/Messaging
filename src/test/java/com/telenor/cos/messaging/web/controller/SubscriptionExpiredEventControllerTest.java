package com.telenor.cos.messaging.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;

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
import com.telenor.cos.messaging.event.subscription.SubscriptionExpiredEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.controller.subscription.SubscriptionCommonController;
import com.telenor.cos.messaging.web.controller.subscription.SubscriptionExpiredEventController;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.test.suite.UnitTests;

/**
 * @author Per Jørgen Walstrøm
 */
@Category(UnitTests.class)
public class SubscriptionExpiredEventControllerTest {
    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private SubscriptionExpiredEventController controller;
    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new SubscriptionExpiredEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testSubscriptionExpiredEventForm(model);
        assertTrue("Model did not contain attribute commonForm", model.containsAttribute("subscriptionForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:subscriptionExpiredEventForm'", "redirect:subscriptionExpiredEventForm",
                controller.testSubscriptionExpiredEvent());
    }

    @Test
    public void testPost() {
        SubscriptionForm commonForm = new SubscriptionForm();
        Long subscrId = 112233L;
        Date validToDate = new Date();
        String correlationId = "corrId32164";
        commonForm.setSubscrId(subscrId);
        commonForm.setSubscrValidToDate(validToDate);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedSubscriptionEvent = new SubscriptionExpiredEvent(subscrId, validToDate);
        when(jmsTemplate.receive(SubscriptionCommonController.SUBSCRIPTION_EXPIRED_TOPIC, correlationId)).thenReturn(
                consumedSubscriptionEvent);
        controller.testSubscriptionExpiredEvent(commonForm, model);
        SubscriptionExpiredEvent addedEvent = (SubscriptionExpiredEvent) ControllerAssertUtil.checkControllerOutput(subscrId,
                ACTION.EXPIRED, model);
        assertEquals(validToDate, addedEvent.getValidToDate());
    }
}
