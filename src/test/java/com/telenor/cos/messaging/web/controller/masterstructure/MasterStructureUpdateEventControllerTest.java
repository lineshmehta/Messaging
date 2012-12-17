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
import com.telenor.cos.messaging.event.masterstructure.MasterStructureUpdateEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.MasterStructureForm;

/**
 * Test case for {@link MasterStructureUpdateEventController}
 * @author t798435
 *
 */
public class MasterStructureUpdateEventControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private static final Long MASTER_ID = 666L;
    private static final Long OWNER_ID = 777L;

    private MasterStructureUpdateEventController controller;

    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new MasterStructureUpdateEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testMasterStructureUpdateEventForm(model);
        assertTrue("Model did not contain attribute masterStructureForm", model.containsAttribute("masterStructureForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:masterStructureUpdateEventForm'", "redirect:masterStructureUpdateEventForm", controller.testMasterStructureUpdateEvent());
    }

    @Test
    public void testPost() {
        MasterStructureForm masterStructureForm = createMasterStructureForm();
        String correlationId = "corrId31415";
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedMasterStructureEvent = new MasterStructureUpdateEvent(MASTER_ID, createMasterStructure());
        when(jmsTemplate.receive(MasterStructureCommonController.UPDATED_MASTER_STRUCTURE, correlationId)).thenReturn(consumedMasterStructureEvent);
        controller.testMasterStructureUpdateEvent(masterStructureForm, model);
        ControllerAssertUtil.checkControllerOutput(MASTER_ID, ACTION.UPDATED, model);
    }

    private MasterStructureForm createMasterStructureForm() {
        MasterStructureForm masterStructureForm = new MasterStructureForm();
        masterStructureForm.setMastIdMember(MASTER_ID);
        masterStructureForm.setMastIdOwner(OWNER_ID);
        masterStructureForm.setInfoIsDeleted(false);
        return masterStructureForm;
    }

    private MasterStructure createMasterStructure() {
        MasterStructure masterStructure = new MasterStructure();
        masterStructure.setMasterIdOwner(MASTER_ID);
        masterStructure.setMasterId(OWNER_ID);
        return masterStructure;
    }
}