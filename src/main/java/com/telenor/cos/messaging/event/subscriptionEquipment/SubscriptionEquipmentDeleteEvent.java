package com.telenor.cos.messaging.event.subscriptionEquipment;


import com.telenor.cos.messaging.event.SubscriptionEquipment;

public class SubscriptionEquipmentDeleteEvent extends SubscriptionEquipmentEvent {

    /**
     * Constructor
     *
     * @param subscriptionId the subscription id
     * @param subscriptionEquipment the subscription equipment data
     */
    public SubscriptionEquipmentDeleteEvent(Long subscriptionId, SubscriptionEquipment subscriptionEquipment) {
        super(ACTION.LOGICAL_DELETE, subscriptionId, subscriptionEquipment);
    }

}
