package com.voyageone.batch.cms.service.putaway;

import com.voyageone.batch.cms.bean.WorkLoadBean;

/**
 * Created by Leo on 15-7-24.
 */
public interface WorkloadCompleteIntf {
    void onComplete(WorkLoadBean workLoadBean);
}
