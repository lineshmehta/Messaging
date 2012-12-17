package com.telenor.cos.messaging.event;

/**
 * Domain objects for Master Structure Events
 * @author t798435
 */
public class MasterStructure extends MasterCustomerId {

    private static final long serialVersionUID = -1L;

    private Long masterIdOwner;

    public Long getMasterIdOwner() {
        return masterIdOwner;
    }

    public void setMasterIdOwner(Long masterIdOwner) {
        this.masterIdOwner = masterIdOwner;
    }

    @Override
    public String toString() {
        return "MasterStructure [masterId=" + getMasterId() + ", masterIdOwner=" + masterIdOwner + "]";
    }
}
