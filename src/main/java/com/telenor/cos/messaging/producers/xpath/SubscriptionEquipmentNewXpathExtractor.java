package com.telenor.cos.messaging.producers.xpath;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

@Component
public class SubscriptionEquipmentNewXpathExtractor extends XPathExtractor {

    private static final String SUBSCR_ID_EXPR = "//insert[@schema='SUBSCRIPTION_EQUIPMENT_INFO']//values//cell[@name='SUBSCR_ID']";
    private static final String IMSI_NUMBER_ID_NEW_EXPR = "//insert[@schema='SUBSCRIPTION_EQUIPMENT_INFO']//values//cell[@name='IMSI_NUMBER_ID']";

    /**
     * @param message message.
     * @return message.
     */
    public XPathLong getSubscriptionId(Node message) {
        return getLong(SUBSCR_ID_EXPR, message);
    }

    /**
     * @param message message.
     * @return imsi (new)
     */
    public XPathString getImsiNumberId(Node message) {
        return getString(IMSI_NUMBER_ID_NEW_EXPR, message);
    }
}
