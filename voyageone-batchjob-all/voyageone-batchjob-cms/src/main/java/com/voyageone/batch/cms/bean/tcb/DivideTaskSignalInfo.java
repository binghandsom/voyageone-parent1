package com.voyageone.batch.cms.bean.tcb;

import com.voyageone.batch.cms.model.WorkLoadBean;

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
