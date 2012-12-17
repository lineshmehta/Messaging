package com.telenor.cos.messaging.web.controller.mastercustomer;


import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.MasterCustomerForm;

public class MasterCustomerCommonController extends CommonController{

    public final static String NEW_MASTERCUSTOMERS     = "Consumer.WTEST.VirtualTopic.MasterCustomer.New";
    public final static String DELETED_MASTERCUSTOMERS = "Consumer.WTEST.VirtualTopic.MasterCustomer.LogicalDeleted";
    public final static String MASTERCUSTOMER_NAME_CHANGE = "Consumer.WTEST.VirtualTopic.MasterCustomer.NameChange";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(NEW_MASTERCUSTOMERS);
            getJms().receive(MASTERCUSTOMER_NAME_CHANGE);
            getJms().receive(DELETED_MASTERCUSTOMERS);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * @return a form with default values
     */
    protected MasterCustomerForm getMasterCustomerForm() {
        return new MasterCustomerForm();
    }

}
