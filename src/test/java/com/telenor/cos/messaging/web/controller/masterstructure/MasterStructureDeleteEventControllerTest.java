package com.telenor.cos.messaging.web.controller.masterstructure;

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
import com.telenor.cos.messaging.event.masterstructure.MasterStructureDeleteEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.MasterStructureForm;

/**
 * @author t798435
 */
public class MasterStructureDeleteEventControllerTest {
    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private MasterStructureDeleteEventController controller;
    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new MasterStructureDeleteEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testMasterStructureDeleteEventForm(model);
        assertTrue("Model did not contain attribute masterStructureForm",
                model.containsAttribute("masterStructureForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:masterStructureDeleteEventForm'", "redirect:masterStructureDeleteEventForm",
                controller.testMasterStructureDeleteEvent());
    }

    @Test
    public void testPost() {
        MasterStructureForm masterStructureForm = new MasterStructureForm();
        Long masterId = 112233L;
        String correlationId = "corrId31415";
        masterStructureForm.setMastIdMember(masterId);
        masterStructureForm.setInfoIsDeleted(true);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedMasterStructureEvent = new MasterStructureDeleteEvent(masterId);
        when(jmsTemplate.receive(MasterStructureCommonController.DELETED_MASTER_STRUCTURE, correlationId)).thenReturn(consumedMasterStructureEvent);
        controller.testMasterStructureDeleteEvent(masterStructureForm, model);
        ControllerAssertUtil.checkControllerOutput(masterId, ACTION.DELETE, model);
    }
}
