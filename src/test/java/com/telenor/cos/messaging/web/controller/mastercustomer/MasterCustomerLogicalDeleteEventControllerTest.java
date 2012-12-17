package com.telenor.cos.messaging.web.controller.mastercustomer;

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
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerLogicalDeleteEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.MasterCustomerForm;

/**
 * @author Vidar Sonerud
 */
public class MasterCustomerLogicalDeleteEventControllerTest {
    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;
    private MasterCustomerLogicalDeleteEventController controller;
    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new MasterCustomerLogicalDeleteEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testMasterCustomerLogicalDeleteEventForm(model);
        assertTrue("Model did not contain attribute masterCustomerForm",
                model.containsAttribute("masterCustomerForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:masterCustomerLogicalDeleteEventForm'", "redirect:masterCustomerLogicalDeleteEventForm",
                controller.testMasterCustomerLogicalDeleteEvent());
    }

    @Test
    public void testPost() {
        MasterCustomerForm masterCustomerForm = new MasterCustomerForm();
        Long masterId = 112233L;
        String correlationId = "corrId31415";
        masterCustomerForm.setMasterId(masterId);
        masterCustomerForm.setInfoIsDeleted(true);
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedMasterCustomerEvent = new MasterCustomerLogicalDeleteEvent(masterId);
        when(jmsTemplate.receive(MasterCustomerCommonController.DELETED_MASTERCUSTOMERS, correlationId)).thenReturn(consumedMasterCustomerEvent);
        controller.testMasterCustomerLogicalDeleteEvent(masterCustomerForm, model);
        ControllerAssertUtil.checkControllerOutput(masterId, ACTION.LOGICAL_DELETE, model);
    }
}
