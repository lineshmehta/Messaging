package com.telenor.cos.messaging.web.controller.userresource;

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
import com.telenor.cos.messaging.event.userresource.UserResourceCsUserIdUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.UserResourceForm;

public class UserResourceCsUserIdUpdateEventControllerTest extends UserResourceCommonControllerTest {

    private static final Long RESOURCE_ID = 456L;

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private UserResourceCsUserIdUpdateEventController controller;
    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new UserResourceCsUserIdUpdateEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testUserResourceCsUserIdUpdateEventForm(model);
        assertTrue("Model did not contain attribute userResourceForm", model.containsAttribute("userResourceForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:userResourceCsUserIdUpdateEventForm'", "redirect:userResourceCsUserIdUpdateEventForm", controller.testUserResourceCsUserIdUpdateEvent());
    }

    @Test
    public void testPost() {
        UserResourceForm userResourceForm = createUserResourceForm();
        String correlationId = "corrId31415";
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedEvent = new UserResourceCsUserIdUpdateEvent(createUserResource(RESOURCE_ID),"cosmaster");
        when(jmsTemplate.receive(UserResourceCommonController.CSUSERID_UPDATE_TOPIC, correlationId)).thenReturn(consumedEvent);
        controller.testUserResourceCsUserIdUpdateEvent(userResourceForm, model);
        ControllerAssertUtil.checkControllerOutput(RESOURCE_ID, ACTION.CS_USERID_CHANGE, model);
    }
}
