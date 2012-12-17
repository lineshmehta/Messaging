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
public class SubscriptionEquipmentUpdateXpathExtractorTest extends AbstractXpathUnitTest {

    @Autowired
    private SubscriptionEquipmentUpdateXpathExtractor xpathExtrac;

    private Node message;

    private static final String SUBSCRIPTION_EQUIPMENT_UPDATE_XML = "dataset/subscription_equipment_info_update.xml";

    @Before
    public void setUp() throws Exception {
        message =new TestHelper().fileToDom(SUBSCRIPTION_EQUIPMENT_UPDATE_XML);
    }

    @Test
    public void testImsi() {
        assertEquals("89242010110169699", XPathString.getValue(xpathExtrac.getImsiNumberIdNew(message)));
    }

    @Test
    public void testSubscriptionId() {
        assertEquals(Long.valueOf(23), XPathLong.getValue(xpathExtrac.getSubscriptionIdOld(message)));
    }
}
