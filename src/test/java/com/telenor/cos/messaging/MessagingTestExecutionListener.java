package com.telenor.cos.messaging;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class MessagingTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        JmsTemplate jms = testContext.getApplicationContext().getBean("jmsTemplate", JmsTemplate.class);
        jms.setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        jms.receive(ExternalEndpoints.NEW_SUBSCRIPTIONS);
        jms.receive(ExternalEndpoints.UPDATED_SUBSCRIPTIONS);
        jms.receive(ExternalEndpoints.DELETED_SUBSCRIPTIONS);
        jms.receive(ExternalEndpoints.EXPIRED_SUBSCRIPTIONS);
        jms.receive(ExternalEndpoints.UPDATED_SUBSCRIPTION_STATUS);
        jms.receive(ExternalEndpoints.SECRET_NUMBER_UPADATE);
        jms.setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
    }
}
