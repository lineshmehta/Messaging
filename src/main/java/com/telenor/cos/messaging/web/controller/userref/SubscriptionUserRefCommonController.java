package com.telenor.cos.messaging.web.controller.userref;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.UserReferenceForm;

public class SubscriptionUserRefCommonController extends CommonController {

    public final static String USER_REFERENCE_NEW_TOPIC = "Consumer.WTEST.VirtualTopic.UserReference.New";
    public final static String USER_REFERENCE_INVOICE_CHANGE_TOPIC = "Consumer.WTEST.VirtualTopic.UserReference.InvoiceChange";
    public final static String USER_REFERENCE_DESCR_CHANGE_TOPIC = "Consumer.WTEST.VirtualTopic.UserReference.DescrChange";
    public final static String USER_REFERENCE_LOGICAL_DELETE_TOPIC = "Consumer.WTEST.VirtualTopic.UserReference.LogicalDeleted";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(USER_REFERENCE_NEW_TOPIC);
            getJms().receive(USER_REFERENCE_INVOICE_CHANGE_TOPIC);
            getJms().receive(USER_REFERENCE_DESCR_CHANGE_TOPIC);
            getJms().receive(USER_REFERENCE_LOGICAL_DELETE_TOPIC);
            getJms().setReceiveTimeout(6000L);
        }
    }

    /**
     * Creates UserReferenceForm with Default Values.
     * @return UserReferenceForm
     */
    protected UserReferenceForm getUserRefForm() {
        return new UserReferenceForm();
    }
}
