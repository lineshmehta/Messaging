package com.telenor.cos.messaging.web.controller.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

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
import com.telenor.cos.messaging.event.resource.ResourceTypeIdUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.ResourceForm;

/**
 * Test case for {@link ResourceTypeIdUpdateEventController}
 * @author Babaprakash D
 *
 */
public class ResourceTypeIdUpdateEventControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private ResourceTypeIdUpdateEventController controller;

    private Model model;

    private static final Long RESOURCE_ID = Long.valueOf(112233);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new ResourceTypeIdUpdateEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testResourceTypeIdUpdateEventForm(model);
        assertTrue("Model did not contain attribute resourceForm",model.containsAttribute("resourceForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:resourceTypeIdUpdateEventForm'", "redirect:resourceTypeIdUpdateEventForm",controller.testResourceTypeIdUpdateEvent());
    }

    @Test
    public void testPost() {
        ResourceForm resourceUpdateEventForm = new ResourceForm();
        String correlationId = "corrId32164";
        resourceUpdateEventForm.setResourceId(RESOURCE_ID);
        resourceUpdateEventForm.setResourceTypeId(1);
        resourceUpdateEventForm.setOldResourceTypeId(2);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Resource resource = new Resource(RESOURCE_ID);
        Event consumedResourceEvent = new ResourceTypeIdUpdateEvent(resource,new ArrayList<String>(),2);
        when(jmsTemplate.receive(ResourceCommonController.RESOURCE_TYPE_ID_UPDATE_TOPIC, correlationId)).thenReturn(consumedResourceEvent);
        controller.testResourceTypeIdUpdateEvent(resourceUpdateEventForm, model);
        ControllerAssertUtil.checkControllerOutput(RESOURCE_ID, ACTION.TYPE_ID_UPDATE, model);
    }
}
