package com.telenor.cos.messaging.web.controller.masterstructure;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.MasterStructureForm;

/**
 * @author t798435
 */
public class MasterStructureCommonController extends CommonController{

    public final static String NEW_MASTER_STRUCTURE = "Consumer.WTEST.VirtualTopic.MasterStructure.New";
    public final static String UPDATED_MASTER_STRUCTURE = "Consumer.WTEST.VirtualTopic.MasterStructure.Updated";
    public final static String DELETED_MASTER_STRUCTURE = "Consumer.WTEST.VirtualTopic.MasterStructure.LogicalDeleted";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(NEW_MASTER_STRUCTURE);
            getJms().receive(UPDATED_MASTER_STRUCTURE);
            getJms().receive(DELETED_MASTER_STRUCTURE);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * @return a form with default values
     */
    protected MasterStructureForm getMasterStructureForm() {
        return new MasterStructureForm();
    }
}