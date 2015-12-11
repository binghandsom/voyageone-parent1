package com.voyageone.batch.cms.bean.tcb;

/**
 * Created by Leo on 15-6-30.
 */
public class AbortTaskSignalInfo extends TaskSignalInfo {
    private String abortCause;
    private boolean processNextTime = false;

    public AbortTaskSignalInfo(String abortCause) {
        this(abortCause, false);
    }

    public AbortTaskSignalInfo(String abortCause, boolean processNextTime) {
        this.abortCause = abortCause;
        this.processNextTime = processNextTime;
    }

    public String getAbortCause() {
        return abortCause;
    }

    public void setAbortCause(String abortCause) {
        this.abortCause = abortCause;
    }

    public boolean isProcessNextTime() {
        return processNextTime;
    }

    public void setProcessNextTime(boolean processNextTime) {
        this.processNextTime = processNextTime;
    }
}
