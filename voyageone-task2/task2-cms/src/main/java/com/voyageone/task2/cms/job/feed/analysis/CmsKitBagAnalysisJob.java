package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.KitBagAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gjl on 2017/2/15.
 */
@Component("CmsKitBagAnalysisJob")
public class CmsKitBagAnalysisJob extends BaseTaskJob {
    @Autowired
    private KitBagAnalysisService kitBagAnalysisService;
    @Override
    protected BaseTaskService getTaskService() {
        return kitBagAnalysisService;
    }
}
