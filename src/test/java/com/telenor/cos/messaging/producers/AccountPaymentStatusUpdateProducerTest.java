package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountPaymentStatusUpdateEvent;
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
 * Test case for {@link com.telenor.cos.messaging.producers.AccountPaymentStatusUpdateProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class AccountPaymentStatusUpdateProducerTest {

    @Mock
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    private AccountPaymentStatusUpdateProducer accountPaymentStatusUpdateProducer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountPaymentStatusUpdateProducer = new AccountPaymentStatusUpdateProducer();
        ReflectionTestUtils.setField(accountPaymentStatusUpdateProducer, "accountUpdateXpathExtractor",accountUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicableToProducer() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountStatusId(any(Node.class))).thenReturn(XPathString.valueOf("176544"));
        assertTrue(accountPaymentStatusUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testNotApplicableToProducer() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountStatusId(any(Node.class))).thenReturn(null);
        assertFalse(accountPaymentStatusUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountStatusId(any(Node.class))).thenReturn(XPathString.valueOf("176544"));
        AccountPaymentStatusUpdateEvent event = (AccountPaymentStatusUpdateEvent) (accountPaymentStatusUpdateProducer.produceMessage(any(Node.class))).get(0);
        assertEquals(Long.valueOf(32143317), event.getDomainId());
        assertEquals(ACTION.PAYMENT_STATUS_CHANGE, event.getAction());
        assertEquals("176544", event.getPaymentStatus());
    }
}
