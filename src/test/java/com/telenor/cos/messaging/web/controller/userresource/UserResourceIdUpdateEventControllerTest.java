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
import com.telenor.cos.messaging.event.userresource.UserResourceResourceIdUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.UserResourceForm;

/**
 * Test case for {@link UserResourceIdUpdateEventController}
 */
public class UserResourceIdUpdateEventControllerTest extends UserResourceCommonControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private UserResourceIdUpdateEventController controller;
    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new UserResourceIdUpdateEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testUserResourceIdUpdateEventForm(model);
        assertTrue("Model did not contain attribute userResourceForm", model.containsAttribute("userResourceForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:userResourceIdUpdateEventForm'", "redirect:userResourceIdUpdateEventForm", controller.testUserResourceIdUpdateEvent());
    }

    @Test
    public void testPost() {
        UserResourceForm userResourceForm = createUserResourceForm();
        String correlationId = "corrId31415";
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedEvent = new UserResourceResourceIdUpdateEvent(createUserResource(456L),createUserResource(123L));
        when(jmsTemplate.receive(UserResourceCommonController.RESOURCEID_UPDATE_TOPIC, correlationId)).thenReturn(consumedEvent);
        controller.testUserResourceIdUpdateEvent(userResourceForm, model);
        ControllerAssertUtil.checkControllerOutput(null, ACTION.RESOURCE_ID_CHANGE, model);
    }
}
