package com.voyageone.batch.ims.bean.tcb;

import com.voyageone.batch.ims.modelbean.WorkLoadBean;

import java.util.Set;

/**
 * Created by Leo on 15-8-10.
 */
public class DivideTaskSignalInfo extends TaskSignalInfo {
    private Set<WorkLoadBean> subWorkloads;

    public DivideTaskSignalInfo(Set<WorkLoadBean> subWorkloads) {
        this.subWorkloads = subWorkloads;
    }

    public Set<WorkLoadBean> getSubWorkloads() {
        return subWorkloads;
    }

    public void setSubWorkloads(Set<WorkLoadBean> subWorkloads) {
        this.subWorkloads = subWorkloads;
    }
}
