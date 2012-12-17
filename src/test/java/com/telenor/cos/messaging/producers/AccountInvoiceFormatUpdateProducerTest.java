package com.telenor.cos.messaging.producers;

import com.telenor.cos.messaging.event.account.AccountInvoiceFormatUpdateEvent;
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
 * Test case for {@link com.telenor.cos.messaging.producers.AccountInvoiceFormatUpdateProducer}
 * @author Babaprakash D
 *
 */
@Category(ServiceTest.class)
public class AccountInvoiceFormatUpdateProducerTest {

    @Mock
    private AccountUpdateXpathExtractor accountUpdateXpathExtractor;

    private AccountInvoiceFormatUpdateProducer accountInvoiceFormatProducer;

    private static final XPathString INVOICE_FORMAT = XPathString.valueOf("DONE");
    private static final XPathLong ACCOUNT_ID = XPathLong.valueOf(9138026);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        accountInvoiceFormatProducer = new AccountInvoiceFormatUpdateProducer();
        ReflectionTestUtils.setField(accountInvoiceFormatProducer, "accountUpdateXPathExtractor", accountUpdateXpathExtractor);
    }

    @Test
    public void testIsApplicableToProducerTrue() throws Exception {
        when(accountUpdateXpathExtractor.getNewAccountInvMedium(any(Node.class))).thenReturn(INVOICE_FORMAT);
        assertTrue(accountInvoiceFormatProducer.isApplicable(any(Node.class)));
    }

    @Test
    public void testIsApplicableToProducerFalse() throws Exception {
        when(accountUpdateXpathExtractor.getNewAccountInvMedium(any(Node.class))).thenReturn(null);
        Boolean isApplicable = accountInvoiceFormatProducer.isApplicable(any(Node.class));
        assertFalse(isApplicable);
    }

    @Test 
    public void testProduceMessage() throws Exception {
        when(accountUpdateXpathExtractor.getNewAccountInvMedium(any(Node.class))).thenReturn(INVOICE_FORMAT);
        when(accountUpdateXpathExtractor.getAccountId(any(Node.class))).thenReturn(ACCOUNT_ID);
        AccountInvoiceFormatUpdateEvent accountInvoiceFormatUpdateEvent = (AccountInvoiceFormatUpdateEvent) (accountInvoiceFormatProducer.produceMessage(any(Node.class))).get(0);
        assertEquals("Unextected Invoice Format",INVOICE_FORMAT.getValue(), accountInvoiceFormatUpdateEvent.getInvoiceFormat());
        assertEquals("Unextected AccountId",ACCOUNT_ID.getValue(), accountInvoiceFormatUpdateEvent.getDomainId());
    }
}
