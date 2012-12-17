package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountNameChangeEvent;
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
 * Test case for {@link com.telenor.cos.messaging.producers.AccountNameChangeProducer}}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class AccountNameChangeProducerTest {

    private AccountNameChangeProducer accountNameChangeProducer;

    @Mock
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountNameChangeProducer = new AccountNameChangeProducer();
        ReflectionTestUtils.setField(accountNameChangeProducer, "accountUpdateXpathExtractor",accountUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicableToProducer() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountName(any(Node.class))).thenReturn(XPathString.valueOf("176544\\DAVID345"));
        assertTrue(accountNameChangeProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testNotApplicableToProducer() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountName(any(Node.class))).thenReturn(null);
        assertFalse(accountNameChangeProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountName(any(Node.class))).thenReturn(XPathString.valueOf("176544\\DAVID345"));
        AccountNameChangeEvent event = (AccountNameChangeEvent) (accountNameChangeProducer.produceMessage(any(Node.class))).get(0);
        assertEquals(Long.valueOf(32143317), event.getDomainId());
        assertEquals(ACTION.NAME_CHANGE, event.getAction());
        assertEquals("176544\\DAVID345", event.getName());
    }
}
