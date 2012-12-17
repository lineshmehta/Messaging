package com.telenor.cos.messaging.web.controller.agreementmember;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.AgreementMemberForm;

public class AgreementMemberCommonController extends CommonController{

    public final static String AGREEMENT_MEMBER_NEW = "Consumer.WTEST.VirtualTopic.AgreementMember.New";
    public final static String LOGICAL_DELETED_AGREEMENT_MEMBER = "Consumer.WTEST.VirtualTopic.AgreementMember.LogicalDelete";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(AGREEMENT_MEMBER_NEW);
            getJms().receive(LOGICAL_DELETED_AGREEMENT_MEMBER);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * Return a new CsUSerForm
     *
     * @return a CsUSerForm
     */
    public AgreementMemberForm getAgreementMemberForm() {
        return new AgreementMemberForm();
    }
}
