package com.telenor.cos.messaging.web.controller.subscriptionequipment;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.SubscriptionEquipmentForm;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.ui.Model;
import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;

import static com.telenor.cos.messaging.event.CosCorrelationIdFactory.createMessageSelectorExpression;

public class SubscriptionEquipmentCommonController extends CommonController {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionEquipmentCommonController.class);

    public final static String NEW_SUBSCRIPTION_EQUIPMENT= "Consumer.WTEST.VirtualTopic.SubscriptionEquipment.New";
    public final static String UPDATE_SUBSCRIPTION_EQUIPMENT= "Consumer.WTEST.VirtualTopic.SubscriptionEquipment.Update";
    public final static String DELETE_SUBSCRIPTION_EQUIPMENT= "Consumer.WTEST.VirtualTopic.SubscriptionEquipment.Delete";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(NEW_SUBSCRIPTION_EQUIPMENT);
            getJms().receive(UPDATE_SUBSCRIPTION_EQUIPMENT);
            getJms().receive(DELETE_SUBSCRIPTION_EQUIPMENT);
            getJms().setReceiveTimeout(3000L);
        }
    }

    /**
     * Return a new CsUSerForm
     * 
     * @return a CsUSerForm
     */
    public SubscriptionEquipmentForm getSubscriptionEquipmentForm() {
        return new SubscriptionEquipmentForm();
    }

    /**
     * @param model the model
     * @param xml the xml message
     * @param destination the destination
     */
    protected void sendAndReceiveMessage(Model model, String xml, String destination) {
        String correlationId = getJms().send(INCOMING, xml);
        LOG.info("Sent XML with correlationId " + correlationId);
        ActiveMQBytesMessage message = (ActiveMQBytesMessage) getJms().receiveSelected(destination, createMessageSelectorExpression(correlationId));
        try{
            String value = new String(message.getContent().getData(), "UTF8");
            model.addAttribute("result", HtmlUtils.htmlEscape(value));
            LOG.info("received event = " + value);
        }catch(UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
    }
}
