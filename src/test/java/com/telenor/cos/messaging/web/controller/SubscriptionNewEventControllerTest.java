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
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.controller.subscription.SubscriptionCommonController;
import com.telenor.cos.messaging.web.controller.subscription.SubscriptionNewEventController;
import com.telenor.cos.messaging.web.form.RelSubscriptionForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.messaging.web.form.UserReferenceForm;
import com.telenor.cos.test.suite.UnitTests;

/**
 * @author Per Jørgen Walstrøm
 */
@Category(UnitTests.class)
public class SubscriptionNewEventControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private SubscriptionNewEventController controller;
    private Model model;
    private String correlationId = "corrId32164";
    private Long subscrId = 112233L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new SubscriptionNewEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testSubscriptionNewEventForm(model);
        assertTrue("Model did not contain attribute commonForm", model.containsAttribute("subscriptionForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:subscriptionNewEventForm'", "redirect:subscriptionNewEventForm", controller.testSubscriptionNewEvent());
    }

    @Test
    public void testNewSubscription() throws InterruptedException {
        SubscriptionForm subscriptionForm = createSubscriptionForm();
        subscriptionForm.getUserReferenceForm().setUserRefDescr("Test");
        subscriptionForm.getUserReferenceForm().setNumberType("TM");
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedSubscriptionEvent = new NewSubscriptionEvent(subscrId, null);
        when(jmsTemplate.receive(SubscriptionCommonController.SUBSCRIPTION_NEW_TOPIC,correlationId)).thenReturn(consumedSubscriptionEvent);
        controller.testSubscriptionNewEvent(subscriptionForm, model);
        ControllerAssertUtil.checkControllerOutput(subscrId, ACTION.CREATED, model);
    }

    private SubscriptionForm createSubscriptionForm() {
        SubscriptionForm subscriptionNewEventForm = new SubscriptionForm();
        RelSubscriptionForm relSubscriptionForm = new RelSubscriptionForm();
        UserReferenceForm userReferenceForm = new UserReferenceForm();
        subscriptionNewEventForm.setSubscrId(subscrId);
        subscriptionNewEventForm.setRelSubscriptionForm(relSubscriptionForm);
        subscriptionNewEventForm.setUserReferenceForm(userReferenceForm);
        return subscriptionNewEventForm;
    }
}
