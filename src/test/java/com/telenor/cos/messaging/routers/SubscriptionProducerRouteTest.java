package com.telenor.cos.messaging.routers;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.context.support.AbstractXmlApplicationContext;

import com.telenor.cos.messaging.constants.EndPointUri;
import com.telenor.cos.messaging.util.StubEvent;
import com.telenor.cos.test.category.EmbeddedTest;

@Category(EmbeddedTest.class)
public class SubscriptionProducerRouteTest extends RouterBaseTest {
    
    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[] {"/producerTestContextWithExtraProducers.xml", "/camelTestContext.xml"} );
    }

    @Test(timeout = 10000)
    public void testConfigure() throws Exception {
        template.sendBody(EndPointUri.INCOMING_QUEUE, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><stub/>");
        StubEvent event1 = consumer().receiveBody(EndPointUri.INCOMING_EVENT_QUEUE, StubEvent.class);
        assertTrue(event1.isStub());
        StubEvent event2 = consumer().receiveBody(EndPointUri.INCOMING_EVENT_QUEUE, StubEvent.class);
        assertTrue(event2.isStub());
    }
}
