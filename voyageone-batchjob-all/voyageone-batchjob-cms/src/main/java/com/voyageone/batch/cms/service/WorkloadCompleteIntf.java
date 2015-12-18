package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.model.WorkLoadBean;

/**
 * Created by Leo on 15-7-24.
 */
public interface WorkloadCompleteIntf {
    void onComplete(WorkLoadBean workLoadBean);
}
