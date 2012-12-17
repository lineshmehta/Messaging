package com.telenor.cos.messaging.web.controller.subscription;

import org.springframework.jms.core.JmsTemplate;

import com.telenor.cos.messaging.web.controller.CommonController;
import com.telenor.cos.messaging.web.form.RelSubscriptionForm;
import com.telenor.cos.messaging.web.form.SubscriptionForm;
import com.telenor.cos.messaging.web.form.UserReferenceForm;

public class SubscriptionCommonController extends CommonController {

    public final static String SUBSCRIPTION_NEW_TOPIC = "Consumer.WTEST.VirtualTopic.Subscriptions.New";
    public final static String SUBSCRIPTION_UPDATED_TOPIC = "Consumer.WTEST.VirtualTopic.Subscriptions.Updated";
    public final static String SUBSCRIPTION_LOGICAL_DELETE_TOPIC = "Consumer.WTEST.VirtualTopic.Subscriptions.LogicalDeleted";
    public final static String SUBSCRIPTION_EXPIRED_TOPIC = "Consumer.WTEST.VirtualTopic.Subscriptions.Expired";
    public final static String SUBSCRIPTION_CHANGE_USER_TOPIC = "Consumer.WTEST.VirtualTopic.Subscriptions.ChangeUser";
    public final static String SUBSCRIPTION_CHANGE_ACCOUNT_TOPIC = "Consumer.WTEST.VirtualTopic.Subscriptions.ChangeAccount";
    public final static String SUBSCRIPTION_CHANGE_TYPE_TOPIC = "Consumer.WTEST.VirtualTopic.Subscriptions.ChangeType";
    public final static String SUBSCRIPTION_CHANGE_STATUS_TOPIC = "Consumer.WTEST.VirtualTopic.Subscriptions.ChangeStatus";
    public final static String SUBSCRIPTION_SECRET_NUMBER_UPDATE_TOPIC = "Consumer.WTEST.VirtualTopic.Subscriptions.SecretNumber";
    public final static String SUBSCRIPTION_SHORT_NUMBER_UPDATE_TOPIC = "Consumer.WTEST.VirtualTopic.Subscriptions.ShortNumber";

    private boolean queuesSetUp = false;

    /**
     * Sets up the JMS-environment.
     */
    public void setUp() {
        if (!queuesSetUp) {
            queuesSetUp = true;
            getJms().setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
            getJms().receive(SUBSCRIPTION_NEW_TOPIC);
            getJms().receive(SUBSCRIPTION_UPDATED_TOPIC);
            getJms().receive(SUBSCRIPTION_LOGICAL_DELETE_TOPIC);
            getJms().receive(SUBSCRIPTION_EXPIRED_TOPIC);
            getJms().receive(SUBSCRIPTION_CHANGE_USER_TOPIC);
            getJms().receive(SUBSCRIPTION_CHANGE_ACCOUNT_TOPIC);
            getJms().receive(SUBSCRIPTION_CHANGE_STATUS_TOPIC);
            getJms().receive(SUBSCRIPTION_SECRET_NUMBER_UPDATE_TOPIC);
            getJms().receive(SUBSCRIPTION_SHORT_NUMBER_UPDATE_TOPIC);
            getJms().setReceiveTimeout(6000L);
        }
    }

    /**
     * @return a form with default values
     */
    protected SubscriptionForm getSubscriptionForm() {
        SubscriptionForm subscriptionForm = new SubscriptionForm();
        addSubscriptionDefaultValues(subscriptionForm);
        return subscriptionForm;
    }

    /**
     * Add default values
     *
     * @param subscriptionForm the form
     */
    protected void addSubscriptionDefaultValues(SubscriptionForm subscriptionForm){
        subscriptionForm.setS212ProductId("04916");
        subscriptionForm.setRelSubscriptionForm(createRelSubscriptionForm());
    }

    /**
     * Creates RelSubscriptionForm with DefaultValues.
     * @return RelSubscriptionForm.
     */
    protected RelSubscriptionForm createRelSubscriptionForm() {
        return new RelSubscriptionForm();
    }

    /**
     * Creates UserReferenceForm with DefaultValues.
     * @return UserReferenceForm.
     */
    protected UserReferenceForm createUserReferenceForm() {
        return new UserReferenceForm();
    }
}
