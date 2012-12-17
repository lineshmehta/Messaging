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
import com.telenor.cos.messaging.event.MasterCustomer;
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNameChangeEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.MasterCustomerForm;

/**
 * Test case for {@link MasterCustomerNameChangeEventController}
 */
public class MasterCustomerNameChangeEventControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private MasterCustomerNameChangeEventController controller;
    private Model model;
    private static final Long MASTER_ID = Long.valueOf(1234);
    private static final String OLD = "OLD ";
    private static final String FIRST_NAME = "FIRST NAME";
    private static final String MIDDLE_NAME = "MIDDLE NAME";
    private static final String LAST_NAME = "LAST NAME";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new MasterCustomerNameChangeEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testMasterCustomerNameChangeEvent(model);
        assertTrue("Model did not contain attribute masterCustomerForm",model.containsAttribute("masterCustomerForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:masterCustomerNameChangeEventForm'", "redirect:masterCustomerNameChangeEventForm",controller.testMasterCustomerNameChangeEvent());
    }

    @Test
    public void testPost() {
        MasterCustomerForm masterCustomerForm = new MasterCustomerForm();
        masterCustomerForm.setMasterId(MASTER_ID);

        masterCustomerForm.setOldFirstName(OLD + FIRST_NAME);
        masterCustomerForm.setOldMiddleName(OLD + MIDDLE_NAME);
        masterCustomerForm.setOldLastName(OLD + LAST_NAME);

        masterCustomerForm.setCustFirstName(FIRST_NAME);
        masterCustomerForm.setCustMiddleName(MIDDLE_NAME);
        masterCustomerForm.setCustLastName(LAST_NAME);

        MasterCustomer masterCustomerName = new MasterCustomer();

        String correlationId = "corrId31415";
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedMasterCustomerEvent = new MasterCustomerNameChangeEvent(MASTER_ID, masterCustomerName);
        when(jmsTemplate.receive(MasterCustomerCommonController.MASTERCUSTOMER_NAME_CHANGE, correlationId)).thenReturn(consumedMasterCustomerEvent);
        controller.testMasterCustomerNameChangeEvent(masterCustomerForm, model);
        MasterCustomerNameChangeEvent addedEvent = (MasterCustomerNameChangeEvent) ControllerAssertUtil.checkControllerOutput(MASTER_ID,
                ACTION.NAME_CHANGE, model);
        assertEquals(masterCustomerName, addedEvent.getMasterCustomerName());
    }
}
