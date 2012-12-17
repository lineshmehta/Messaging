package com.telenor.cos.messaging.producers.xpath;

import com.telenor.cos.messaging.producers.xpath.type.XPathDate;
import com.telenor.cos.messaging.producers.xpath.type.XPathLong;
import com.telenor.cos.messaging.producers.xpath.type.XPathString;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

@Component
public class SubscriptionEquipmentUpdateXpathExtractor extends XPathExtractor {

    private static final String SUBSCR_ID_EXPR = "//update[@schema='SUBSCRIPTION_EQUIPMENT_INFO']//values//cell[@name='SUBSCR_ID']";
    private static final String SUBSCR_ID_EXPR_OLD = "//update[@schema='SUBSCRIPTION_EQUIPMENT_INFO']//oldValues//cell[@name='SUBSCR_ID']";
    private static final String IMSI_NUMBER_ID_NEW_EXPR = "//update[@schema='SUBSCRIPTION_EQUIPMENT_INFO']//values//cell[@name='IMSI_NUMBER_ID']";
    private static final String IMSI_NUMBER_OLD_EXPR = "//update[@schema='SUBSCRIPTION_EQUIPMENT_INFO']//oldValues//cell[@name='IMSI_NUMBER_ID']";
    private static final String INFO_IS_DELETED_NEW_EXPR = "//update[@schema='SUBSCRIPTION_EQUIPMENT_INFO']//values//cell[@name='INFO_IS_DELETED']";
    private static final String VALID_TO_DATE_NEW_EXPR = "//update[@schema='SUBSCRIPTION_EQUIPMENT_INFO']//values//cell[@name='VALID_TO_DATE']";

    /**
     * @param message message.
     * @return message.
     */
    public XPathLong getSubscriptionId(Node message) {
        return getLong(SUBSCR_ID_EXPR, message);
    }

    /**
     * @param message message
     * @return message
     */
    public XPathLong getSubscriptionIdOld(Node message) {
        return getLong(SUBSCR_ID_EXPR_OLD, message);
    }

    /**
     * @param message message.
     * @return imsi (new)
     */
    public XPathString getImsiNumberIdNew(Node message) {
        return getString(IMSI_NUMBER_ID_NEW_EXPR, message);
    }

    /**
    * @param message message.
    * @return imsi (old)
    */
   public XPathString getImsiNumberIdOld(Node message) {
       return getString(IMSI_NUMBER_OLD_EXPR, message);
   }

    /**
     * @param message message.
     * @return SystemTypeId.
     */
    public XPathString getInfoIsDeleted(Node message) {
        return getString(INFO_IS_DELETED_NEW_EXPR, message);
    }

    /**
     * @param message message.
     * @return SystemTypeId.
     */
    public XPathDate getValidToDate(Node message) {
        return getDate(VALID_TO_DATE_NEW_EXPR, message);
    }
}
