package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.Event.ACTION;
import com.telenor.cos.messaging.event.account.AccountTypeUpdateEvent;
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
 * Test case for {@link com.telenor.cos.messaging.producers.AccountTypeUpdateProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class AccountTypeUpdateProducerTest {

    @Mock
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    private AccountTypeUpdateProducer accountTypeUpdateProducer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        accountTypeUpdateProducer = new AccountTypeUpdateProducer();
        ReflectionTestUtils.setField(accountTypeUpdateProducer, "accountUpdateXPathExtractor",accountUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicable() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountTypeId(any(Node.class))).thenReturn(XPathString.valueOf("NO"));
        assertTrue(accountTypeUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testNotApplicable() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountTypeId(any(Node.class))).thenReturn(null);
        assertFalse(accountTypeUpdateProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testProduceMessage() throws Exception {
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(XPathLong.valueOf("32143317"));
        when(accountUpdateXpathExtractor.getNewAccountTypeId(any(Node.class))).thenReturn(XPathString.valueOf("NO"));
        AccountTypeUpdateEvent event = (AccountTypeUpdateEvent) (accountTypeUpdateProducer.produceMessage(any(Node.class)).get(0));
        assertEquals(Long.valueOf(32143317), event.getDomainId());
        assertEquals(ACTION.TYPE_CHANGE, event.getAction());
        assertEquals("NO", event.getAccountType());
    }
}
