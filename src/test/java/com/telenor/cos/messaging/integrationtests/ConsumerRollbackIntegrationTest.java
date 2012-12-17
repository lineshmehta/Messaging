package com.telenor.cos.messaging.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telenor.cos.messaging.CosCorrelationJmsTemplate;
import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.User;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountNewEvent;
import com.telenor.cos.messaging.util.TestHelper;
import com.telenor.cos.test.category.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/remoteClientWithStubbedConsumer.xml")
@Category(IntegrationTest.class)
public class ConsumerRollbackIntegrationTest {

    private static final Long ACCOUNT_ID = Long.valueOf(9364740);
    private static final String DEADLETTER_QUEUE = "ActiveMQ.DLQ.Topic.VirtualTopic.Accounts.New"; 

    private static final String NEW_ACC_XML = "dataset/account_new.xml";

    @Autowired
    private CosCorrelationJmsTemplate jms;
    
    @Autowired
    private UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter;

    @Before
    public void before() throws Exception {
        setUserAndPassword(User.SYSTEM_USER); // default user is system user
        jms.setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        jms.receive(ExternalEndpoints.ACCOUNT_NEW);
        jms.receive(DEADLETTER_QUEUE);
        jms.setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_INDEFINITE_WAIT);
        Thread.sleep(2000);
    }

    @Test (timeout = 20000)
    public void shouldThrowExceptionOnConsumerAndMoveMessageToDLQ() throws Exception {
        String newAccountXml = new TestHelper().fileToString(NEW_ACC_XML);
        String correlationId = jms.send(ExternalEndpoints.INCOMING_REP_SERVER, newAccountXml);
        Thread.sleep(2000);
        AccountNewEvent newAccountEvent = (AccountNewEvent) jms.receive(DEADLETTER_QUEUE,correlationId);
        assertNotNull(newAccountEvent);
        assertEquals("Unexpected action", ACTION.CREATED, newAccountEvent.getAction());
        assertEquals("Unexpected Account Id", ACCOUNT_ID, newAccountEvent.getDomainId());
    }

    private void setUserAndPassword(User user) {
        userCredentialsConnectionFactoryAdapter.setUsername(user.getUsername());
        userCredentialsConnectionFactoryAdapter.setPassword(user.getPassword());
    }
}
