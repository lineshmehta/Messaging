package com.telenor.cos.messaging.integrationtests;

import com.telenor.cos.messaging.CosCorrelationJmsTemplate;
import com.telenor.cos.messaging.ExternalEndpoints;
import com.telenor.cos.messaging.User;
import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.util.TestHelper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Common Class for all IntegrationTest cases.
 * @author Babaprakash D
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/remoteClient.xml")
public class CommonIntegrationTest {

    private TestHelper testHelper = new TestHelper();

    @Autowired
    private CosCorrelationJmsTemplate jms;

    @Autowired
    private UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter;

    public CosCorrelationJmsTemplate getJms() {
        setUserAndPassword(User.SYSTEM_USER); // default user is system user
        return jms;
    }

    public CosCorrelationJmsTemplate getJms(User user) {
        setUserAndPassword(user);
        return jms;
    }

    public TestHelper getTestHelper() {
        return testHelper;
    }

    protected String sendXmlToRepServerQueueAndReturnCorrelationId(String pathToXml) throws Exception {
        String xmlToSend = testHelper.fileToString(pathToXml);
        return jms.send(ExternalEndpoints.INCOMING_REP_SERVER, xmlToSend);
    }
    
    protected void assertAction(Event event,ACTION action) {
        assertNotNull(event);
        assertEquals("Unexpected action", action, event.getAction());
    }

    private void setUserAndPassword(User user) {
        userCredentialsConnectionFactoryAdapter.setUsername(user.getUsername());
        userCredentialsConnectionFactoryAdapter.setPassword(user.getPassword());
    }
}
