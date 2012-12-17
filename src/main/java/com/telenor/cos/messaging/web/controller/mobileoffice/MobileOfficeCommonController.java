package com.telenor.cos.messaging.web.controller.mobileoffice;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.MobileOfficeForm;

public class MobileOfficeCommonController extends CommonController {

    public final static String NEW_MOBILE_OFFICE= "Consumer.WTEST.VirtualTopic.MobileOffice.New";
    public final static String UPDATE_MOBILE_OFFICE= "Consumer.WTEST.VirtualTopic.MobileOffice.Update";
    public final static String DELETE_MOBILE_OFFICE= "Consumer.WTEST.VirtualTopic.MobileOffice.Delete";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(NEW_MOBILE_OFFICE);
            getJms().receive(UPDATE_MOBILE_OFFICE);
            getJms().receive(DELETE_MOBILE_OFFICE);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * Return a new CsUSerForm
     * 
     * @return a CsUSerForm
     */
    public MobileOfficeForm getMobileOfficeForm() {
        return new MobileOfficeForm();
    }
}
