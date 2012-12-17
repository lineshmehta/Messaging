package com.telenor.cos.messaging.web.controller.agreementowner;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.AgreementOwnerForm;

public class AgreementOwnerCommonController extends CommonController {

    public final static String AGREEMENT_OWNER_NEW = "Consumer.WTEST.VirtualTopic.AgreementOwner.New";
    public final static String AGREEMENT_OWNER_DELETE = "Consumer.WTEST.VirtualTopic.AgreementOwner.Delete";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(AGREEMENT_OWNER_NEW);
            getJms().receive(AGREEMENT_OWNER_DELETE);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * Return a new AgreementOwnerForm
     * 
     * @return a AgreementOwnerForm
     */
    public AgreementOwnerForm getAgreementOwnerForm() {
        return new AgreementOwnerForm();
    }

}
