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
import com.telenor.cos.messaging.event.resource.ResourceContentInheritUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.ResourceForm;

/**
 * Test case for {@link ResourceContentInheritUpdateEventController}
 *
 * @author Babaprakash D
 *
 */
public class ResourceContentInheritUpdateEventControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private ResourceContentInheritUpdateEventController controller;
    private Model model;
    private static final Long RESOURCE_ID = Long.valueOf(112233);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new ResourceContentInheritUpdateEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testResourceContentInheritUpdateEventForm(model);
        assertTrue("Model did not contain attribute resourceForm", model.containsAttribute("resourceForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:resourceContentInheritUpdateEventForm'",
                "redirect:resourceContentInheritUpdateEventForm", controller.testResourceContentInheritUpdateEvent());
    }

    @Test
    public void testPost() {
        ResourceForm resourceUpdateEventForm = new ResourceForm();
        String correlationId = "corrId32164";
        resourceUpdateEventForm.setResourceId(RESOURCE_ID);
        resourceUpdateEventForm.setResourceHasContentInherit(true);
        resourceUpdateEventForm.setOldResourceHasContentInherit(false);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Resource resource = new Resource(RESOURCE_ID);
        Event consumedResourceEvent = new ResourceContentInheritUpdateEvent(resource, new ArrayList<String>(), true);
        when(jmsTemplate.receive(ResourceCommonController.RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC, correlationId)).thenReturn(
                consumedResourceEvent);
        controller.testResourceContentInheritUpdateEvent(resourceUpdateEventForm, model);
        ResourceContentInheritUpdateEvent addedEvent = (ResourceContentInheritUpdateEvent) ControllerAssertUtil.checkControllerOutput(
                RESOURCE_ID, ACTION.CONTENT_INHERIT_UPDATE, model);
        assertTrue(addedEvent.getResourceHasContentInherit());
    }
}
