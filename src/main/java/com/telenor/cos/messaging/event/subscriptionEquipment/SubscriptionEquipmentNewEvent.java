package com.telenor.cos.messaging.event.subscriptionEquipment;


import com.telenor.cos.messaging.event.SubscriptionEquipment;

public class SubscriptionEquipmentNewEvent extends SubscriptionEquipmentEvent {

    /**
     * Constructor
     *
     * @param subscriptionId the subscription id
     * @param subscriptionEquipment the subscription equipment data
     */
    public SubscriptionEquipmentNewEvent(Long subscriptionId, SubscriptionEquipment subscriptionEquipment) {
        super(ACTION.CREATED, subscriptionId, subscriptionEquipment);
    }

}
