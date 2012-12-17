package com.telenor.cos.messaging.web.controller.usermapping;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.TnuUserIdMappingForm;

/**
 * Common WebApp Controller for UserMapping Events.
 * @author Babaprakash D
 *
 */
public class TnuUserIdMappingCommonController extends CommonController {

    public final static String NEW_TNUID_USERMAPPING = "Consumer.WTEST.VirtualTopic.TnuIdUserMapping.New";
    public final static String DELETE_TNUID_USERMAPPING = "Consumer.WTEST.VirtualTopic.TnuIdUserMapping.Delete";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(NEW_TNUID_USERMAPPING);
            getJms().receive(DELETE_TNUID_USERMAPPING);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * @return TnuUserIdMappingForm
     */
    protected TnuUserIdMappingForm getUserMappingForm() {
        return new TnuUserIdMappingForm();
    }
}
