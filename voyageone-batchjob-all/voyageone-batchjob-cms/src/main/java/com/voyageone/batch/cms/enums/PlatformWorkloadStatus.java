package com.voyageone.batch.cms.enums;

/**
 * Created by Leo on 15-8-3.
 */
public class PlatformWorkloadStatus implements Cloneable{
    public static final int JOB_INIT = 0;
    public static final int JOB_DONE = 1;
    public static final int JOB_ABORT = 2;
    public static final int JOB_UNDEFINE= 3;

    public static final int ADD_START = 100;
    public static final int UPDATE_START = 200;

    protected int value;

    public PlatformWorkloadStatus(int value) {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public PlatformWorkloadStatus clone() {
        PlatformWorkloadStatus cloneObj = null;
        try {
            cloneObj = (PlatformWorkloadStatus) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return cloneObj;
    }
}
