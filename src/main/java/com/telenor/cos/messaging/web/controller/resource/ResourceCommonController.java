package com.telenor.cos.messaging.web.controller.resource;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.ResourceForm;

/**
 * WebApp Common Controller for ResourceEvents.
 * @author Babaprakash D
 *
 */
public class ResourceCommonController extends CommonController {

    public final static String RESOURCE_NEW_TOPIC = "Consumer.WTEST.VirtualTopic.Resource.New";
    public final static String RESOURCE_LOGICAL_DELETE_TOPIC = "Consumer.WTEST.VirtualTopic.Resource.LogicalDeleted";
    public final static String RESOURCE_TYPE_ID_UPDATE_TOPIC = "Consumer.WTEST.VirtualTopic.Resource.TypeIdChange";
    public final static String RESOURCE_TYPE_ID_KEY_UPDATE_TOPIC = "Consumer.WTEST.VirtualTopic.Resource.TypeIdKeyChange";
    public final static String RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC = "Consumer.WTEST.VirtualTopic.Resource.ContentInheritChange";
    public final static String RESOURCE_STRUCTURE_INHERIT_UPDATE_TOPIC = "Consumer.WTEST.VirtualTopic.Resource.StructureInheritChange";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(RESOURCE_NEW_TOPIC);
            getJms().receive(RESOURCE_LOGICAL_DELETE_TOPIC);
            getJms().receive(RESOURCE_TYPE_ID_UPDATE_TOPIC);
            getJms().receive(RESOURCE_TYPE_ID_KEY_UPDATE_TOPIC);
            getJms().receive(RESOURCE_CONTENT_INHERIT_UPDATE_TOPIC);
            getJms().receive(RESOURCE_STRUCTURE_INHERIT_UPDATE_TOPIC);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * Creates ResourceForm with default values.
     * @return ResourceForm.
     */
    protected ResourceForm getResourceForm() {
        return new ResourceForm();
    }
    
    /**
     * Creates ResourceForm with Updated values.
     * @param form ResourceForm.
     * @return ResourceForm.
     */
    protected ResourceForm createUpdateResourceForm(ResourceForm form) {
        ResourceForm resourceForm = getResourceForm();
        resourceForm.setResourceId(form.getResourceId());
        resourceForm.setOldResourceTypeId(form.getOldResourceTypeId());
        resourceForm.setOldResourceTypeIdKey(form.getOldResourceTypeIdKey());
        resourceForm.setOldResourceHasContentInherit(form.getOldResourceHasContentInherit());
        resourceForm.setOldResourceHasStructureInherit(form.getOldResourceHasStructureInherit());
        return resourceForm;
    }
}