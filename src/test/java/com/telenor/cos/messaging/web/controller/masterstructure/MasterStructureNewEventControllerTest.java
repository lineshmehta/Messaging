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
import com.telenor.cos.messaging.event.MasterStructure;
import com.telenor.cos.messaging.event.masterstructure.MasterStructureNewEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.MasterStructureForm;

/**
 * Test case for {@link MasterStructureNewEventController}
 * @author t798435
 *
 */
public class MasterStructureNewEventControllerTest {

    private static final Long MAST_ID_MEMBER = 112233L;
    private static final Long MAST_ID_OWNER = 331122L;
    private static final Long MASTER_ID = 112233L;

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private MasterStructureNewEventController controller;
    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new MasterStructureNewEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testNewMasterStructureEventForm(model);
        assertTrue("Model did not contain attribute masterStructureForm",model.containsAttribute("masterStructureForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:masterStructureNewEventForm'", "redirect:masterStructureNewEventForm",controller.testMasterStructureNewEvent());
    }

    @Test
    public void testPost() {
        MasterStructureForm masterStructureForm = createMasterStructureForm();
        String correlationId = "corrId31415";
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedMasterStructureEvent = new MasterStructureNewEvent(createMasterStructure());
        when(jmsTemplate.receive(MasterStructureCommonController.NEW_MASTER_STRUCTURE, correlationId)).thenReturn(consumedMasterStructureEvent);
        controller.testMasterStructureNewEvent(masterStructureForm, model);
        ControllerAssertUtil.checkControllerOutput(MASTER_ID, ACTION.CREATED, model);
    }

    private MasterStructureForm createMasterStructureForm() {
        MasterStructureForm masterStructureForm = new MasterStructureForm();
        masterStructureForm.setMastIdMember(MAST_ID_MEMBER);
        masterStructureForm.setMastIdOwner(MAST_ID_OWNER);
        masterStructureForm.setInfoIsDeleted(false);
        return masterStructureForm;
    }

    private MasterStructure createMasterStructure() {
        MasterStructure masterStructure = new MasterStructure();
        masterStructure.setMasterIdOwner(MAST_ID_OWNER);
        masterStructure.setMasterId(MASTER_ID);
        return masterStructure;
    }
}
