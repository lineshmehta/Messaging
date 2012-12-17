package com.telenor.cos.messaging.producers.xpath;

import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;

@Component
public class RelSubscriptionXpathExtractor extends XPathExtractor {
    
    private static final String OWNER_SUBSCRIPTON_ID_EXPR = "//insert[@schema='REL_SUBSCRIPTION']//values//cell[@name='subscr_id_owner']";
    private static final String REL_SUBSCRIPTON_TYPE_EXPR = "//insert[@schema='REL_SUBSCRIPTION']//values//cell[@name='rel_subscr_type']";
    
    /**
     * Gets OwnerSubscriptionId. 
     * @param message message.
     * @return OwnerSubscriptionId.
     */
    public XPathLong getOwnerSubscriptionId(Node message) {
        return getLong(OWNER_SUBSCRIPTON_ID_EXPR, message);
    }

    /**
     * Gets RelSubscriptionType. 
     * @param message message.
     * @return RelSubscriptionType.
     */
    public XPathString getRelSubscriptionType(Node message) {
        return getString(REL_SUBSCRIPTON_TYPE_EXPR, message);
    }
}
