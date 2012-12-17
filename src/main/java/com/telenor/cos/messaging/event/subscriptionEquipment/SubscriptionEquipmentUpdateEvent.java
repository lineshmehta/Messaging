package com.telenor.cos.messaging.event.subscriptionEquipment;


import com.telenor.cos.messaging.event.SubscriptionEquipment;

public class SubscriptionEquipmentUpdateEvent extends SubscriptionEquipmentEvent {

    /**
     * Constructor
     *
     * @param subscriptionId the subscription id
     * @param subscriptionEquipment the subscription equipment data
     */
    public SubscriptionEquipmentUpdateEvent(Long subscriptionId, SubscriptionEquipment subscriptionEquipment) {
        super(ACTION.UPDATED, subscriptionId, subscriptionEquipment);
    }

}
