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
import com.telenor.cos.messaging.event.Resource;
import com.telenor.cos.messaging.event.resource.ResourceNewEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.ResourceForm;

/**
 * Test case for {@link ResourceNewEventController}
 * @author Babaprakash D
 *
 */
public class ResourceNewEventControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private ResourceNewEventController controller;
    private Model model;
    private static final Long RESOURCE_ID = Long.valueOf(112233);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new ResourceNewEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testNewResourceEventForm(model);
        assertTrue("Model did not contain attribute resourceForm",model.containsAttribute("resourceForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:resourceNewEventForm'", "redirect:resourceNewEventForm",controller.testResourceNewEvent());
    }

    @Test
    public void testPost() {
        ResourceForm resourceNewEventForm = new ResourceForm();
        String correlationId = "corrId32164";
        resourceNewEventForm.setResourceId(RESOURCE_ID);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Resource resource = new Resource(RESOURCE_ID);
        Event consumedResourceEvent = new ResourceNewEvent(resource);
        when(jmsTemplate.receive(ResourceCommonController.RESOURCE_NEW_TOPIC, correlationId)).thenReturn(consumedResourceEvent);
        controller.testResourceNewEvent(resourceNewEventForm, model);
        ControllerAssertUtil.checkControllerOutput(RESOURCE_ID, ACTION.CREATED, model);
    }
}
