package com.telenor.cos.messaging.web.controller.resource;

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
import com.telenor.cos.messaging.event.resource.ResourceLogicalDeleteEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.ResourceForm;


/**
 * Test case for {@link ResourceLogicalDeleteEventController}
 * @author Babaprakash D
 *
 */
public class ResourceLogicalDeleteEventControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private ResourceLogicalDeleteEventController controller;
    private Model model;
    private static final Long RESOURCE_ID = Long.valueOf(112233);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new ResourceLogicalDeleteEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testResourceLogicalDeleteEventForm(model);
        assertTrue("Model did not contain attribute resourceForm",model.containsAttribute("resourceForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:resourceLogicalDeleteEventForm'", "redirect:resourceLogicalDeleteEventForm",controller.testResourceLogicalDeleteEvent());
    }

    @Test
    public void testPost() {
        ResourceForm resourceLogicalDeleteEventForm = new ResourceForm();
        String correlationId = "corrId32164";
        resourceLogicalDeleteEventForm.setResourceId(RESOURCE_ID);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedResourceEvent = new ResourceLogicalDeleteEvent(RESOURCE_ID);
        when(jmsTemplate.receive(ResourceCommonController.RESOURCE_LOGICAL_DELETE_TOPIC, correlationId)).thenReturn(consumedResourceEvent);
        controller.testResourceLogicalDeleteEvent(resourceLogicalDeleteEventForm, model);
        ControllerAssertUtil.checkControllerOutput(RESOURCE_ID, ACTION.LOGICAL_DELETE, model);
    }
}
