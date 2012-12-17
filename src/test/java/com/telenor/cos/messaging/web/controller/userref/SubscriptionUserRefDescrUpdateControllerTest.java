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
import com.telenor.cos.messaging.event.userref.UserReferenceDescriptionUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.UserReferenceForm;

public class SubscriptionUserRefDescrUpdateControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private SubscriptionUserRefDescrUpdateController controller;
    private Model model;
    private static final Long SUBSCRIPTION_ID = Long.valueOf(112233);
    private static final String NEW_USERREF_DESCR = "Test1";
    private static final String NUMBER_TYPE = "ES";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new SubscriptionUserRefDescrUpdateController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testSubscriptionUserReferenceDescrUpdateEventForm(model);
        assertTrue("Model did not contain attribute userReferenceForm",model.containsAttribute("userReferenceForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:subscriptionUserReferenceDescrUpdateEventForm'", "redirect:subscriptionUserReferenceDescrUpdateEventForm",controller.testSubscriptionUserReferenceDescrUpdateEvent());
    }

    @Test
    public void testPost() {
        UserReferenceForm userReferenceForm = new UserReferenceForm();
        String correlationId = "corrId32164";
        userReferenceForm.setSubscriptionId(SUBSCRIPTION_ID);
        userReferenceForm.setUserRefDescr(NEW_USERREF_DESCR);
        userReferenceForm.setNumberType(NUMBER_TYPE);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedUserReferenceDescriptionUpdateEvent = new UserReferenceDescriptionUpdateEvent(SUBSCRIPTION_ID,NEW_USERREF_DESCR,NUMBER_TYPE);
        when(jmsTemplate.receive(SubscriptionUserRefCommonController.USER_REFERENCE_DESCR_CHANGE_TOPIC, correlationId)).thenReturn(consumedUserReferenceDescriptionUpdateEvent);
        controller.testSubscriptionUserReferenceDescrUpdateEvent(userReferenceForm, model);
        UserReferenceDescriptionUpdateEvent addedEvent = (UserReferenceDescriptionUpdateEvent) ControllerAssertUtil
                .checkControllerOutput(SUBSCRIPTION_ID, ACTION.USERREF_DESC_CHG, model);
        assertEquals(NEW_USERREF_DESCR, addedEvent.getUserRefDescr());
        assertEquals(NUMBER_TYPE, addedEvent.getNumberType());
    }
}
