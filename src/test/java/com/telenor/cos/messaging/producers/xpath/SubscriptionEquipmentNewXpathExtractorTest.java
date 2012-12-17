package com.telenor.cos.messaging.producers.xpath;

import static org.junit.Assert.assertEquals;

import com.telenor.cos.messaging.util.TestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/contextWithoutCamel.xml")
public class SubscriptionEquipmentNewXpathExtractorTest extends AbstractXpathUnitTest {

    @Autowired
    private SubscriptionEquipmentNewXpathExtractor xpathExtrac;

    private Node message;

    private static final String SUBSCRIPTION_EQUIPMENT_NEW_XML = "dataset/subscription_equipment_info_new.xml";

    @Before
    public void setUp() throws Exception {
        message = new TestHelper().fileToDom(SUBSCRIPTION_EQUIPMENT_NEW_XML);
    }

    @Test
    public void testImsi() {
        assertEquals("121231234", XPathString.getValue(xpathExtrac.getImsiNumberId(message)));
    }

    @Test
    public void testSubscriptionId() {
        assertEquals(Long.valueOf(101857), XPathLong.getValue(xpathExtrac.getSubscriptionId(message)));
    }
}
