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
import com.telenor.cos.messaging.event.mastercustomer.MasterCustomerNewEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.MasterCustomerForm;

/**
 * Test case for {@link MasterCustomerNewEventController}
 * @author Babaprakash D
 *
 */
public class MasterCustomerNewEventControllerTest {

    private static final Long MASTER_CUSTOMER_ID = 112233L;

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private MasterCustomerNewEventController controller;
    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new MasterCustomerNewEventController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testNewMasterCustomerEventForm(model);
        assertTrue("Model did not contain attribute masterCustomerForm",model.containsAttribute("masterCustomerForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:masterCustomerNewEventForm'", "redirect:masterCustomerNewEventForm",controller.testMasterCustomerNewEvent());
    }

    @Test
    public void testPost() {
        MasterCustomerForm masterCustomerForm = createMasterCustomerForm();
        String correlationId = "corrId31415";
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedMasterCustomerEvent = new MasterCustomerNewEvent(createMasterCustomer());
        when(jmsTemplate.receive(MasterCustomerCommonController.NEW_MASTERCUSTOMERS, correlationId)).thenReturn(consumedMasterCustomerEvent);
        controller.testMasterCustomerNewEvent(masterCustomerForm, model);
        ControllerAssertUtil.checkControllerOutput(MASTER_CUSTOMER_ID, ACTION.CREATED, model);
    }

    private MasterCustomerForm createMasterCustomerForm() {
        MasterCustomerForm masterCustomerForm = new MasterCustomerForm();
        masterCustomerForm.setCustFirstName("Anna");
        masterCustomerForm.setCustLastName("Duck");
        masterCustomerForm.setMasterId(112233L);
        return masterCustomerForm;
    }

    private MasterCustomer createMasterCustomer() {
        MasterCustomer masterCustomer = new MasterCustomer();
        masterCustomer.setFirstName("Anna");
        masterCustomer.setLastName("Duck");
        masterCustomer.setMasterId(MASTER_CUSTOMER_ID);
        return masterCustomer;
    }
}
