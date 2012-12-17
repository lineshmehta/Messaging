package com.telenor.cos.messaging.web.controller.agreementmember;

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
import com.telenor.cos.messaging.event.AgreementMember;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.agreementmember.AgreementMemberNewEvent;
import com.telenor.cos.messaging.web.ControllerAssertUtil;
import com.telenor.cos.messaging.web.form.AgreementMemberForm;

public class AgreementMemberNewEventControllerTest {

    private static final Long AGREEMENT_ID = 1L;

    private AgreementMemberNewEventController controller;
    private Model model;

    @Mock
    private CosCorrelationJmsTemplate jmsTemplateMock;


    @Before
    public void setUp() {
        model = new BindingAwareModelMap();
        controller = new AgreementMemberNewEventController();
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(controller, "queuesSetUp", Boolean.TRUE);
        ReflectionTestUtils.setField(controller, "jms", jmsTemplateMock);
    }

    @Test
    public void testFindForm() {
        controller.testAgreementMemberNewEventForm(model);
        assertTrue("Model did not contain attribute agreementMemberForm", model.containsAttribute("agreementMemberForm"));
    }

    @Test
    public void testRedirect() {
        assertEquals("Unexpected redirect", "redirect:agreementMemberNewEventForm", controller.testAgreementMemberNewEvent());
    }

    @Test
    public void testPost() {
        AgreementMemberForm agreementMemberForm = new AgreementMemberForm();
        agreementMemberForm.setAgreementMemberId(AGREEMENT_ID);
        agreementMemberForm.setInfoIsDeleted(false);
        String correlationId = "someId";
        when(jmsTemplateMock.send(any(String.class), any(Object.class))).thenReturn(correlationId);
        AgreementMember agreementMember = new AgreementMember();
        agreementMember.setAgreementMemberId(AGREEMENT_ID);
        AgreementMemberNewEvent consumedEvent = new AgreementMemberNewEvent(agreementMember);
        when(jmsTemplateMock.receive(AgreementMemberNewEventController.AGREEMENT_MEMBER_NEW, correlationId)).thenReturn(consumedEvent);
        controller.testAgreementMemberNewEventForm(agreementMemberForm, model);
        ControllerAssertUtil.checkControllerOutput(AGREEMENT_ID, ACTION.CREATED, model);
    }

}
