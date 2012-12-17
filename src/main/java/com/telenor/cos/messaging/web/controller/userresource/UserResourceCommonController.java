package com.telenor.cos.messaging.web.controller.userresource;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.UserResourceForm;

/**
 * CommonController for UserResourceEvents.
 * @author Babaprakash D
 *
 */
public class UserResourceCommonController extends CommonController {
    
    public final static String NEW_USER_RESOURCE_TOPIC = "Consumer.WTEST.VirtualTopic.UserResource.New";
    public final static String DELETE_USER_RESOURCE_TOPIC = "Consumer.WTEST.VirtualTopic.UserResource.Delete";
    public final static String RESOURCEID_UPDATE_TOPIC = "Consumer.WTEST.VirtualTopic.UserResource.ResourceIdUpdate";
    public final static String CSUSERID_UPDATE_TOPIC = "Consumer.WTEST.VirtualTopic.UserResource.CsUserIdUpdate";

    private boolean queuesSetUp = false;
    
    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(NEW_USER_RESOURCE_TOPIC);
            getJms().receive(RESOURCEID_UPDATE_TOPIC);
            getJms().receive(CSUSERID_UPDATE_TOPIC);
            getJms().receive(DELETE_USER_RESOURCE_TOPIC);
            getJms().setReceiveTimeout(3000L);
        }
    }
    
    /**
     * @return UserResourceForm.
     */
    protected UserResourceForm getUserResourceForm() {
        return new UserResourceForm();
    }
}
