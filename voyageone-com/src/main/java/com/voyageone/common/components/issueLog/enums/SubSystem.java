package com.voyageone.common.components.issueLog.enums;

/**
 * Created by Tester on 5/6/2015.
 */
public enum SubSystem {
    COM(1),
    OMS(2),
    WMS(3),
    CMS(4),
    CORE(5),
    IMS(6),
    SYNSHIP(7),
    VMS(8);

    private int id;

    SubSystem(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static SubSystem valueOf(int i) {
        switch (i) {
            case 1:
                return SubSystem.COM;
            case 2:
                return SubSystem.OMS;
            case 3:
                return SubSystem.WMS;
            case 4:
                return SubSystem.CMS;
            case 5:
                return SubSystem.CORE;
            case 6:
                return SubSystem.IMS;
            case 7:
                return SubSystem.SYNSHIP;
            case 8:
                return SubSystem.VMS;
            default:
                return null;
        }
    }
}
