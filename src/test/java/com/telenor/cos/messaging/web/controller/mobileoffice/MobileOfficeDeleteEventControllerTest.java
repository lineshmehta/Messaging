package com.telenor.cos.messaging.web.controller.mobileoffice;

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
import com.telenor.cos.messaging.event.mobileoffice.MobileOfficeDeleteEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.MobileOfficeForm;


public class MobileOfficeDeleteEventControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private MobileOfficeDeleteEventController controller;
    private Model model;
    private static final String DIRECTORY_NUMBER = "91234567";
    private static final String EXTENSION_NUMBER_OLD = "54321";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new MobileOfficeDeleteEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testMobileOfficeDeleteEventForm(model);
        assertTrue("Model did not contain attribute mobileOfficeForm",model.containsAttribute("mobileOfficeForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:mobileOfficeDeleteEventForm'", "redirect:mobileOfficeDeleteEventForm",
                controller.testMobileOfficeDeleteEvent());
    }

    @Test
    public void testPost() {
        MobileOfficeForm mobileOfficeForm = new MobileOfficeForm();
        String correlationId = "corrId75310";
        mobileOfficeForm.setDirectoryNumber(DIRECTORY_NUMBER);
        mobileOfficeForm.setExtensionNumberOld(EXTENSION_NUMBER_OLD);
        mobileOfficeForm.setInfoIsDeleted(true);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedMobileOfficeDeleteEvent = new MobileOfficeDeleteEvent(DIRECTORY_NUMBER, EXTENSION_NUMBER_OLD);
        when(jmsTemplate.receive(MobileOfficeCommonController.DELETE_MOBILE_OFFICE, correlationId)).thenReturn(consumedMobileOfficeDeleteEvent);
        controller.testMobileOfficeDeleteEvent(mobileOfficeForm, model);
        MobileOfficeDeleteEvent addedEvent = (MobileOfficeDeleteEvent) ControllerAssertUtil.checkControllerOutput(null, ACTION.DELETE,
                model);
        assertEquals(DIRECTORY_NUMBER, addedEvent.getDirectoryNumber());
        assertEquals(EXTENSION_NUMBER_OLD, addedEvent.getOldExtensionNumber());
    }
}
