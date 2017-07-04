package com.voyageone.components.tmall.bean;

import static com.voyageone.components.ComponentConstants.C_MAX_API_ERROR;
import static com.voyageone.components.ComponentConstants.TRY_WAIT_TIME_4TAOBAO;

public class TmallApiExecuteContext {

    public static final TmallApiExecuteContext Default = new TmallApiExecuteContext();

    private int tryCount = C_MAX_API_ERROR;
    private int tryWait = TRY_WAIT_TIME_4TAOBAO;

    public int tryCount() {
        return tryCount;
    }

    public TmallApiExecuteContext tryCount(int tryCount) {
        this.tryCount = tryCount;
        return this;
    }

    public int tryWait() {
        return tryWait;
    }

    public TmallApiExecuteContext tryWait(int tryWait) {
        this.tryWait = tryWait;
        return this;
    }
}
