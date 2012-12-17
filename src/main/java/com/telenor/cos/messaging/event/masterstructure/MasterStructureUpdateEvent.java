package com.telenor.cos.messaging.event.masterstructure;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.MasterStructure;

/**
 * This class creates event for the changes to the parent Id
 * of a given master customer
 * @author t798435
 */
public class MasterStructureUpdateEvent extends Event {

    private static final long serialVersionUID = -8440459854029651292L;

    private MasterStructure masterStructure;

    /**
     * Creates MasterStructure Update event
     * @param masterId --> masterId.
     * @param masterStructure --> will have the changed parentId.
     */
    public MasterStructureUpdateEvent(Long masterId, MasterStructure masterStructure) {
        super(masterId, ACTION.UPDATED, TYPE.MASTERSTRUCTURE);
        this.masterStructure = masterStructure;
    }

    public MasterStructure getMasterStructure() {
        return masterStructure;
    }
}
