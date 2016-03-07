package com.voyageone.task2.cms.bean.tcb;

/**
 * Created by Leo on 15-6-30.
 */
public class TaskSignal extends Throwable{
    private TaskSignalType signalType;
    private TaskSignalInfo signalInfo;

    public TaskSignal(TaskSignalType signalType, TaskSignalInfo signalInfo) {
        this.signalType = signalType;
        this.signalInfo = signalInfo;
    }

    public TaskSignalType getSignalType() {
        return signalType;
    }

    public void setSignalType(TaskSignalType signalType) {
        this.signalType = signalType;
    }

    public TaskSignalInfo getSignalInfo() {
        return signalInfo;
    }

    public void setSignalInfo(TaskSignalInfo signalInfo) {
        this.signalInfo = signalInfo;
    }
}
