package com.telenor.cos.messaging.web.controller.usermapping;

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
import com.telenor.cos.messaging.event.TnuIdUserMapping;
import com.telenor.cos.messaging.event.usermapping.TnuIdUserMappingNewEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.TnuUserIdMappingForm;

/**
 *
 * Testcase for {@link TnuUserIdMappingNewController}
 * @author Babaprakash D
 *
 */
public class TnuUserIdMappingNewControllerTest {

    @Mock
    private CosCorrelationJmsTemplate jmsTemplate;

    private TnuUserIdMappingNewController controller;
    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        controller = new TnuUserIdMappingNewController();
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplate);
    }

    @Test
    public void findForm() {
        controller.testTnuIdUserMappingNewEventForm(model);
        assertTrue("Model did not contain attribute TnuUserIdMappingForm",model.containsAttribute("tnuUserIdMappingForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Redirect should be 'redirect:tnuIdUserMappingNewEventForm'", "redirect:tnuIdUserMappingNewEventForm",controller.testTnuIdUserMappingNewEvent());
    }

    @Test
    public void testPost() {
        TnuUserIdMappingForm tnuIdUserMappingForm = createUserMappingForm();
        String correlationId = "corrId31415";
        when(jmsTemplate.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        Event consumedEvent = new TnuIdUserMappingNewEvent(createUserMapping());
        when(jmsTemplate.receive(TnuUserIdMappingCommonController.NEW_TNUID_USERMAPPING, correlationId)).thenReturn(consumedEvent);
        controller.testTnuIdUserMappingNewEvent(tnuIdUserMappingForm, model);
        ControllerAssertUtil.checkControllerOutput(null, ACTION.CREATED, model);
    }

    private TnuUserIdMappingForm createUserMappingForm() {
        TnuUserIdMappingForm tnuIdUserMapping = new TnuUserIdMappingForm();
        tnuIdUserMapping.setTnuId("_1000030");
        tnuIdUserMapping.setCsUserId("M0_100469591");
        tnuIdUserMapping.setApplicationId(62);
        return tnuIdUserMapping;
    }

    private TnuIdUserMapping createUserMapping() {
        TnuIdUserMapping tnuIdUserMapping = new TnuIdUserMapping();
        tnuIdUserMapping.setTelenorUserId("_1000030");
        tnuIdUserMapping.setCosSecurityUserId("M0_100469591");
        tnuIdUserMapping.setApplicationId(62);
        return tnuIdUserMapping;
    }
}
