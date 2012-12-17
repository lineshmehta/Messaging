package com.telenor.cos.messaging.web.controller.agreement;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.AgreementForm;

public class AgreementCommonController extends CommonController {

    public final static String NEW_AGREEMENT = "Consumer.WTEST.VirtualTopic.Agreement.New";
    public final static String LOGICAL_DELETED_AGREEMENT = "Consumer.WTEST.VirtualTopic.Agreement.LogicalDeleted";
    
    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(NEW_AGREEMENT);
            getJms().receive(LOGICAL_DELETED_AGREEMENT);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * Return a new CsUSerForm
     * 
     * @return a CsUSerForm
     */
    public AgreementForm getAgreementForm() {
        return new AgreementForm();
    }

}
