package com.voyageone.batch.ims.bean.tcb;

/**
 * Created by Leo on 15-6-30.
 */
public class AbortTaskSignalInfo extends TaskSignalInfo {
    private String abortCause;

    public AbortTaskSignalInfo(String abortCause) {
        this.abortCause = abortCause;
    }

    public String getAbortCause() {
        return abortCause;
    }

    public void setAbortCause(String abortCause) {
        this.abortCause = abortCause;
    }
}
