package com.telenor.cos.messaging.event.subscriptionEquipment;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.SubscriptionEquipment;

public class SubscriptionEquipmentEvent extends Event {

    private SubscriptionEquipment subscriptionEquipment;

    /**
     * Constructor
     *
     * @param type the action type
     * @param subscriptionId the subscription id
     * @param subscriptionEquipment the subscription equipment data
     */
    public SubscriptionEquipmentEvent(ACTION type, Long subscriptionId, SubscriptionEquipment subscriptionEquipment) {
        super(subscriptionId, type, TYPE.SUBSCRIPTION_EQUIPMENT);
        this.subscriptionEquipment = subscriptionEquipment;
    }

    public SubscriptionEquipment getSubscriptionEquipment() {
        return subscriptionEquipment;
    }
}
