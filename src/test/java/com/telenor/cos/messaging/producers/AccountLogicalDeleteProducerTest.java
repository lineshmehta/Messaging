package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountLogicalDeleteEvent;
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
 * Test case for {@link com.telenor.cos.messaging.producers.AccountLogicalDeleteProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class AccountLogicalDeleteProducerTest {

    private AccountLogicalDeleteProducer accountLogicalDeleteProducer;

    @Mock
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountLogicalDeleteProducer = new AccountLogicalDeleteProducer();
        ReflectionTestUtils.setField(accountLogicalDeleteProducer, "accountUpdateXpathExtractor",accountUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicableToProducer() throws Exception {
        when(accountUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("Y"));
        assertTrue(accountLogicalDeleteProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testNotApplicableToProducer() throws Exception {
        when(accountUpdateXpathExtractor.getNewInfoIsDeleted(any(Node.class))).thenReturn(XPathString.valueOf("N"));
        assertFalse(accountLogicalDeleteProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf(32143317));
        AccountLogicalDeleteEvent event = (AccountLogicalDeleteEvent) (accountLogicalDeleteProducer.produceMessage(any(Node.class))).get(0);
        assertEquals((Long) 32143317L, event.getDomainId());
        assertEquals(ACTION.LOGICAL_DELETE, event.getAction());
    }
}
