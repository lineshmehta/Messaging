package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountStatusUpdateEvent;
import com.telenor.cos.messaging.producers.xpath.AccountUpdateXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import com.telenor.cos.test.category.ServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link com.telenor.cos.messaging.producers.AccountStatusUpdateProducer}
 * @author t808074
 *
 */
@Category(ServiceTest.class)
public class AccountStatusUpdateProducerTest {
    public static final XPathLong ACC_ID = XPathLong.valueOf("32143317");
    @Mock
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    private AccountStatusUpdateProducer accountStatusUpdateProducer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountStatusUpdateProducer = new AccountStatusUpdateProducer();
        ReflectionTestUtils.setField(accountStatusUpdateProducer, "accountUpdateXPathExtractor",accountUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountStatusId2(any(Node.class))).thenReturn(XPathString.valueOf("ÅP"));
        assertTrue(accountStatusUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testNotApplicable() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountStatusId2(any(Node.class))).thenReturn(null);
        assertFalse(accountStatusUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(ACC_ID);
        when(accountUpdateXpathExtractor.getNewAccountStatusId2(any(Node.class))).thenReturn(XPathString.valueOf("ÅP"));
        AccountStatusUpdateEvent event = (AccountStatusUpdateEvent) (accountStatusUpdateProducer.produceMessage(any(Node.class)).get(0));
        assertEquals(ACC_ID.getValue(), event.getDomainId());
        assertEquals(ACTION.STATUS_UPDATE, event.getAction());
        assertEquals("ÅP", event.getStatus());
    }
}
