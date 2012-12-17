package com.telenor.cos.messaging.event.masterstructure;

import com.telenor.cos.messaging.event.Event;
import com.telenor.cos.messaging.event.MasterStructure;

/**
 * This class defines a new event for Master Structure
 * i.e. MAST_STRUCTURE table in FKM for mapping
 * MASTER ID (of MASTER_CUSTOMER) to MASTER ID OWNER (of MAST_STRUCTURE)
 * @author t798435
 *
 */
public class MasterStructureNewEvent extends Event {

    private static final long serialVersionUID = -6828423228143692469L;
    
    private MasterStructure masterStructure;

    /**
     * New MasterStructure Event Constructor.
     * @param masterStructure --> the masterStructure object
     */
    public MasterStructureNewEvent(MasterStructure masterStructure) {
        super(masterStructure.getMasterId(), ACTION.CREATED, TYPE.MASTERSTRUCTURE);
        this.masterStructure = masterStructure;
    }

    public MasterStructure getMasterStructure() {
        return masterStructure;
    }
}
