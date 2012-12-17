package com.telenor.cos.messaging.routers.aggregation.translators;

import com.telenor.cos.messaging.event.Subscription;
import com.telenor.cos.messaging.event.subscription.NewSubscriptionEvent;
import com.telenor.cos.messaging.producers.xpath.RelSubscriptionXpathExtractor;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link com.telenor.cos.messaging.routers.aggregation.translators.RelNewSubscriptionAggregationTranslator}
 * @author Babaprakash D
 *
 */
public class RelNewSubscriptionAggregationTranslatorTest {

    private final static XPathString DATA_CARD = XPathString.valueOf("DK");
    private final static XPathString DATA_CARD2 = XPathString.valueOf("D2");
    private final static XPathString TWIN_CARD = XPathString.valueOf("TV");
    private static final Long SUBSCR_ID = Long.valueOf(1);
    private static final Long MSISDN = Long.valueOf(1235678);

    @Mock
    private RelSubscriptionXpathExtractor relSubscriptionXpathExtractor;

    private RelNewSubscriptionAggregationTranslator relNewSubscriptionAggregateTranslator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        relNewSubscriptionAggregateTranslator = new RelNewSubscriptionAggregationTranslator();
        ReflectionTestUtils.setField(relNewSubscriptionAggregateTranslator, "relSubscriptionXpathExtractor", relSubscriptionXpathExtractor);
        when(relSubscriptionXpathExtractor.getOwnerSubscriptionId(any(Node.class))).thenReturn(XPathLong.valueOf(SUBSCR_ID));
    }

    @Test
    public void testTranslateForDataKort() {
        when(relSubscriptionXpathExtractor.getRelSubscriptionType(any(Node.class))).thenReturn(DATA_CARD);
        createEventAndAssert(DATA_CARD);
    }

    @Test
    public void testTranslateForDataKort2() {
        when(relSubscriptionXpathExtractor.getRelSubscriptionType(any(Node.class))).thenReturn(DATA_CARD2);
        createEventAndAssert(DATA_CARD2);
    }

    @Test
    public void testTranslateForTwinCard() {
        when(relSubscriptionXpathExtractor.getRelSubscriptionType(any(Node.class))).thenReturn(TWIN_CARD);
        createEventAndAssert(TWIN_CARD);
    }

    private NewSubscriptionEvent createNewSubscriptionEvent() {
        Subscription subscription = new Subscription();
        subscription.setOwnerSubscriptionId(SUBSCR_ID);
        subscription.setMsisdn(MSISDN);
        NewSubscriptionEvent newSubscriptionEvent = new NewSubscriptionEvent(SUBSCR_ID, subscription);
        return newSubscriptionEvent;
    }
    private void createEventAndAssert(XPathString dataType) {
        NewSubscriptionEvent eventAfterTranaslation =(NewSubscriptionEvent)relNewSubscriptionAggregateTranslator.translate(createNewSubscriptionEvent(), any(Node.class));
        if(DATA_CARD.equals(dataType)) {
            assertEquals("Unexpected msisdndatakort",MSISDN,eventAfterTranaslation.getData().getMsisdnDatakort());
        }else if(DATA_CARD2.equals(dataType)) {
            assertEquals("Unexpected msisdndatakort2",MSISDN,eventAfterTranaslation.getData().getMsisdnDatakort2());
        }else if(TWIN_CARD.equals(dataType)) {
            assertEquals("Unexpected msisdntwincard",MSISDN,eventAfterTranaslation.getData().getMsisdnTvilling());
        }
    }
}
