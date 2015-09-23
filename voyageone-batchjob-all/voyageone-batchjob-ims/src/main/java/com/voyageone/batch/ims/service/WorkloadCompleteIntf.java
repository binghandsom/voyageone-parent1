package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.modelbean.WorkLoadBean;

/**
 * Created by Leo on 15-7-24.
 */
public interface WorkloadCompleteIntf {
    void onComplete(WorkLoadBean workLoadBean);
}
